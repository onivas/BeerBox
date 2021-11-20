package com.savinoordine.sp.util.network

sealed class ResultOf<out T> {
    data class Success<T>(val value: T) : ResultOf<T>()
    data class Error(val type: ErrorType) : ResultOf<Nothing>()
}

abstract class ErrorType
object GenericError : ErrorType()
object NetworkUnavailable : ErrorType()
object Unauthorized : ErrorType()
object Forbidden : ErrorType()
object NotFound : ErrorType()
data class BadRequest(val code: String, val message: String) : ErrorType()

fun <T> T.success(): ResultOf<T> = ResultOf.Success(this)

fun ErrorType.error(): ResultOf<Nothing> = ResultOf.Error(this)

inline fun <T, R> ResultOf<T>.map(f: (T) -> R): ResultOf<R> = flatMap { f(it).success() }

inline fun <T, R> ResultOf<T>.flatMap(f: (T) -> ResultOf<R>): ResultOf<R> =
    when (this) {
        is ResultOf.Success -> f(this.value)
        is ResultOf.Error -> this
    }

inline fun <T, R> ResultOf<T>.fold(success: (T) -> R, error: (ErrorType) -> R): R =
    when (this) {
        is ResultOf.Success -> success(this.value)
        is ResultOf.Error -> error(this.type)
    }

inline fun <T> ResultOf<T>.onError(f: (ErrorType) -> Unit): ResultOf<T> = when (this) {
    is ResultOf.Success -> this
    is ResultOf.Error -> {
        f(type)
        this
    }
}

inline fun <T> ResultOf<T>.onSuccess(f: (T) -> Unit): ResultOf<T> = when (this) {
    is ResultOf.Success -> {
        f(value)
        this
    }
    is ResultOf.Error -> this
}
