package com.olehka.currencyrates.util

import android.content.Context
import com.olehka.currencyrates.api.RatesResponse
import com.olehka.currencyrates.data.CurrencyRate
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

fun provideCurrencyRateList(response: RatesResponse, baseCurrencyValue: Float): List<CurrencyRate> {
    val list = ArrayList<CurrencyRate>()
    if (response.baseCurrency == null || response.rates == null) {
        return list
    }
    list.add(
        CurrencyRate(
            code = response.baseCurrency,
            name = getCurrencyName(response.baseCurrency),
            value = baseCurrencyValue
        )
    )
    response.rates.entries.map { entry ->
        CurrencyRate(
            code = entry.key,
            name = getCurrencyName(entry.key),
            value = entry.value * baseCurrencyValue
        )
    }.also {
        list.addAll(it)
    }
    return list
}

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