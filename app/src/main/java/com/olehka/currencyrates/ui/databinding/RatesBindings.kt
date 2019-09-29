package com.olehka.currencyrates.ui.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.ui.adapter.RatesAdapter
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
fun setFlag(imageView: ImageView, code: String) {
    imageView.setImageResource(
        getCurrencyFlagResId(imageView.context, code.toLowerCase())
    )
}

@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: List<CurrencyRate>) {
    (recyclerView.adapter as RatesAdapter).submitList(items)
}