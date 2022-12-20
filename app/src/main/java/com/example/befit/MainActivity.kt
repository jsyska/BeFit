package com.example.befit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.befit.adapters.ProductAdapter
import com.example.befit.barcodescanner.CamActivity
import com.example.befit.database.DatabaseManager
import com.example.befit.databinding.ActivityMainBinding
import com.example.befit.databinding.DialogAddProductBinding
import com.example.befit.databinding.DialogSelectQuantityBinding
import com.example.befit.models.Product
import com.example.befit.models.User
import com.example.befit.services.FoodApi
import com.example.befit.services.FoodApiResponse
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: DialogAddProductBinding
    private lateinit var quantityDialogBinding: DialogSelectQuantityBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yy")
    private val dayFormat = SimpleDateFormat("EEEE")
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var user: User
    private lateinit var productAdapter: ProductAdapter
    private lateinit var currentUserId: String

    private var totalKcal: Int = 0
    private var totalProtein: Int = 0
    private var totalFat: Int = 0
    private var totalCarb: Int = 0

    private var maxKcal: Int = 0
    private var maxProtein: Int = 0
    private var maxFat: Int = 0
    private var maxCarb: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //hiding app bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // downloading user data from firebase
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val userRef = database.getReference("users").child(currentUserId)



        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select date")
                .build()

        binding.date.text = dateFormat.format(datePicker.selection)
        binding.day.text = dayFormat.format(datePicker.selection)

        //downloading products for selected date and setting recycle view
        val productDao = DatabaseManager.getInstance(this).productDao()
        CoroutineScope(Dispatchers.Main).launch {
            val products = productDao.getProductsForDate(binding.date.text.toString(), currentUserId)
            productAdapter = ProductAdapter(products, this@MainActivity)
            binding.productList.adapter = productAdapter
            calculateTotalNutrition(products)
        }

        //updating calories data when user data changes
        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)!!
                val calories = user.calculateCaloricIntake()
                val macronutrients = calculateMacronutrients(calories)

                maxKcal = calories
                maxProtein = macronutrients["protein"]!!.toInt()
                maxFat = macronutrients["fat"]!!.toInt()
                maxCarb = macronutrients["carb"]!!.toInt()
                updateNutritionBars()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext,"An error occurred while fetching user Data!", Toast.LENGTH_SHORT)
            }
        })

        binding.calendarButton.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.date.text = dateFormat.format(it)
            binding.day.text = dayFormat.format(it)
            CoroutineScope(Dispatchers.IO).launch {
                val products = productDao.getProductsForDate(binding.date.text.toString(), currentUserId)
                productAdapter = ProductAdapter(products, this@MainActivity)
                binding.productList.adapter = productAdapter
                calculateTotalNutrition(products)
                updateNutritionBars()
            }
        }


        binding.settingsButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val intent = Intent(this, SettingsActivity::class.java)

            userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user = dataSnapshot.getValue(User::class.java)!!
                    intent.putExtra("height", user.height)
                    intent.putExtra("goal", user.goal)
                    intent.putExtra("dailyActivity", user.dailyActivity)
                    intent.putExtra("weight", user.weight)
                    intent.putExtra("age", user.age)
                    intent.putExtra("trainings", user.trainingsPerWeek)
                    intent.putExtra("gender", user.gender)
                    intent.putExtra("changeSpeed", user.changeSpeed)
                    binding.progressBar.visibility = View.INVISIBLE
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext,"An error occurred while fetching user Data!", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val copyDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select date")
                .build()

        binding.copyButton.setOnClickListener{
            copyDatePicker.show(supportFragmentManager, "tag")
        }

        copyDatePicker.addOnPositiveButtonClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var products = productDao.getProductsForDate(dateFormat.format(it), currentUserId)

                for (product in products) {
                    product.id = 0
                    product.date = binding.date.text.toString()
                }
                productDao.insertAll(products)

                val updatedProducts = productDao.getProductsForDate(binding.date.text.toString(), currentUserId)
                productAdapter = ProductAdapter(updatedProducts, this@MainActivity)
                binding.productList.adapter = productAdapter
                calculateTotalNutrition(updatedProducts)
                updateNutritionBars()
            }
        }

        binding.addButton.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setTitle("Add product")
            val view = layoutInflater.inflate(R.layout.dialog_add_product, null);
            dialogBinding = DialogAddProductBinding.inflate(LayoutInflater.from(this))
            dialog.setView(dialogBinding.root)
            dialog.setPositiveButton("Ok", null)
            dialog.setNegativeButton("Cancel", null);
            dialog.show()

            dialogBinding.scan.setOnClickListener{
                val i = Intent(this, CamActivity::class.java)
                getContent.launch(i)
            }
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val barcode = it?.data?.getStringExtra("BarcodeResult")
                if (barcode != null) {
                    binding.progressBar.visibility = View.VISIBLE
                    FoodApi.retrofitService.getProperties(barcode).enqueue(object:
                        Callback<FoodApiResponse> {
                        override fun onFailure(call: Call<FoodApiResponse>, t: Throwable) {
                            Toast.makeText(dialogBinding.scan.context, "Unable to get product data.", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.INVISIBLE
                        }

                        override fun onResponse(
                            call: Call<FoodApiResponse>,
                            response: Response<FoodApiResponse>
                        ) {
                            var _response: Response<FoodApiResponse> = response

                            if(_response.isSuccessful){

                                if(_response.body()?.Status == 1){
                                    val responseProduct = _response.body()?.Product!!

                                    binding.progressBar.visibility = View.INVISIBLE

                                    val quantityDialog = MaterialAlertDialogBuilder(dialogBinding.scan.context)
                                    quantityDialog.setTitle(responseProduct.ProductName)
                                    quantityDialogBinding = DialogSelectQuantityBinding.inflate(LayoutInflater.from(dialogBinding.scan.context))
                                    val adapter = ArrayAdapter(dialogBinding.scan.context, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.product_units))
                                    quantityDialogBinding.unitView.setAdapter(adapter)
                                    quantityDialog.setView(quantityDialogBinding.root)
                                    quantityDialog.setPositiveButton("Add"){ _, _ ->
                                        val amountVal = quantityDialogBinding.amount.text.toString().toInt()
                                        val product = Product(0,
                                            responseProduct.ProductName,
                                            binding.date.text.toString(),
                                            responseProduct.Nutriments.Carbohydrates100g,
                                            responseProduct.Nutriments.EnergyKcal100g,
                                            responseProduct.Nutriments.Fat100g,
                                            responseProduct.Nutriments.Proteins100g,
                                            quantityDialogBinding.amount.text.toString().toInt(),
                                            currentUserId,
                                            responseProduct.Nutriments.EnergyKcal100g * amountVal / 100,
                                            responseProduct.Nutriments.Fat100g * amountVal / 100,
                                            responseProduct.Nutriments.Proteins100g * amountVal / 100,
                                            responseProduct.Nutriments.Carbohydrates100g * amountVal / 100)

                                        productAdapter.addProduct(product)
                                        val productDao = DatabaseManager.getInstance(applicationContext).productDao()
                                        CoroutineScope(Dispatchers.Main).launch {
                                            productDao.insert(product)
                                            val products = productDao.getProductsForDate(binding.date.text.toString(), currentUserId)
                                            calculateTotalNutrition(products)
                                            updateNutritionBars()
                                        }
                                        Toast.makeText(applicationContext, responseProduct.ProductName + " successfully added" ,Toast.LENGTH_SHORT).show()
                                    }
                                    quantityDialog.setNegativeButton("Cancel", null);
                                    quantityDialog.show()
                                } else{
                                    Toast.makeText(dialogBinding.scan.context, "Sorry, we don't have data about this product.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(dialogBinding.scan.context, "We couldn't fetch data about this product.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
                else{
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }

        private fun calculateMacronutrients(calories: Int): Map<String, Int> {
            val proteinGrams = (calories * 0.20) / 4  // Protein has 4 calories per gram
            val fatGrams = (calories * 0.35) / 9  // Fat has 9 calories per gram
            val carbGrams = (calories * 0.45) / 4  // Carbs have 4 calories per gram

            return mapOf(
                "protein" to proteinGrams.toInt(),
                "fat" to fatGrams.toInt(),
                "carb" to carbGrams.toInt()
            )
        }

    public fun calculateTotalNutrition(products: MutableList<Product>) {
        var totalCalories = 0
        var totalProteins = 0
        var totalFats = 0
        var totalCarbs = 0
        for (product in products) {
            totalCalories += product.kcal.toInt()
            totalProteins += product.protein.toInt()
            totalFats += product.fat.toInt()
            totalCarbs += product.carb.toInt()
        }
        this.totalKcal = totalCalories
        this.totalFat = totalFats
        this.totalProtein = totalProteins
        this.totalCarb = totalCarbs
    }

    public fun updateNutritionBars(){
        binding.caloriesCount.text = "$totalKcal/$maxKcal"
        binding.proteinCount.text = "$totalProtein/$maxProtein"
        binding.fatCount.text = "$totalFat/$maxFat"
        binding.carbCount.text = "$totalCarb/$maxCarb"
        binding.caloriesBar.max = maxKcal
        binding.caloriesBar.progress = totalKcal
        binding.proteinsBar.max = maxProtein
        binding.proteinsBar.progress = totalProtein
        binding.fatBar.max = maxFat
        binding.fatBar.progress = totalFat
        binding.carbBar.max = maxCarb
        binding.carbBar.progress = totalCarb
    }
}

