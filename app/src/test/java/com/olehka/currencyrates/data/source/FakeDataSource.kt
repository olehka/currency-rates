package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.Result

class FakeDataSource(
    var currencyRates: MutableList<CurrencyRate> = mutableListOf()
) : DataSource {
    override suspend fun getCurrencyRates(baseCurrency: String): Result<List<CurrencyRate>> {
        return if (currencyRates.isNotEmpty() && baseCurrency == currencyRates[0].code) {
            Result.Success(currencyRates)
        } else {
            Result.Error(Exception("CurrencyRates loading error"))
        }
    }

    override suspend fun saveCurrencyRates(rates: List<CurrencyRate>) {
        currencyRates.clear()
        currencyRates.addAll(rates)
    }
}