package com.savinoordine.sp.util.recyclerview

import androidx.recyclerview.widget.DiffUtil

class StandardDiffUtil<T>(
    private val areItemsTheSameCheck: (T, T) -> Boolean = { oldItem: T, newItem: T ->
        oldItem == newItem
    }
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSameCheck(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSameCheck(oldItem, newItem)
}
