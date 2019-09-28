package com.olehka.currencyrates.util

import android.content.Context
import com.olehka.currencyrates.api.RatesResponse
import com.olehka.currencyrates.data.CurrencyRate
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

fun RatesResponse.toListOfCurrencyRates(): List<CurrencyRate> {
    if (baseCurrency == null || rates == null) {
        return emptyList()
    }
    val list = ArrayList<CurrencyRate>()
    list.add(
        CurrencyRate(
            code = baseCurrency,
            name = getCurrencyName(baseCurrency),
            value = 1f
        )
    )
    rates.entries.map { entry ->
        CurrencyRate(
            code = entry.key,
            name = getCurrencyName(entry.key),
            value = entry.value
        )
    }.also {
        list.addAll(it)
    }
    return list
}

fun List<CurrencyRate>.mapValues(baseValue: Float) = map { CurrencyRate(it.code, it.name, baseValue * it.value) }

fun getCurrencyFlagResId(context: Context, code: String) = context.resources.getIdentifier(
    "ic_${code}_flag", "mipmap", context.packageName
)

private fun getCurrencyName(currencyCode: String): String {
    return try {
        Currency.getInstance(currencyCode).displayName
    } catch (e: Exception) {
        ""
    }
}