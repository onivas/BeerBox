package com.savinoordine.sp.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ToastMessenger
@Inject
constructor(@ActivityContext private val activityContext: Context) {

    fun showMessage(@StringRes stringResId: Int) {
        Toast.makeText(
            activityContext,
            stringResId,
            Toast.LENGTH_SHORT
        ).show()
    }
}
