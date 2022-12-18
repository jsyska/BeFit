package com.example.befit.usersetup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.befit.R
import com.example.befit.databinding.FragmentAgeBinding
import com.example.befit.helpers.Animations
import com.example.befit.helpers.TypeWriter

class AgeFragment : Fragment() {
    private lateinit var binding: FragmentAgeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.input.alpha = 0f
        binding.button.alpha = 0f

        val inputAnimator = Animations(binding.input)
        val buttonAnimator = Animations(binding.button)

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 50)
        typeWriter.setText("Enter your age:")
        typeWriter.showText()
        typeWriter.setOnFinishedTypingListener {
            inputAnimator.fadeIn(600)
            buttonAnimator.fadeIn(600)
        }

        binding.button.setOnClickListener{
            if(binding.input.text.isNotEmpty()){
                typeWriter.deleteText()
                inputAnimator.fadeOut(600)
                buttonAnimator.fadeOut(600)
                typeWriter.setOnFinishedDeletingListener {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, HeightFragment())
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }
        }
        return view
    }
}