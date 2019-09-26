package com.olehka.currencyrates.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.olehka.currencyrates.data.CurrencyRate

class RateItemDiffCallback : DiffUtil.ItemCallback<CurrencyRate>() {
    override fun areItemsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem.currency == newItem.currency
    }

    override fun areContentsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: CurrencyRate, newItem: CurrencyRate): Float {
        return newItem.value
    }
}