package com.olehka.currencyrates.ui.viewmodel

import androidx.lifecycle.*
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.RatesRepository
import com.olehka.currencyrates.data.Result
import com.olehka.currencyrates.util.mapValues
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

    private lateinit var periodicJob: Job

    private var baseCurrency = EURO
    private var baseValue = DEFAULT_VALUE

    private val mutableRateList =
        MutableLiveData<List<CurrencyRate>>().apply { value = emptyList() }
    val rateList: LiveData<List<CurrencyRate>> = mutableRateList

    val empty: LiveData<Boolean> = Transformations.map(mutableRateList) { it.isEmpty() }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    fun startPeriodicCurrencyRatesUpdate() {
        cancelActiveJob()
        // alternative: liveData building block
        periodicJob = viewModelScope.launch(exceptionHandler) {
            while (isActive) {
                updateRateList(repository.getCurrencyRates(baseCurrency, fromNetwork = true))
                delay(DELAY_MILLIS)
            }
        }
    }

    fun getCurrencyRatesFromCache() {
        viewModelScope.launch(exceptionHandler) {
            updateRateList(repository.getCurrencyRates(baseCurrency))
        }
    }

    fun onBaseCurrencyValueChanged(currency: String, value: Float) {
        Timber.v("onBaseCurrencyValueChanged: $currency: $value")
        if (baseCurrency != currency || baseValue != value) {
            baseCurrency = currency
            baseValue = value
            startPeriodicCurrencyRatesUpdate()
        }
    }

    fun onBaseValueChanged(value: Float) {
        Timber.v("onBaseValueChanged: $value")
        if (baseValue != value) {
            baseValue = value
            getCurrencyRatesFromCache()
        }
    }

    fun cancelActiveJob() {
        if (::periodicJob.isInitialized && periodicJob.isActive) {
            periodicJob.cancel()
        }
    }

    private fun updateRateList(result: Result<List<CurrencyRate>>) {
        when (result) {
            is Result.Success -> {
                mutableRateList.value = result.data.mapValues(baseValue)
            }
            is Result.Error -> {
                mutableRateList.value = emptyList()
                // also: showSnackbar with error message
            }
        }
    }
}