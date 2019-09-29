package com.olehka.currencyrates.di

import android.content.Context
import com.olehka.currencyrates.RatesApplication
import com.olehka.currencyrates.data.Repository
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestApplicationModule::class,
        ViewModelModule::class,
        FragmentModule::class
    ]
)
interface TestApplicationComponent : AndroidInjector<RatesApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestApplicationComponent
    }

    val repository: Repository
}