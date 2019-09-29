package com.olehka.currencyrates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.olehka.currencyrates.databinding.FragmentRatesBinding
import com.olehka.currencyrates.ui.adapter.RatesAdapter
import com.olehka.currencyrates.ui.viewmodel.RatesViewModel
import com.olehka.currencyrates.util.ConnectionStateMonitor
import com.olehka.currencyrates.util.setupSnackbar
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class RatesFragment : DaggerFragment(), ConnectionStateMonitor.OnNetworkAvailableCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var connectionStateMonitor: ConnectionStateMonitor

    private val viewModel by viewModels<RatesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentRatesBinding

    private lateinit var listAdapter: RatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRatesBinding.inflate(
            inflater, container, false
        ).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectionStateMonitor = ConnectionStateMonitor(context!!, this)

        view?.setupSnackbar(this, viewModel.shackbarMessage)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        listAdapter = RatesAdapter(viewModel)
        viewDataBinding.ratesList.adapter = listAdapter
    }

    override fun onResume() {
        super.onResume()
        connectionStateMonitor.enable()
        if (connectionStateMonitor.hasNetworkConnection()) onConnected()
        else onDisconnected()
    }

    override fun onPause() {
        viewModel.cancelPeriodicCurrencyRatesUpdate()
        connectionStateMonitor.disable()
        super.onPause()
    }

    override fun onConnected() {
        Timber.v("onConnected")
        lifecycleScope.launch {
            viewModel.startPeriodicCurrencyRatesUpdate()
        }
    }

    override fun onDisconnected() {
        Timber.v("onDisconnected")
        lifecycleScope.launch {
            viewModel.cancelPeriodicCurrencyRatesUpdate()
            viewModel.clearCurrencyRatesList()
        }
    }
}