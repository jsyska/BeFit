package com.example.befit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.befit.databinding.ActivitySignUpBinding
import com.example.befit.models.User
import com.example.befit.usersetup.UserSetupActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        binding.SignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.SignUp.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT)
                            val user = firebaseAuth.currentUser
                            val uid = user!!.uid
                            database.getReference("users").child(uid).setValue(User(email)).addOnCompleteListener {
                                binding.progressBar.visibility = View.INVISIBLE
                                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                                    val intent = Intent(this, UserSetupActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                }
                            }
                        } else {
                            binding.progressBar.visibility = View.INVISIBLE
                            Snackbar.make(binding.SignUp, "Something went wrong while creating account. Please try again.", Snackbar.LENGTH_SHORT)
                        }
                    }
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                    Snackbar.make(binding.SignUp, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                Snackbar.make(binding.SignUp, "Please fill up all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

