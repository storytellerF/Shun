package com.storyteller_f.filter_ui.adapter

import android.view.ViewGroup

abstract class FilterViewHolderFactory<T> {
    abstract fun create(
        viewType: Int,
        container: FilterItemContainer
    ): FilterItemViewHolder<T>

    companion object {
        fun getContainer(parent: ViewGroup): FilterItemContainer {
            return FilterItemContainer(parent)
        }
    }
}