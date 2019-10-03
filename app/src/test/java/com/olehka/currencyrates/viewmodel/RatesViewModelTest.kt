package com.olehka.currencyrates.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.olehka.currencyrates.MainCoroutineRule
import com.olehka.currencyrates.R
import com.olehka.currencyrates.data.FakeRepository
import com.olehka.currencyrates.util.getRatesBaseEur_1
import com.olehka.currencyrates.util.getRatesBaseZar_1
import com.olehka.currencyrates.ui.viewmodel.RatesViewModel
import com.olehka.currencyrates.util.LiveDataTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RatesViewModelTest {

    private lateinit var ratesViewModel: RatesViewModel

    private lateinit var ratesRepository: FakeRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        ratesRepository = FakeRepository()
        ratesRepository.addCurrencyRates("EUR", getRatesBaseEur_1())
        ratesRepository.addCurrencyRates("ZAR", getRatesBaseZar_1())
        ratesViewModel = RatesViewModel(ratesRepository)
    }

    @Test
    fun getCurrencyRates_fromLocalCache() {
        ratesViewModel.updateCurrencyRatesFromCache()
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)).hasSize(3)
    }

    @Test
    fun getCurrencyRates_startPeriodicUpdate() {
        ratesViewModel.isConnected = true
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)).hasSize(3)
    }

    @Test
    fun getCurrencyRates_whenError() {
        ratesRepository.shouldReturnError = true
        ratesViewModel.updateCurrencyRatesFromCache()
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)).isEmpty()
        ratesViewModel.isConnected = true
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)).isEmpty()
    }

    @Test
    fun getCurrencyRates_updateBaseValue() {
        ratesViewModel.onBaseValueChanged(200f)
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)[0].value).isEqualTo(200f)
    }

    @Test
    fun getCurrencyRates_updateBaseCurrency() {
        ratesViewModel.isConnected = true
        ratesViewModel.onBaseCurrencyValueChanged("ZAR", 100f)
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)[0].code).isEqualTo("ZAR")
    }

    @Test
    fun getCurrencyRates_wrongBaseCurrency() {
        ratesViewModel.onBaseCurrencyValueChanged("UAH", 100f)
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.rateList)).isEmpty()
    }

    @Test
    fun testSnackbar_showErrorMessage() {
        ratesRepository.shouldReturnError = true
        ratesViewModel.updateCurrencyRatesFromCache()
        assertThat(LiveDataTestUtil.getValue(ratesViewModel.shackbarMessage)
            .getContentIfNotHandled())
            .isEqualTo(R.string.currency_rates_loading_error)
    }
}