package com.example.befit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.befit.databinding.ActivitySignUpBinding
import com.example.befit.usersetup.UserSetupActivity
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

        binding.SignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.SignUp.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(binding.SignUp.context, "Signed up successfully", Toast.LENGTH_SHORT)
                            val intent = Intent(this, UserSetupActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        } else {
                            Snackbar.make(binding.SignUp, "Something went wrong while creating account. Please try again.", Snackbar.LENGTH_SHORT)
                        }
                    }
                } else {
                    Snackbar.make(binding.SignUp, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.SignUp, "Please fill up all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

