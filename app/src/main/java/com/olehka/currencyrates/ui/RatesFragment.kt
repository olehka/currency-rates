package com.olehka.currencyrates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.olehka.currencyrates.databinding.FragmentRatesBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRatesBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadRates(currency = "EUR")
            delay(10_000)
            viewModel.loadRates(currency = "AUD")
        }
    }

}