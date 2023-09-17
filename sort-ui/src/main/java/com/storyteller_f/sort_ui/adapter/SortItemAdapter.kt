package com.storyteller_f.sort_ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_ui.adapter.SortItemViewHolder.SortItemChanged

class SortItemAdapter<T>(
    private val sortChains: MutableList<SortChain<T>>,
    private val sortViewHolderFactory: SortViewHolderFactory<T>
) : RecyclerView.Adapter<SortItemViewHolder<T>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortItemViewHolder<T> {
        return sortViewHolderFactory.create(
            viewType,
            SortViewHolderFactory.getContainer(parent)
        )
    }

    override fun onBindViewHolder(holder: SortItemViewHolder<T>, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.refresh = object : SortItemChanged {
            override fun onChanged(v: View, sortChain: SortChain<*>) {
                sortChains[adapterPosition] = sortChain as SortChain<T>
                this@SortItemAdapter.notifyItemChanged(adapterPosition)
            }
        }
        holder.bind(sortChains[position])
    }

    override fun getItemViewType(position: Int): Int {
        return sortChains[position].itemViewType
    }

    override fun getItemCount(): Int {
        return sortChains.size
    }
}