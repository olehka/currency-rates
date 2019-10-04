package com.olehka.currencyrates.data

import com.olehka.currencyrates.data.source.DataSource
import com.olehka.currencyrates.di.ApplicationModule.LocalSource
import com.olehka.currencyrates.di.ApplicationModule.RemoteSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesRepository @Inject constructor(
    @RemoteSource private val remoteDataSource: DataSource,
    @LocalSource private val localDataSource: DataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    private val cachedRates: MutableList<CurrencyRate> = mutableListOf()

    override suspend fun getCurrencyRates(
        baseCurrency: String,
        fromNetwork: Boolean
    ): Result<List<CurrencyRate>> {
        return withContext(ioDispatcher) {
            if (fromNetwork) {
                when (val result = remoteDataSource.getCurrencyRates(baseCurrency)) {
                    is Result.Success -> {
                        localDataSource.saveCurrencyRates(result.data)
                        updateCache(result.data)
                        result
                    }
                    is Result.Error -> {
                        result
                    }
                }
            } else {
                if (cachedRates.isNotEmpty()) {
                    Result.Success(cachedRates)
                } else {
                    val result = localDataSource.getCurrencyRates(baseCurrency)
                    if (result is Result.Success) {
                        updateCache(result.data)
                    }
                    result
                }
            }
        }
    }

    private fun updateCache(rates: List<CurrencyRate>) {
        cachedRates.clear()
        cachedRates.addAll(rates)
    }
}