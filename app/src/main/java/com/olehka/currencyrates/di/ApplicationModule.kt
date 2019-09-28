package com.olehka.currencyrates.di

import com.olehka.currencyrates.api.RatesService
import com.olehka.currencyrates.data.source.DataSource
import com.olehka.currencyrates.data.source.LocalDataSource
import com.olehka.currencyrates.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalSource

    @Singleton
    @RemoteSource
    @Provides
    fun provideRemoteDataSource(service: RatesService): DataSource {
        return RemoteDataSource(service)
    }

    @Singleton
    @LocalSource
    @Provides
    fun provideLocalDataSource(): DataSource {
        return LocalDataSource()
    }

    @Singleton
    @Provides
    fun provideRatesService(): RatesService {
        return Retrofit.Builder()
            .baseUrl(RatesService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RatesService::class.java)
    }
}