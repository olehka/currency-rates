package com.olehka.currencyrates.data

import com.olehka.currencyrates.api.RatesResponse
import com.olehka.currencyrates.data.source.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getCurrencyRates(currency: String): Result<RatesResponse> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getCurrencyRates(currency)
        }
    }
}