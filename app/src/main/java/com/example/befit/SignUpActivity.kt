package com.example.befit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.befit.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Snackbar.make(binding.button, "Signed up successfully.", Snackbar.LENGTH_SHORT).setAction("Log in"){
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                                .show()
                            //Toast.makeText(this, "Signed up successfully. Now u can log in.", Toast.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(binding.button, "Something went wrong while creating account. Please try again.", Snackbar.LENGTH_SHORT)
                        }
                    }
                } else {
                    Snackbar.make(binding.button, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.button, "Please fill up all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}