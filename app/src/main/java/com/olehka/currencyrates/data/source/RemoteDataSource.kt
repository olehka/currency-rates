package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.api.RatesService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: RatesService
): BaseDataSource() {

    suspend fun getCurrencyRates(currency: String) = getResult { service.getCurrencyRates(currency) }
}