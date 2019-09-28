package com.olehka.currencyrates.data

import com.olehka.currencyrates.data.source.DataSource
import com.olehka.currencyrates.di.ApplicationModule.LocalSource
import com.olehka.currencyrates.di.ApplicationModule.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository @Inject constructor(
    @RemoteSource private val remoteDataSource: DataSource,
    @LocalSource private val localDataSource: DataSource
) : Repository {

    override suspend fun getCurrencyRates(
        baseCurrency: String,
        fromNetwork: Boolean
    ): Result<List<CurrencyRate>> {
        return if (fromNetwork) {
            withContext(Dispatchers.IO) {
                when (val result = remoteDataSource.getCurrencyRates(baseCurrency)) {
                    is Result.Success -> {
                        localDataSource.saveCurrencyRates(result.data)
                        result
                    }
                    is Result.Error -> {
                        result
                    }
                }
            }
        } else {
            localDataSource.getCurrencyRates(baseCurrency)
        }
    }
}