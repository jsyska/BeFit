package com.example.befit.usersetup

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import com.example.befit.R
import com.example.befit.databinding.FragmentAgeBinding
import com.example.befit.databinding.FragmentDailyActivityBinding
import com.example.befit.databinding.FragmentGoalBinding
import com.example.befit.databinding.FragmentHeightBinding
import com.example.befit.databinding.FragmentTrainingsBinding
import com.example.befit.databinding.FragmentWelcomeBinding
import com.example.befit.helpers.Animations
import com.example.befit.helpers.TypeWriter
import com.google.firebase.database.core.view.Change
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrainingsFragment : Fragment() {
    private lateinit var binding: FragmentTrainingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainingsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.input.alpha = 0f
        binding.button.alpha = 0f

        val inputAnimator = Animations(binding.input)
        val buttonAnimator = Animations(binding.button)

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 50)
        typeWriter.setText("Choose number of trainings per week ")
        typeWriter.showText()
        typeWriter.setOnFinishedTypingListener {
            inputAnimator.fadeIn(600)
            buttonAnimator.fadeIn(600)
        }

        binding.button.setOnClickListener{
            if(binding.input.selectedItem != null){
                typeWriter.deleteText()
                inputAnimator.fadeOut(600)
                buttonAnimator.fadeOut(600)
                typeWriter.setOnFinishedDeletingListener {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, ChangeSpeedFragment())
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }
        }


        return view
    }
}