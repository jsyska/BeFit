package com.example.befit.usersetup

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.befit.R
import com.example.befit.databinding.FragmentAgeBinding
import com.example.befit.databinding.FragmentHeightBinding
import com.example.befit.databinding.FragmentWeightBinding
import com.example.befit.databinding.FragmentWelcomeBinding
import com.example.befit.helpers.Animations
import com.example.befit.helpers.TypeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeightFragment : Fragment() {
    private lateinit var binding: FragmentWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.input.alpha = 0f
        binding.button.alpha = 0f

        val inputAnimator = Animations(binding.input)
        val buttonAnimator = Animations(binding.button)

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 50)
        typeWriter.setText("Enter your weight ")
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
                        ?.replace(R.id.fragment_container, GoalFragment())
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }
        }
        return view
    }
}