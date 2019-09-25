package com.olehka.currencyrates.data

import java.util.*

data class CurrencyRate(
    val id: String = UUID.randomUUID().toString(),
    val currency: String,
    val rateValue: Float
)