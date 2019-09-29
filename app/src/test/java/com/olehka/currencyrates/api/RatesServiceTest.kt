package com.olehka.currencyrates.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RatesServiceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: RatesService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RatesService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun requestCurrencyRates() = runBlocking {
        enqueueResponse("currency_rates.json")
        val resultResponse = service.getCurrencyRates(baseCurrency = "EUR").body()
        val request = mockWebServer.takeRequest()
        assertThat(resultResponse).isNotNull()
        assertThat(request.path).isEqualTo("/latest?base=EUR")
    }

    @Test
    fun getCurrencyRatesResponse() = runBlocking {
        enqueueResponse("currency_rates.json")
        val resultResponse = service.getCurrencyRates(baseCurrency = "EUR").body()
        val rates = resultResponse?.rates
        assertThat(resultResponse?.baseCurrency).isEqualTo("EUR")
        assertThat(resultResponse?.date).isEqualTo("2018-09-06")
        assertThat(rates?.size).isEqualTo(32)
    }

    @Test
    fun getFirstCurrencyRateItem() = runBlocking {
        enqueueResponse("currency_rates.json")
        val resultResponse = service.getCurrencyRates(baseCurrency = "EUR").body()
        val rates = resultResponse?.rates?.toList()
        val rate = rates?.get(0)
        assertThat(rate?.first).isEqualTo("AUD")
        assertThat(rate?.second).isEqualTo(1.6147f)
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(source.readString(Charsets.UTF_8))
        )
    }
}