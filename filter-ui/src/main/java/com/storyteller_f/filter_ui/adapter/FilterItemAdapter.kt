package com.storyteller_f.filter_ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.filter_core.Filter

class FilterItemAdapter<T>(
    private val filterChains: MutableList<Filter<T>>,
    private val filterViewHolderFactory: FilterViewHolderFactory<T>
) : RecyclerView.Adapter<FilterItemViewHolder<T>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterItemViewHolder<T> {
        return filterViewHolderFactory.create(
            viewType,
            FilterViewHolderFactory.getContainer(parent)
        )
    }

    override fun onBindViewHolder(holder: FilterItemViewHolder<T>, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.refresh = object : FilterItemChanged<T> {
            override fun onChanged(v: View, filter: Filter<T>) {
                filterChains[adapterPosition] = filter
            }
        }
        holder.bind(filterChains[position])
    }

    override fun getItemViewType(position: Int): Int {
        return filterChains[position].itemViewType
    }

    override fun getItemCount(): Int {
        return filterChains.size
    }

    @FunctionalInterface
    interface FilterItemChanged<T> {
        fun onChanged(v: View, filter: Filter<T>)
    }
}