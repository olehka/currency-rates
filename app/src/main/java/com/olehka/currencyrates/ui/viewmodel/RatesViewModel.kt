package com.olehka.currencyrates.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olehka.currencyrates.data.RatesRepository
import com.olehka.currencyrates.data.Result
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class RatesViewModel
@Inject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    private lateinit var ratesJob: Job

    fun loadCurrencyRates(currency: String) {
        if (::ratesJob.isInitialized && ratesJob.isActive) {
            ratesJob.cancel()
        }
        ratesJob = viewModelScope.launch {
            while (isActive) {
                ratesRepository.getCurrencyRates(currency).let { result ->
                    when (result) {
                        is Result.Success ->
                            Timber.v("${result.data.baseCurrency}: ${result.data.rates?.toList()?.joinToString()}")
                        is Result.Error ->
                            Timber.e("Currency rates loading error")
                    }
                }
                delay(1_000)
            }
        }
    }
}