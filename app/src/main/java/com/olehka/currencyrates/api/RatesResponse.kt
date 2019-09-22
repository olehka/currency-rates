package com.olehka.currencyrates.api

import com.google.gson.annotations.SerializedName

data class RatesResponse(

    @SerializedName("base")
    val baseCurrency: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("rates")
    val rates: Map<String, Float>? = null
)