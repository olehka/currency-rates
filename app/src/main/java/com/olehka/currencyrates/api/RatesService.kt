package com.olehka.currencyrates.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesService {

    companion object {
        const val BASE_URL = "https://revolut.duckdns.org/"
    }

    @GET("latest")
    suspend fun getCurrencyRates(@Query("base") baseCurrency: String? = null): Response<RatesResponse>
}