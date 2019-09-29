package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.Result
import java.lang.Exception

class LocalDataSource : DataSource {
    private val currencyRates: MutableList<CurrencyRate> = mutableListOf()

    override suspend fun saveCurrencyRates(rates: List<CurrencyRate>) {
        currencyRates.clear()
        currencyRates.addAll(rates)
    }

    override suspend fun getCurrencyRates(baseCurrency: String): Result<List<CurrencyRate>> {
        return if (currencyRates.isEmpty() || currencyRates[0].code != baseCurrency) {
            Result.Error(Exception("loading error"))
        } else {
            Result.Success(currencyRates)
        }
    }
}