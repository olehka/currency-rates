package com.olehka.currencyrates.di

import com.olehka.currencyrates.ui.RatesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeRatesFragment(): RatesFragment
}