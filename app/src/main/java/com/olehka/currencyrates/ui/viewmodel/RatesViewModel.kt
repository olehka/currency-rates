package com.olehka.currencyrates.ui.viewmodel

import androidx.lifecycle.*
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.RatesRepository
import com.olehka.currencyrates.data.Result
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

const val DELAY_MILLIS = 1_000L
const val DEFAULT_VALUE = 100f
const val EURO = "EUR"

class RatesViewModel
@Inject constructor(
    private val repository: RatesRepository
) : ViewModel() {

    private lateinit var ratesJob: Job

    private var baseCurrency = EURO
    private var baseValue = DEFAULT_VALUE

    private val mutableRateList =
        MutableLiveData<List<CurrencyRate>>().apply { value = emptyList() }
    val rateList: LiveData<List<CurrencyRate>> = mutableRateList

    val empty: LiveData<Boolean> = Transformations.map(mutableRateList) { it.isEmpty() }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    fun loadCurrencyRates() {
        cancelActiveJob()
        // alternative: liveData building block
        ratesJob = viewModelScope.launch(exceptionHandler) {
            while (isActive) {
                repository.getCurrencyRatesFromNetwork(baseCurrency, baseValue).let { result ->
                    when (result) {
                        is Result.Success -> {
                            mutableRateList.value = result.data
                        }
                        is Result.Error -> {
                            mutableRateList.value = emptyList()
                            // also: showSnackbar with error message
                        }
                    }
                }
                delay(DELAY_MILLIS)
            }
        }
    }

    fun onBaseCurrencyValueChanged(currency: String, value: Float) {
        Timber.v("onBaseCurrencyValueChanged: $currency: $value")
        if (baseCurrency != currency || baseValue != value) {
            baseCurrency = currency
            baseValue = value
            loadCurrencyRates()
        }
    }

    fun onBaseValueChanged(value: Float) {
        Timber.v("onBaseValueChanged: $value")
        if (baseValue != value) {
            baseValue = value
            mutableRateList.value = repository.getSavedCurrencyRates(baseValue)
        }
    }

    fun cancelActiveJob() {
        if (::ratesJob.isInitialized && ratesJob.isActive) {
            ratesJob.cancel()
        }
    }
}