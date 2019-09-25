package com.olehka.currencyrates.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.olehka.currencyrates.data.CurrencyRate

class RateItemDiffCallback : DiffUtil.ItemCallback<CurrencyRate>() {
    override fun areItemsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem == newItem
    }
}