package com.olehka.currencyrates.data

import com.google.common.truth.Truth.assertThat
import com.olehka.currencyrates.data.source.FakeDataSource
import com.olehka.currencyrates.util.getRatesBaseEur_1
import com.olehka.currencyrates.util.getRatesBaseEur_2
import com.olehka.currencyrates.util.getRatesBaseZar_2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RatesRepositoryTest {

    private val remoteRatesBaseEur = getRatesBaseEur_2()
    private val localRatesBaseEur = getRatesBaseEur_1()

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var ratesRepository: RatesRepository

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        remoteDataSource = FakeDataSource(remoteRatesBaseEur.toMutableList())
        localDataSource = FakeDataSource(localRatesBaseEur.toMutableList())
        ratesRepository = RatesRepository(remoteDataSource, localDataSource, Dispatchers.Unconfined)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrencyRates_beforeFirstNetworkCall() = runBlockingTest {
        val emptySource = FakeDataSource()
        val repository = RatesRepository(emptySource, emptySource)
        assertThat(repository.getCurrencyRates(baseCurrency = "EUR") is Result.Error).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrencyRates_fromLocalCache() = runBlockingTest {
        val result = ratesRepository.getCurrencyRates(baseCurrency = "EUR")
        assertThat(result is Result.Success).isTrue()
        val rates = (result as Result.Success).data
        assertThat(rates).isEqualTo(localDataSource.currencyRates)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrencyRates_fromNetwork() = runBlockingTest {
        val result = ratesRepository.getCurrencyRates(baseCurrency = "EUR", fromNetwork = true)
        assertThat(result is Result.Success).isTrue()
        val rates = (result as Result.Success).data
        assertThat(rates).isEqualTo(remoteDataSource.currencyRates)
        assertThat(rates).isEqualTo(localDataSource.currencyRates)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrencyRates_wrongBaseCurrency() = runBlockingTest {
        var result = ratesRepository.getCurrencyRates(baseCurrency = "EUR")
        assertThat(result is Result.Success).isTrue()
        result = ratesRepository.getCurrencyRates(baseCurrency = "USD")
        assertThat(result is Result.Error).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrencyRates_changeBaseCurrency() = runBlockingTest {
        val newRemoteDataSource = FakeDataSource(getRatesBaseZar_2().toMutableList())
        val repository = RatesRepository(newRemoteDataSource, localDataSource, Dispatchers.Unconfined)
        assertThat(repository.getCurrencyRates(baseCurrency = "EUR") is Result.Success).isTrue()
        val result = repository.getCurrencyRates(baseCurrency = "ZAR", fromNetwork = true)
        assertThat(result is Result.Success).isTrue()
        val rates = (result as Result.Success).data
        assertThat(rates[0].code).isEqualTo("ZAR")
    }
}