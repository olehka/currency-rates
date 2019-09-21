package com.olehka.currencyrates.di

import androidx.lifecycle.ViewModel
import com.olehka.currencyrates.ui.RatesFragment
import com.olehka.currencyrates.ui.viewmodel.RatesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RatesModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun ratesFragment(): RatesFragment

    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindViewModel(viewModel: RatesViewModel): ViewModel
}