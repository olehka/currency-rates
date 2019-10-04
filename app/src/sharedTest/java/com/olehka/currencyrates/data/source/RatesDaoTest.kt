package com.olehka.currencyrates.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.olehka.currencyrates.MainCoroutineRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.util.getRatesBaseZar_1
import com.olehka.currencyrates.util.getRatesBaseZar_2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RatesDaoTest {

    private lateinit var database: RatesDatabase

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RatesDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveAndGetCurrencyRates() = runBlockingTest {
        val rates = getRatesBaseZar_1()
        database.ratesDao().saveCurrencyRates(rates)
        val loaded = database.ratesDao().getCurrencyRates()
        assertThat(loaded.size).isEqualTo(3)
        assertThat(loaded[0].code).isEqualTo("ZAR")
        assertThat(loaded[1].code).isEqualTo("USD")
        assertThat(loaded[2].code).isEqualTo("EUR")
    }

    @Test
    fun saveAndDeleteCurrencyRates() = runBlockingTest {
        val rates = getRatesBaseZar_2()
        database.ratesDao().saveCurrencyRates(rates)
        var loaded = database.ratesDao().getCurrencyRates()
        assertThat(loaded).isNotNull()
        assertThat(loaded).isNotEmpty()
        database.ratesDao().deleteCurrencyRates()
        loaded = database.ratesDao().getCurrencyRates()
        assertThat(loaded).isEmpty()
    }

    @Test
    fun saveAndReplaceCurrencyRatesOnConflict() = runBlockingTest {
        val rates = ArrayList<CurrencyRate>()
        val rate1 = CurrencyRate(code = "EUR", name = "Euro", value = 1.0f)
        rates.add(rate1)
        database.ratesDao().saveCurrencyRates(rates)
        var loaded = database.ratesDao().getCurrencyRates()
        assertThat(loaded.size).isEqualTo(1)
        assertThat(loaded[0].value).isEqualTo(1.0f)
        val rate2 = CurrencyRate(code = rate1.code, name = rate1.name, value = 2.0f, id = rate1.id)
        rates.clear()
        rates.add(rate2)
        database.ratesDao().saveCurrencyRates(rates)
        loaded = database.ratesDao().getCurrencyRates()
        assertThat(loaded.size).isEqualTo(1)
        assertThat(loaded[0].value).isEqualTo(2.0f)
    }
}