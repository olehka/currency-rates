package com.olehka.currencyrates.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CurrencyRates")
data class CurrencyRate(
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "value") val value: Float,
    @PrimaryKey @ColumnInfo(name = "entityId") var id: String =
        UUID.randomUUID().toString()
)