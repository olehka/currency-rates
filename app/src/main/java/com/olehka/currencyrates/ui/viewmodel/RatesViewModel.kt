package com.olehka.currencyrates.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.olehka.currencyrates.data.RatesRepository
import javax.inject.Inject

class RatesViewModel
@Inject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    fun loadRates() {
        ratesRepository.getRates("EUR")
    }
}