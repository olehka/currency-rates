package com.olehka.currencyrates.data.source

import retrofit2.Response
import com.olehka.currencyrates.data.Result
import timber.log.Timber
import java.lang.Exception

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.Success(body)
            }
            val errorStr = "Network call has failed: code = ${response.code()}, message = ${response.message()}"
            Timber.e(errorStr)
            return Result.Error(Exception(errorStr))
        } catch (e: Exception) {
            Timber.e(e)
            return Result.Error(e)
        }
    }
}