package com.olehka.currencyrates.di

import android.content.Context
import androidx.room.Room
import com.olehka.currencyrates.api.RatesService
import com.olehka.currencyrates.data.RatesRepository
import com.olehka.currencyrates.data.Repository
import com.olehka.currencyrates.data.source.DataSource
import com.olehka.currencyrates.data.source.LocalDataSource
import com.olehka.currencyrates.data.source.RatesDatabase
import com.olehka.currencyrates.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
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
    fun provideLocalDataSource(
        database: RatesDatabase,
        ioDispatcher: CoroutineDispatcher
    ): DataSource {
        return LocalDataSource(database.ratesDao(), ioDispatcher)
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

    @Singleton
    @Provides
    fun provideRatesRepository(
        @RemoteSource remoteDataSource: DataSource,
        @LocalSource localDataSource: DataSource
    ): Repository {
        return RatesRepository(remoteDataSource, localDataSource)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDataBase(context: Context): RatesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RatesDatabase::class.java,
            "Rates.db"
        ).build()
    }
}