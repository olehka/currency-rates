package com.olehka.currencyrates.ui.viewmodel

import androidx.lifecycle.*
import com.olehka.currencyrates.data.CurrencyRate
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

    private val mutableRateList =
        MutableLiveData<List<CurrencyRate>>().apply { value = emptyList() }
    val rateList: LiveData<List<CurrencyRate>> = mutableRateList

    val empty: LiveData<Boolean> = Transformations.map(mutableRateList) { it.isEmpty() }

    fun loadCurrencyRates(currency: String) {
        if (::ratesJob.isInitialized && ratesJob.isActive) {
            ratesJob.cancel()
        }
        ratesJob = viewModelScope.launch {
            while (isActive) {
                ratesRepository.getCurrencyRates(currency).let { result ->
                    when (result) {
                        is Result.Success -> {
                            Timber.v("${result.data.baseCurrency}: ${result.data.rates?.toList()?.joinToString()}")
                            mutableRateList.value = result.data.rates?.entries?.map { entry ->
                                CurrencyRate(
                                    currency = entry.key,
                                    rateValue = entry.value
                                )
                            }
                        }
                        is Result.Error -> {
                            Timber.e("Currency rates loading error")
                            mutableRateList.value = emptyList()
                        }
                    }
                }
                delay(1_000)
            }
        }
    }
}