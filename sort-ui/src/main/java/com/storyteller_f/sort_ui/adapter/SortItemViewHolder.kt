package com.storyteller_f.sort_ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem
import com.storyteller_f.sort_ui.R

abstract class SortItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var refresh: SortItemChanged? = null
    abstract fun bind(sortChain: SortChain<T>)
    abstract class Simple<T>(itemView: View) : SortItemViewHolder<T>(itemView) {
        var name: TextView
        private var up: ImageView
        private var down: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            up = itemView.findViewById(R.id.sort_asc_button)
            down = itemView.findViewById(R.id.sort_desc_button)
        }

        private fun tintList(b: Boolean): ColorStateList {
            val colorStateList = if (b) {
                ColorStateList.valueOf(Color.BLACK)
            } else ColorStateList.valueOf(Color.GRAY)
            return colorStateList
        }

        override fun bind(sortChain: SortChain<T>) {
            name.text = sortChain.showName
            val sortDirection = sortChain.item.sortDirection
            val b = sortDirection == SortConfigItem.up
            ImageViewCompat.setImageTintList(up, tintList(b))
            ImageViewCompat.setImageTintList(down, tintList(sortDirection == SortConfigItem.down))
            up.setOnClickListener { v: View ->
                if (refresh != null) {
                    val copy = sortChain.dup() as SortChain<*>
                    copy.item.sortDirection = SortConfigItem.up
                    refresh!!.onChanged(v, copy)
                }
            }
            down.setOnClickListener { v: View ->
                sortChain.item.sortDirection = SortConfigItem.down
                val copy = sortChain.dup() as SortChain<*>
                copy.item.sortDirection = SortConfigItem.down
                if (refresh != null) refresh!!.onChanged(v, copy)
            }
        }

        companion object {
            @JvmStatic
            fun create(container: SortItemContainer): View {
                val frameLayout = container.frameLayout
                LayoutInflater.from(frameLayout.context)
                    .inflate(R.layout.item_simple_sort, frameLayout, true)
                return container.view
            }
        }
    }

    @FunctionalInterface
    interface SortItemChanged {
        fun onChanged(v: View, sortChain: SortChain<*>)
    }
}