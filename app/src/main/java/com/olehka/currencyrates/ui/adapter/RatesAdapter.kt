package com.olehka.currencyrates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.databinding.RateItemBinding
import com.olehka.currencyrates.ui.viewmodel.RatesViewModel


class RatesAdapter(private val viewModel: RatesViewModel) :
    ListAdapter<CurrencyRate, RatesAdapter.RateViewHolder>(RateItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val currencyRate = getItem(position)
        holder.bind(currencyRate, viewModel)
    }

    override fun onBindViewHolder(
        holder: RateViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        payloads.takeIf { it.isNotEmpty() }?.firstOrNull()?.let { holder.bind(it as Float) }
            ?: holder.bind(getItem(position), viewModel)
    }

    class RateViewHolder private constructor(private val binding: RateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rate: CurrencyRate, viewModel: RatesViewModel) {
            binding.rate = rate
            binding.value.let {
                it.doOnTextChanged { text, _, _, _ ->
                    if (layoutPosition == 0) {
                        text.toString().toFloatOrNull()?.let { value ->
                            viewModel.onBaseValueChanged(value)
                        }
                    }
                }
                it.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && layoutPosition > 0) {
                        viewModel.onBaseCurrencyValueChanged(rate.currency, it.text.toString().toFloat())
                    }
                }
            }
            binding.executePendingBindings()
        }

        fun bind(payload: Float) {
            binding.value.takeIf { !it.isFocused }
                ?.apply { setText(String.format("%.2f", payload)) }
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