package com.olehka.currencyrates.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olehka.currencyrates.data.RatesRepository
import com.olehka.currencyrates.data.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class RatesViewModel
@Inject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    fun loadRates(currency: String) {
        viewModelScope.launch {
            while (true) {
                delay(2_000)
                ratesRepository.getRates(currency).let { result ->
                    if (result is Result.Success) {
                        Timber.v("${result.data.baseCurrency}: ${result.data.rates?.toList()?.joinToString()}")
                    } else {
                        Timber.v("Data not available :|")
                    }
                }
            }
        }
    }
}