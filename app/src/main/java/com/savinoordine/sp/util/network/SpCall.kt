package com.savinoordine.sp.util.network

import android.util.Log
import com.savinoordine.sp.util.error.ErrorModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpCall<T>(
    private val rawCall: Call<T>,
) :
    Call<ResultOf<T>> {
    override fun clone(): Call<ResultOf<T>> = SpCall(rawCall.clone())

    override fun execute(): Response<ResultOf<T>> =
        // I'm using suspend methods. On retrofit they are implemented wrapping the callback api
        throw NotImplementedError()

    override fun enqueue(callback: Callback<ResultOf<T>>) {
        rawCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(this@SpCall, Response.success(response.toResultOf()))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e("call", "${t.message}")
                val result = NetworkUnavailable.error()
                callback.onResponse(this@SpCall, Response.success(result))
            }
        })
    }

    override fun isExecuted() = rawCall.isExecuted

    override fun cancel() = rawCall.cancel()

    override fun isCanceled() = rawCall.isCanceled

    override fun request(): Request = rawCall.request()

    override fun timeout(): Timeout = rawCall.timeout()
}

private fun <T> Response<T>.toResultOf(): ResultOf<T> =
    if (isSuccessful) {
        try {
            body()!!.success()
        } catch (e: Exception) {
            GenericError.error()
        }
    } else {
        when (code()) {
            in 400 until 500 -> this.toClientError().error()
            else -> GenericError.error()
        }
    }

fun <T> Response<T>.toClientError(): ErrorType =
    when (code()) {
        400 -> mapClientError()
        401 -> Unauthorized
        403 -> Forbidden
        404 -> NotFound
        409 -> mapClientError()
        else -> GenericError
    }

fun <T> Response<T>.mapClientError(): ErrorType {
    val errorString = errorBody()?.string()
    if (errorString != null) {
        val moshi = Moshi.Builder().build()
        val errorAdapter: JsonAdapter<ErrorModel> = moshi.adapter(ErrorModel::class.java)
        val errorModel = runCatching { errorAdapter.fromJson(errorString) }.getOrNull()
        if (errorModel != null) {
            return BadRequest(
                code = errorModel.exceptionCode.orEmpty(),
                message = errorModel.message.orEmpty()
            )
        }
    }
    return GenericError
}
