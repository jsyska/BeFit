package com.example.befit.helpers

import android.text.SpannableStringBuilder
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TypeWriter(private val mTextView: TextView) {

    private var mIndex: Int = 0
    private var mDelay: Long = 150 // in ms
    private var mText: String = ""
    private var mPunctuationDelay: Long = 300
    private var mDeleteDelay: Long = 50
    private var onFinishedTypingListener: (() -> Unit)? = null
    private var onFinishedDeletingListener: (() -> Unit)? = null




    fun setCharacterDelay(charactersDelay: Long, punctuationsDelay: Long, deleteDelay: Long): TypeWriter {
        mDelay = charactersDelay
        mPunctuationDelay = punctuationsDelay
        mDeleteDelay = deleteDelay
        return this
    }


    fun setText(text: String): TypeWriter {
        mText = text
        return this
    }

    fun showText() {
        mIndex = 0
        mTextView.text = ""
        GlobalScope.launch {
            while (mIndex < mText.length) {
                // Get the current character
                val c = mText[mIndex]
                // Set the delay based on the character type
                val delay = when {
                    c.isWhitespace() -> mPunctuationDelay // longer delay for spaces and punctuation marks
                    else -> mDelay // shorter delay for letters and numbers
                }
                // Update the text and delay
                val ssb = SpannableStringBuilder(mText.substring(0, mIndex++))
                ssb.append("|")
                mTextView.text = ssb
                delay(delay)
            }
            onFinishedTypingListener?.invoke()
        }
    }

    fun deleteText() {
        mIndex = mTextView.text.lastIndex
        GlobalScope.launch {
            delay(500)
            while(mIndex > 0){
                val ssb = SpannableStringBuilder(mText.substring(0, --mIndex))
                if(mIndex > 1) ssb.append("|")
                mTextView.text = ssb
                delay(mDeleteDelay)
            }
            onFinishedDeletingListener?.invoke()
        }
    }

    fun setOnFinishedTypingListener(listener: () -> Unit) {
        onFinishedTypingListener = listener
    }

    fun setOnFinishedDeletingListener(listener: () -> Unit) {
        onFinishedDeletingListener = listener
    }
}