package com.example.befit.usersetup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.befit.R
import com.example.befit.databinding.FragmentGenderBinding
import com.example.befit.helpers.FadesAnimations
import com.example.befit.helpers.TypeWriter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class GenderFragment : Fragment() {
    private lateinit var binding: FragmentGenderBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenderBinding.inflate(inflater, container, false)
        val view = binding.root

        database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        binding.input.alpha = 0f
        binding.button.alpha = 0f

        val inputAnimator = FadesAnimations(binding.input)
        val buttonAnimator = FadesAnimations(binding.button)

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 50)
        typeWriter.setText("Set your gender ")
        typeWriter.showText()
        typeWriter.setOnFinishedTypingListener {
            inputAnimator.fadeIn(600)
            buttonAnimator.fadeIn(600)
        }

        binding.button.setOnClickListener{
            if(binding.input.selectedItem != null){
                usersRef.child(currentUserId.toString()).child("gender").setValue(binding.input.selectedItem.toString()).addOnCompleteListener {
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
        }
        return view
    }
}