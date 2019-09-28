package com.olehka.currencyrates.data.source

import com.olehka.currencyrates.api.RatesResponse
import com.olehka.currencyrates.api.RatesService
import com.olehka.currencyrates.data.CurrencyRate
import com.olehka.currencyrates.data.Result
import com.olehka.currencyrates.util.toListOfCurrencyRates
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: RatesService
) : DataSource {
    override suspend fun saveCurrencyRates(rates: List<CurrencyRate>) {}

    override suspend fun getCurrencyRates(baseCurrency: String): Result<List<CurrencyRate>> {
        return when (val result = getResult { service.getCurrencyRates(baseCurrency) }) {
            is Result.Success -> {
                getSuccessResult(result.data)
            }
            is Result.Error -> {
                result
            }
        }
    }

    private suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.Success(body)
            }
            val errorStr =
                "Network call has failed: code = ${response.code()}, message = ${response.message()}"
            Timber.e(errorStr)
            return Result.Error(Exception(errorStr))
        } catch (e: Exception) {
            Timber.e(e)
            return Result.Error(e)
        }
    }

    private fun getSuccessResult(response: RatesResponse):
            Result.Success<List<CurrencyRate>> {
        return response.let {
            Timber.v("${it.baseCurrency}: ${it.rates?.toList()?.joinToString()}")
            Result.Success(it.toListOfCurrencyRates())
        }
    }
}