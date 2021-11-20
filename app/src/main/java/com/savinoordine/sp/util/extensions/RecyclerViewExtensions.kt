package com.savinoordine.sp.util.extensions

import androidx.recyclerview.widget.RecyclerView

inline fun <reified A : RecyclerView.Adapter<*>> RecyclerView.castAdapterTo(): A = this.adapter as A