package com.storyteller_f.sort_ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.common_dialog.RenameDialog
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem
import com.storyteller_f.sort_ui.R

abstract class SortItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var refresh: ((SortChain<T>) -> Unit)? = null
    abstract fun bind(sortChain: SortChain<T>)
    abstract class Simple<T>(itemView: View) : SortItemViewHolder<T>(itemView) {
        var name: TextView
        private var up: ImageView
        private var down: ImageView
        private val editButton: ImageButton

        init {
            name = itemView.findViewById(R.id.name)
            up = itemView.findViewById(R.id.sort_asc_button)
            down = itemView.findViewById(R.id.sort_desc_button)
            editButton = itemView.findViewById(R.id.edit_name)
        }

        private fun tintList(b: Boolean): ColorStateList {
            val colorStateList = if (b) {
                ColorStateList.valueOf(Color.BLACK)
            } else ColorStateList.valueOf(Color.GRAY)
            return colorStateList
        }

        override fun bind(sortChain: SortChain<T>) {
            val showName: String = sortChain.item.name ?: sortChain.showName
            name.text = showName
            editButton.setOnClickListener {
                RenameDialog(it.context, showName) { newName ->
                    @Suppress("UNCHECKED_CAST") val dup = sortChain.dup() as SortChain<T>
                    dup.item.name = newName
                    refresh?.invoke(dup)
                }.show()
            }
            val sortDirection = sortChain.item.sortDirection
            val b = sortDirection == SortConfigItem.UP
            ImageViewCompat.setImageTintList(up, tintList(b))
            ImageViewCompat.setImageTintList(down, tintList(sortDirection == SortConfigItem.DOWN))
            up.setOnClickListener {
                if (refresh != null) {
                    @Suppress("UNCHECKED_CAST") val copy = sortChain.dup() as SortChain<T>
                    copy.item.sortDirection = SortConfigItem.UP
                    refresh!!.invoke(copy)
                }
            }
            down.setOnClickListener {
                sortChain.item.sortDirection = SortConfigItem.DOWN
                @Suppress("UNCHECKED_CAST") val copy = sortChain.dup() as SortChain<T>
                copy.item.sortDirection = SortConfigItem.DOWN
                if (refresh != null) refresh!!.invoke(copy)
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

}