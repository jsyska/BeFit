package com.example.befit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.befit.databinding.ActivitySettingsBinding
import com.example.befit.databinding.DialogDotNumberPickerBinding
import com.example.befit.databinding.DialogNumberPickerBinding
import com.example.befit.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsActivity : AppCompatActivity() {

    private lateinit var singleDialogBinding: DialogNumberPickerBinding
    private lateinit var doubleDialogBinding: DialogDotNumberPickerBinding
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = database.getReference("users").child(currentUserId.toString())

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.height.text = "Height: " + intent.getStringExtra("height")
        binding.weight.text = "Weight: " + intent.getStringExtra("weight")
        binding.age.text = "Age: " + intent.getStringExtra("age")
        binding.goal.text = "Goal: " + intent.getStringExtra("goal")
        binding.trainings.text = "Trainings per week: " + intent.getStringExtra("trainings")
        binding.gender.text = "Gender: " + intent.getStringExtra("gender")
        binding.activity.text = "Daily activity: " + intent.getStringExtra("dailyActivity")
        binding.changeSpeed.text = "Change speed: " + intent.getStringExtra("changeSpeed")





        binding.logOutButton.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }

        binding.weightEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.weightEdit.context)
            dialog.setTitle("Weight")
            doubleDialogBinding = DialogDotNumberPickerBinding.inflate(LayoutInflater.from(binding.weightEdit.context))

            doubleDialogBinding.firstPicker.minValue = 0
            doubleDialogBinding.firstPicker.maxValue = 300
            doubleDialogBinding.firstPicker.value = 84 // TODO downloaded from DB

            doubleDialogBinding.secondPicker.minValue = 0
            doubleDialogBinding.secondPicker.maxValue = 9
            doubleDialogBinding.secondPicker.value = 0

            dialog.setView(doubleDialogBinding.root)
            dialog.setPositiveButton("Set") { _, _ ->
                val result = doubleDialogBinding.firstPicker.value.toString() + "." + doubleDialogBinding.secondPicker.value.toString()
                binding.weight.text = "Weight: $result"
                userRef.child("weight").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Weight updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.genderEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.genderEdit.context)
            dialog.setTitle("Gender")
            singleDialogBinding = DialogNumberPickerBinding.inflate(LayoutInflater.from(binding.genderEdit.context))

            singleDialogBinding.picker.minValue = 0
            singleDialogBinding.picker.maxValue = 1
            singleDialogBinding.picker.value = 0
            val pickerVals = arrayOf("Male", "Female")
            singleDialogBinding.picker.displayedValues = pickerVals

            dialog.setView(singleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = pickerVals[singleDialogBinding.picker.value]
                binding.gender.text = "Gender: $result"
                userRef.child("gender").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Gender updated successfully", Toast.LENGTH_SHORT).show()
                }

            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.ageEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.ageEdit.context)
            dialog.setTitle("Age")
            singleDialogBinding = DialogNumberPickerBinding.inflate(LayoutInflater.from(binding.ageEdit.context))

            singleDialogBinding.picker.minValue = 0
            singleDialogBinding.picker.maxValue = 100
            singleDialogBinding.picker.value = 21

            dialog.setView(singleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = singleDialogBinding.picker.value.toString()
                binding.age.text = "Age: $result"
                userRef.child("age").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Age updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.heightEdit.setOnClickListener{
            val dialog = MaterialAlertDialogBuilder(binding.heightEdit.context)
            dialog.setTitle("Height")
            doubleDialogBinding = DialogDotNumberPickerBinding.inflate(LayoutInflater.from(binding.heightEdit.context))

            doubleDialogBinding.firstPicker.minValue = 0
            doubleDialogBinding.firstPicker.maxValue = 300
            doubleDialogBinding.firstPicker.value = 184

            doubleDialogBinding.secondPicker.minValue = 0
            doubleDialogBinding.secondPicker.maxValue = 9
            doubleDialogBinding.secondPicker.value = 0
            dialog.setView(doubleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = doubleDialogBinding.firstPicker.value.toString() + "." + doubleDialogBinding.secondPicker.value.toString()
                binding.height.text = "Height: $result"
                userRef.child("height").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Height updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.goalEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.goalEdit.context)
            dialog.setTitle("Goal")
            singleDialogBinding = DialogNumberPickerBinding.inflate(LayoutInflater.from(binding.goalEdit.context))

            singleDialogBinding.picker.minValue = 0
            singleDialogBinding.picker.maxValue = 2
            singleDialogBinding.picker.value = 0
            val pickerVals = arrayOf("Lose weight", "Maintain weight", "Gain weight")
            singleDialogBinding.picker.displayedValues = pickerVals

            dialog.setView(singleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = pickerVals[singleDialogBinding.picker.value]
                binding.goal.text = "Goal: $result"
                userRef.child("goal").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Goal updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.activityEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.activityEdit.context)
            dialog.setTitle("Daily activity")
            singleDialogBinding = DialogNumberPickerBinding.inflate(LayoutInflater.from(binding.activityEdit.context))

            singleDialogBinding.picker.minValue = 0
            singleDialogBinding.picker.maxValue = 2
            singleDialogBinding.picker.value = 0
            val pickerVals = arrayOf("Low", "Medium", "High")
            singleDialogBinding.picker.displayedValues = pickerVals

            dialog.setView(singleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = pickerVals[singleDialogBinding.picker.value]
                binding.activity.text = "Daily activity: $result"
                userRef.child("dailyActivity").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Daily activity updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }


        binding.trainingsEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.trainingsEdit.context)
            dialog.setTitle("Trainings per week")
            singleDialogBinding = DialogNumberPickerBinding.inflate(LayoutInflater.from(binding.trainingsEdit.context))

            singleDialogBinding.picker.minValue = 0
            singleDialogBinding.picker.maxValue = 3
            singleDialogBinding.picker.value = 0
            val pickerVals = arrayOf("0", "1-3", "4-5", "6-7")
            singleDialogBinding.picker.displayedValues = pickerVals

            dialog.setView(singleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = pickerVals[singleDialogBinding.picker.value]
                binding.trainings.text = "Trainings per week: $result"
                userRef.child("trainingsPerWeek").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Number of trainings updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.changeSpeedEdit.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.changeSpeedEdit.context)
            dialog.setTitle("Change speed")
            doubleDialogBinding = DialogDotNumberPickerBinding.inflate(LayoutInflater.from(binding.changeSpeedEdit.context))

            doubleDialogBinding.firstPicker.minValue = 0
            doubleDialogBinding.firstPicker.maxValue = 2
            doubleDialogBinding.firstPicker.value = 0

            doubleDialogBinding.secondPicker.minValue = 0
            doubleDialogBinding.secondPicker.maxValue = 9
            doubleDialogBinding.secondPicker.value = 5

            dialog.setView(doubleDialogBinding.root)
            dialog.setPositiveButton("Set"){ _, _ ->
                val result = doubleDialogBinding.firstPicker.value.toString() + "." + doubleDialogBinding.secondPicker.value.toString()
                binding.changeSpeed.text = "Change speed: $result"
                userRef.child("changeSpeed").setValue(result).addOnCompleteListener{
                    Toast.makeText(this,"Change speed updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("Cancel", null);
            dialog.show()
        }

        binding.back.setOnClickListener{
            finish()
        }
    }


}