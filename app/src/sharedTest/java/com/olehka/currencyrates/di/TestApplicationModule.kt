package com.olehka.currencyrates.di

import com.olehka.currencyrates.data.FakeRepository
import com.olehka.currencyrates.data.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestApplicationModule {

    @Singleton
    @Provides
    fun provideRepository(): Repository = FakeRepository()
}