package com.example.befit.helpers

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Animations(private val view: View) {

    fun fadeIn(duration: Long) {
        GlobalScope.launch(Dispatchers.Main) {
            val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            animator.duration = duration
            animator.start()
        }
    }

    fun fadeOut(duration: Long) {
        GlobalScope.launch(Dispatchers.Main) {
            val animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            animator.duration = duration
            animator.start()
        }
    }
}