package com.example.befit.usersetup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.befit.R
import com.example.befit.databinding.FragmentWelcomeBinding
import com.example.befit.helpers.TypeWriter


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val typeWriter = TypeWriter(binding.text)
        typeWriter.setCharacterDelay(50, 150, 30)
        typeWriter.setText("Hi, now it's time to set up your account ")
        typeWriter.showText()
        typeWriter.setOnFinishedTypingListener {
            typeWriter.deleteText()
            typeWriter.setOnFinishedDeletingListener {
                // Replace fragment
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, AgeFragment())
                    ?.addToBackStack(null)
                    ?.commit()

            }
        }
        return view
    }
}