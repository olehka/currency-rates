package com.olehka.currencyrates.data

interface Repository {

    suspend fun getCurrencyRates(baseCurrency: String, fromNetwork: Boolean = false):
            Result<List<CurrencyRate>>
}