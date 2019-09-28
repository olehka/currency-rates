package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.Result

interface DataSource {

    suspend fun getCurrencyRates(baseCurrency: String): Result<List<CurrencyRate>>

    suspend fun saveCurrencyRates(rates: List<CurrencyRate>)
}