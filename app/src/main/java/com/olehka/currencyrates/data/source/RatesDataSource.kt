package com.olehka.currencyrates.data.source

interface RatesDataSource {

    fun getRates(currency: String)
}