package com.olehka.currencyrates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.olehka.currencyrates.databinding.FragmentRatesBinding
import com.olehka.currencyrates.ui.adapter.RatesAdapter
import com.olehka.currencyrates.ui.viewmodel.RatesViewModel
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class RatesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RatesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentRatesBinding

    private lateinit var listAdapter: RatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRatesBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        listAdapter = RatesAdapter()
        viewModel.rateList.observe(this, Observer { list -> listAdapter.submitList(list) })
        viewDataBinding.ratesList.adapter = listAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadCurrencyRates(currency = "EUR")
            delay(10_000)
            viewModel.loadCurrencyRates(currency = "AUD")
            delay(5_000)
            viewModel.loadCurrencyRates(currency = "GBP")
        }
    }

}