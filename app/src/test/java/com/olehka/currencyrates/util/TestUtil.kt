package com.olehka.currencyrates.util

import com.olehka.currencyrates.data.CurrencyRate

fun getRatesBaseEur_1(): List<CurrencyRate> {
    val rates = ArrayList<CurrencyRate>()
    rates.add(CurrencyRate(code = "EUR", name = "Euro", value = 1.0f))
    rates.add(CurrencyRate(code = "USD", name = "US Dollar", value = 1.1674f))
    rates.add(CurrencyRate(code = "ZAR", name = "South African Rand", value = 17.884f))
    return rates
}

fun getRatesBaseEur_2(): List<CurrencyRate> {
    val rates = ArrayList<CurrencyRate>()
    rates.add(CurrencyRate(code = "EUR", name = "Euro", value = 1.0f))
    rates.add(CurrencyRate(code = "USD", name = "US Dollar", value = 1.1583f))
    rates.add(CurrencyRate(code = "ZAR", name = "South African Rand", value = 17.745f))
    return rates
}

fun getRatesBaseZar_1(): List<CurrencyRate> {
    val rates = ArrayList<CurrencyRate>()
    rates.add(CurrencyRate(code = "ZAR", name = "South African Rand", value = 1.0f))
    rates.add(CurrencyRate(code = "USD", name = "US Dollar", value = 0.065302f))
    rates.add(CurrencyRate(code = "EUR", name = "Euro", value = 0.056131f))
    return rates
}

fun getRatesBaseZar_2(): List<CurrencyRate> {
    val rates = ArrayList<CurrencyRate>()
    rates.add(CurrencyRate(code = "ZAR", name = "South African Rand", value = 1.0f))
    rates.add(CurrencyRate(code = "USD", name = "US Dollar", value = 0.065533f))
    rates.add(CurrencyRate(code = "EUR", name = "Euro", value = 0.056331f))
    return rates
}