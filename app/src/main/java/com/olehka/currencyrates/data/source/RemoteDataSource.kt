package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.api.RatesService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: RatesService
) {

    suspend fun getRates(currency: String) = service.getRates(currency)
}