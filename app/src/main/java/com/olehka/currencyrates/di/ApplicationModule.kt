package com.olehka.currencyrates.di

import com.olehka.currencyrates.data.source.DefaultRatesRepository
import com.olehka.currencyrates.data.source.RatesDataSource
import com.olehka.currencyrates.data.source.RatesRepository
import com.olehka.currencyrates.data.source.RatesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RatesRemoteDataSource

    @JvmStatic
    @Singleton
    @RatesRemoteDataSource
    @Provides
    fun provideRatesRemoteDataSource(): RatesDataSource {
        return RatesRemoteDataSource
    }

}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultRatesRepository): RatesRepository
}