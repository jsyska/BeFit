package com.example.befit.usersetup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.befit.R
import com.example.befit.databinding.ActivityUserSetupBinding

class UserSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSetupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSetupBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Add the initial fragment to the frame layout
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, WelcomeFragment())
            .commit()
    }
}