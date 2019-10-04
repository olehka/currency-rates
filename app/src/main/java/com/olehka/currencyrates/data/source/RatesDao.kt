package com.olehka.currencyrates.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olehka.currencyrates.data.CurrencyRate

@Dao
interface RatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrencyRates(rates: List<CurrencyRate>)

    @Query("SELECT * FROM CurrencyRates")
    suspend fun getCurrencyRates(): List<CurrencyRate>

    @Query("DELETE FROM CurrencyRates")
    suspend fun deleteCurrencyRates()
}