package com.savinoordine.sp.util.network

import retrofit2.Call
import retrofit2.CallAdapter

class SpCallAdapter<T>(
    private val clazz: Class<T>,
) : CallAdapter<T, Any> {

    override fun responseType() = clazz

    override fun adapt(call: Call<T>): Call<ResultOf<T>> = SpCall(call)
}
