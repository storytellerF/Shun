package com.storyteller_f.filter_ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_ui.adapter.FilterItemAdapter.FilterItemChanged

abstract class FilterItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var refresh: FilterItemChanged<T>? = null
    abstract fun bind(filterChain: Filter<T>)
}