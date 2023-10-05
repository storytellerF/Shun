package com.storyteller_f.filter_ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.filter_core.Filter

abstract class FilterItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var refresh: ((Filter<T>) -> Unit)? = null
    abstract fun bind(filterChain: Filter<T>)
}