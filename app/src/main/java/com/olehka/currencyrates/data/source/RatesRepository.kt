package com.olehka.currencyrates.data.source

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    fun getRates(currency: String) {
        remoteDataSource.getRates(currency)
    }
}