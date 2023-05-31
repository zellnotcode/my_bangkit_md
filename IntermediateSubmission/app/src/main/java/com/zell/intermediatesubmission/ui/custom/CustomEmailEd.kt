package com.zell.intermediatesubmission.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.helper.isValidEmail

class CustomEmailEd : AppCompatEditText {

    private var isValid: Boolean = false
        set(value) {
            field = value
            onValidChangedListeners.forEach { it.invoke(value) }
        }

    private val onValidChangedListeners = mutableListOf<(Boolean) -> Unit>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s.toString())) {
                    error = "Email format is invalid"
                    validStateChange(false)
                } else {
                    validStateChange(true)
                }
            }
        })
    }

    private fun validStateChange(boolean: Boolean) {
        isValid = boolean
    }

    fun addOnValidChangedListener(listener: (Boolean) -> Unit) {
        onValidChangedListeners.add(listener)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
        hint = "Email"
    }
}