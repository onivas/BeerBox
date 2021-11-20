package com.savinoordine.sp.util.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<out Any, Any>? = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                ResultOf::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    SpCallAdapter(getRawType(resultType))
                }
                else -> null
            }
        }
        else -> null
    }

    companion object {
        fun create() = CallAdapterFactory()
    }
}
