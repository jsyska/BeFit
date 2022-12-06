package com.example.befit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.befit.databinding.ActivityMainBinding
import com.example.befit.databinding.DialogAddProductBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: DialogAddProductBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yy")
    private val dayFormat = SimpleDateFormat("EEEE")

    private var _barcode: String = ""

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

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val barcode = it?.data?.getStringExtra("BarcodeResult")
                if (barcode != null) {
                }
            }
        }


}

