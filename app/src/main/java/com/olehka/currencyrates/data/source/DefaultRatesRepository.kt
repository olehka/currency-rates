package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.di.ApplicationModule
import javax.inject.Inject

class DefaultRatesRepository @Inject constructor(
    @ApplicationModule.RatesRemoteDataSource
    private val ratesRemoteDataSource: RatesDataSource
) : RatesRepository {

    override fun getRates(currency: String) {
        ratesRemoteDataSource.getRates(currency)
    }
}