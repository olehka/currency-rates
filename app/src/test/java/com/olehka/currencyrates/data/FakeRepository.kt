package com.olehka.currencyrates.data

import androidx.annotation.VisibleForTesting
import java.lang.Exception

class FakeRepository : Repository {

    var shouldReturnError = false

    private val currencyRatesMap: HashMap<String, List<CurrencyRate>> = HashMap()

    override suspend fun getCurrencyRates(
        baseCurrency: String,
        fromNetwork: Boolean
    ): Result<List<CurrencyRate>> {
        return if (shouldReturnError || !currencyRatesMap.containsKey(baseCurrency)) {
            Result.Error(Exception("loading error"))
        } else {
            Result.Success(currencyRatesMap[baseCurrency]!!)
        }
    }

    @VisibleForTesting
    fun addCurrencyRates(baseCurrency: String, rates: List<CurrencyRate>) {
        currencyRatesMap[baseCurrency] = rates
    }
}