package com.olehka.currencyrates.ui.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun setFloat(textView: TextView, float: Float) {
    if (float.isNaN()) {
        textView.text = ""
    } else {
        textView.text = String.format("%.2f", float)
    }
}