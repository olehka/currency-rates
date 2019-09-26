package com.olehka.currencyrates.data

import com.olehka.currencyrates.api.RatesResponse
import com.olehka.currencyrates.data.source.RemoteDataSource
import com.olehka.currencyrates.util.provideCurrencyRateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    private lateinit var savedResponse: RatesResponse

    suspend fun getCurrencyRatesFromNetwork(
        currency: String,
        baseCurrencyValue: Float
    ): Result<List<CurrencyRate>> {
        return withContext(Dispatchers.IO) {
            when (val result = remoteDataSource.getCurrencyRates(currency)) {
                is Result.Success -> {
                    result.data.let {
                        Timber.v("${it.baseCurrency}: ${it.rates?.toList()?.joinToString()}")
                        savedResponse = it
                        Result.Success(provideCurrencyRateList(it, baseCurrencyValue))
                    }
                }
                is Result.Error -> {
                    val errorMessage = "Currency rates loading error"
                    Timber.e(errorMessage)
                    Result.Error(Exception(errorMessage))
                }
            }
        }
    }

    fun getSavedCurrencyRates(baseCurrencyValue: Float): List<CurrencyRate> {
        return if (::savedResponse.isInitialized) {
            provideCurrencyRateList(savedResponse, baseCurrencyValue)
        } else {
            emptyList()
        }
    }
}