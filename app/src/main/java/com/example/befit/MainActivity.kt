package com.example.befit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.befit.databinding.ActivityMainBinding
import com.example.befit.databinding.DialogAddProductBinding
import com.example.befit.databinding.DialogSelectQuantityBinding
import com.example.befit.services.FoodApi
import com.example.befit.services.FoodApiResponse
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select date")
                .build()

        binding.date.text = dateFormat.format(datePicker.selection)
        binding.day.text = dayFormat.format(datePicker.selection)

        binding.calendarButton.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.date.text = dateFormat.format(it)
            binding.day.text = dayFormat.format(it)
        }

        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.copyButton.setOnClickListener{
            FoodApi.retrofitService.getProperties("80177173").enqueue(object:
                Callback<FoodApiResponse> {
                override fun onFailure(call: Call<FoodApiResponse>, t: Throwable) {
                    var _response = t.message
                }

                override fun onResponse(
                    call: Call<FoodApiResponse>,
                    response: Response<FoodApiResponse>
                ) {
                    var _response: Response<FoodApiResponse> = response
                    if(_response.isSuccessful){

                        if(_response.body()?.Status == 1){
                            val quantityDialog = MaterialAlertDialogBuilder(binding.copyButton.context)
                            quantityDialog.setTitle(_response.body()?.Product?.ProductName)
                            val view = layoutInflater.inflate(R.layout.dialog_select_quantity, null);
                            quantityDialogBinding = DialogSelectQuantityBinding.inflate(LayoutInflater.from(binding.copyButton.context))
                            quantityDialog.setView(quantityDialogBinding.root)
                            quantityDialog.setPositiveButton("Ok", null)
                            quantityDialog.setNegativeButton("Cancel", null);
                            quantityDialog.show()
                        } else{
                            Toast.makeText(binding.copyButton.context, "No product was found in database.", Toast.LENGTH_SHORT)
                        }
                    } else {
                        Toast.makeText(binding.copyButton.context, "Something went wrong while making a request.", Toast.LENGTH_SHORT)
                    }

                }
            })
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
                    FoodApi.retrofitService.getProperties(barcode).enqueue(object:
                        Callback<FoodApiResponse> {
                        override fun onFailure(call: Call<FoodApiResponse>, t: Throwable) {
                            var _response = t.message
                            Toast.makeText(dialogBinding.scan.context, "Unable to get product data.", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<FoodApiResponse>,
                            response: Response<FoodApiResponse>
                        ) {
                            var _response: Response<FoodApiResponse> = response

                            if(_response.isSuccessful){

                                if(_response.body()?.Status == 1){

                                    val quantityDialog = MaterialAlertDialogBuilder(dialogBinding.scan.context)
                                    quantityDialog.setTitle(_response.body()?.Product?.ProductName)
                                    val view = layoutInflater.inflate(R.layout.dialog_select_quantity, null);
                                    quantityDialogBinding = DialogSelectQuantityBinding.inflate(LayoutInflater.from(dialogBinding.scan.context))
                                    val adapter = ArrayAdapter(dialogBinding.scan.context, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.product_units))
                                    quantityDialogBinding.unitView.setAdapter(adapter)
                                    quantityDialog.setView(quantityDialogBinding.root)
                                    quantityDialog.setPositiveButton("Ok", null)
                                    quantityDialog.setNegativeButton("Cancel", null);
                                    quantityDialog.show()
                                } else{
                                    Toast.makeText(dialogBinding.scan.context, "No product was found in database.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(dialogBinding.scan.context, "Something went wrong while making a request.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        }
}

