package com.olehka.currencyrates.data.source

interface RatesRepository {

    fun getRates(currency: String)
}