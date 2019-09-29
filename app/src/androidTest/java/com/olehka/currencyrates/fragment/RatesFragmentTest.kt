package com.olehka.currencyrates.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.olehka.currencyrates.DaggerTestApplicationRule
import com.olehka.currencyrates.R
import com.olehka.currencyrates.RecyclerViewChildActions.Companion.actionOnChild
import com.olehka.currencyrates.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import com.olehka.currencyrates.data.FakeRepository
import com.olehka.currencyrates.data.Repository
import com.olehka.currencyrates.ui.RatesFragment
import com.olehka.currencyrates.util.getRatesBaseEur_1
import com.olehka.currencyrates.util.getRatesBaseZar_1
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class RatesFragmentTest {

    private lateinit var repository: Repository

    @get:Rule
    val rule = DaggerTestApplicationRule()

    @Before
    fun setupComponents() {
        repository = rule.component.repository
    }

    @Test
    fun showEmptyList() {
        launchFragmentInContainer<RatesFragment>()
        onView(withId(R.id.empty_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.rates_list)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showListOfCurrencyRates() {
        (repository as FakeRepository)
            .addCurrencyRates(baseCurrency = "EUR", rates = getRatesBaseEur_1())
        launchFragmentInContainer<RatesFragment>()
        onView(withId(R.id.empty_tv)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rates_list)).check(matches(isDisplayed()))
        onView(withText("EUR")).check(matches(isDisplayed()))
        onView(withText("USD")).check(matches(isDisplayed()))
        onView(withText("ZAR")).check(matches(isDisplayed()))
    }

    @Test
    fun changeBaseCurrency() {
        (repository as FakeRepository).let {
            it.addCurrencyRates(baseCurrency = "EUR", rates = getRatesBaseEur_1())
            it.addCurrencyRates(baseCurrency = "ZAR", rates = getRatesBaseZar_1())
        }
        launchFragmentInContainer<RatesFragment>()
        onView(withId(R.id.rates_list))
            .perform(
                actionOnItemAtPosition<ViewHolder>(
                    2,
                    actionOnChild(
                        click(),
                        R.id.value
                    )
                )
            )
        onView(withId(R.id.rates_list))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    0,
                    withText("ZAR")
                )
            ))
        onView(withId(R.id.rates_list))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    1,
                    withText("USD")
                )
            ))
        onView(withId(R.id.rates_list))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.title,
                    2,
                    withText("EUR")
                )
            ))
    }

    @Test
    fun changeBaseValue() {
        (repository as FakeRepository)
            .addCurrencyRates(baseCurrency = "EUR", rates = getRatesBaseEur_1())
        launchFragmentInContainer<RatesFragment>()
        onView(withId(R.id.rates_list))
            .perform(
                actionOnItemAtPosition<ViewHolder>(
                    0,
                    actionOnChild(
                        replaceText("200"),
                        R.id.value
                    )
                )
            )
        onView(withId(R.id.rates_list))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.value,
                    0,
                    withText("200.00")
                )
            ))
        onView(withId(R.id.rates_list))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.value,
                    1,
                    withText("233.48")
                )
            ))
        onView(withId(R.id.rates_list))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.value,
                    2,
                    withText("3576.80")
                )
            ))
    }
}