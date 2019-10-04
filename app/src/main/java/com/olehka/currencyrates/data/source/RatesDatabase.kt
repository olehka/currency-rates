package com.olehka.currencyrates.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.olehka.currencyrates.data.CurrencyRate

@Database(entities = [CurrencyRate::class], version = 1, exportSchema = false)
abstract class RatesDatabase : RoomDatabase() {

    abstract fun ratesDao(): RatesDao
}