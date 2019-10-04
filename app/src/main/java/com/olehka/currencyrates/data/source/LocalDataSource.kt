package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource internal constructor(
    private val ratesDao: RatesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override suspend fun saveCurrencyRates(rates: List<CurrencyRate>) {
        withContext(ioDispatcher) {
            ratesDao.deleteCurrencyRates()
            ratesDao.saveCurrencyRates(rates)
        }
    }

    override suspend fun getCurrencyRates(baseCurrency: String): Result<List<CurrencyRate>> {
        return withContext(ioDispatcher) {
            try {
                val rates = ratesDao.getCurrencyRates()
                if (!rates.isNullOrEmpty() && rates[0].code == baseCurrency) {
                    Result.Success(rates)
                } else {
                    Result.Error(Exception("loading error"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}