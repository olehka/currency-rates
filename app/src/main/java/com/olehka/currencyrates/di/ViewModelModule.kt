package com.olehka.currencyrates.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olehka.currencyrates.ui.viewmodel.RatesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindRatesViewModel(viewModel: RatesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: RatesViewModelFactory): ViewModelProvider.Factory
}