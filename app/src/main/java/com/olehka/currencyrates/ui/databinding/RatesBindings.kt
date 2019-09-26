package com.olehka.currencyrates.ui.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.olehka.currencyrates.util.getCurrencyFlagResId

@BindingAdapter("android:text")
fun setFloat(textView: TextView, float: Float) {
    if (float.isNaN()) {
        textView.text = ""
    } else {
        textView.text = String.format("%.2f", float)
    }
}

@BindingAdapter("app:setFlag")
fun setFlagImage(imageView: ImageView, code: String) {
    imageView.setImageResource(
        getCurrencyFlagResId(imageView.context, code.toLowerCase())
    )
}