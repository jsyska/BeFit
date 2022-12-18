package com.example.befit.usersetup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.befit.MainActivity
import com.example.befit.databinding.FragmentFinishedSetupBinding
import com.example.befit.helpers.Animations
import com.example.befit.helpers.TypeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishedSetupFragment : Fragment() {
    private lateinit var binding: FragmentFinishedSetupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishedSetupBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.logo.alpha = 0f

        val logoAnimator = Animations(binding.logo)

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 50)
        typeWriter.setText("You are all set up! ")
        typeWriter.showText()
        typeWriter.setOnFinishedTypingListener {
            typeWriter.deleteText()
            typeWriter.setOnFinishedDeletingListener {
                typeWriter.setText("It's time to ")
                typeWriter.showText()
                typeWriter.setOnFinishedTypingListener {
                    logoAnimator.fadeIn(1200)
                    navigateToMainActivity()
                }
            }
        }
        return view
    }

    fun navigateToMainActivity() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }
}