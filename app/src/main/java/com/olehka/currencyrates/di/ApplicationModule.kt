package com.olehka.currencyrates.di

import com.olehka.currencyrates.api.RatesService
import com.olehka.currencyrates.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: RatesService): RemoteDataSource {
        return RemoteDataSource(service)
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