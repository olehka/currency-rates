package com.olehka.currencyrates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.databinding.RateItemBinding


class RatesAdapter : ListAdapter<CurrencyRate, RatesAdapter.RateViewHolder>(RateItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val currencyRate = getItem(position)
        holder.bind(currencyRate)
    }

    class RateViewHolder private constructor(val binding: RateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currencyRate: CurrencyRate) {
            binding.rate = currencyRate
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RateItemBinding.inflate(layoutInflater, parent, false)
                return RateViewHolder(binding)
            }
        }
    }
}