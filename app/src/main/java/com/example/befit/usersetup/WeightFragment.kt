package com.example.befit.usersetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.befit.R
import com.example.befit.databinding.FragmentWeightBinding
import com.example.befit.helpers.FadesAnimations
import com.example.befit.helpers.TypeWriter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class WeightFragment : Fragment() {
    private lateinit var binding: FragmentWeightBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.input.alpha = 0f
        binding.button.alpha = 0f

        database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        val inputAnimator = FadesAnimations(binding.input)
        val buttonAnimator = FadesAnimations(binding.button)

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 50)
        typeWriter.setText("Enter your weight ")
        typeWriter.showText()
        typeWriter.setOnFinishedTypingListener {
            inputAnimator.fadeIn(600)
            buttonAnimator.fadeIn(600)
        }

        binding.button.setOnClickListener{
            if(binding.input.text.isNotEmpty()) {
                usersRef.child(currentUserId.toString()).child("weight").setValue(binding.input.text.toString()).addOnCompleteListener {
                    typeWriter.deleteText()
                    inputAnimator.fadeOut(600)
                    buttonAnimator.fadeOut(600)
                    typeWriter.setOnFinishedDeletingListener {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment_container, GenderFragment())
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                }
            }
        }
        return view
    }
}