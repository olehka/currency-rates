package com.olehka.currencyrates.util

import com.olehka.currencyrates.api.RatesResponse
import com.olehka.currencyrates.data.CurrencyRate

fun provideCurrencyRateList(response: RatesResponse, baseCurrencyValue: Float): List<CurrencyRate> {
    val list = ArrayList<CurrencyRate>()
    if (response.baseCurrency == null || response.rates == null) {
        return list
    }
    list.add(CurrencyRate(currency = response.baseCurrency, value = baseCurrencyValue))
    response.rates.entries.map { entry ->
        CurrencyRate(currency = entry.key, value = entry.value * baseCurrencyValue)
    }.also {
        list.addAll(it)
    }
    return list
}