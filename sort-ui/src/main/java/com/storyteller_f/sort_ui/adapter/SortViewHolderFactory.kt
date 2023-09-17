package com.storyteller_f.sort_ui.adapter

import android.view.ViewGroup

abstract class SortViewHolderFactory<T> {
    abstract fun create(
        viewType: Int,
        container: SortItemContainer
    ): SortItemViewHolder<T>

    companion object {
        fun getContainer(parent: ViewGroup): SortItemContainer {
            return SortItemContainer(parent)
        }
    }
}