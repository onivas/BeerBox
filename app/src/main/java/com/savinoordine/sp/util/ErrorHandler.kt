package com.savinoordine.sp.util

import com.savinoordine.sp.R
import com.savinoordine.sp.util.network.BadRequest
import com.savinoordine.sp.util.network.ErrorType
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ErrorHandler
@Inject
constructor(private val messenger: ToastMessenger) {

    fun handleError(errorType: ErrorType) = with(messenger) {
        when (errorType) {
            is BadRequest -> showMessage(R.string.bad_request_message)
            else -> showMessage(R.string.generic_error_message)
        }
    }
}
