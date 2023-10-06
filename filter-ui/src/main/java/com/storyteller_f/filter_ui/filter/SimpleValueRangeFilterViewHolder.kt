package com.storyteller_f.filter_ui.filter

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.storyteller_f.common_dialog.RenameDialog
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.filter.simple.SimpleValueRangeFilter
import com.storyteller_f.filter_ui.R
import com.storyteller_f.filter_ui.adapter.FilterItemContainer
import com.storyteller_f.filter_ui.adapter.FilterItemViewHolder

class SimpleValueRangeFilterViewHolder<T>(itemView: View) : FilterItemViewHolder<T>(itemView) {
    val name: TextView
    private val min: EditText
    private val max: EditText
    private val editButton: ImageButton

    init {
        name = itemView.findViewById(R.id.value_range_label)
        min = itemView.findViewById(R.id.value_range_min)
        max = itemView.findViewById(R.id.value_range_max)
        editButton = itemView.findViewById(R.id.edit_name)
    }

    override fun bind(filterChain: Filter<T>) {
        if (filterChain is SimpleValueRangeFilter<T>) {
            editButton.setOnClickListener {
                RenameDialog(it.context, filterChain.currentName) { newName ->
                    @Suppress("UNCHECKED_CAST") val dup =
                        filterChain.dup() as SimpleValueRangeFilter<T>
                    dup.item.name = newName
                    refresh?.invoke(dup)
                }.show()
            }
            name.text = filterChain.currentName
            @Suppress("UNCHECKED_CAST") val duplicated =
                filterChain.dup() as SimpleValueRangeFilter<T>
            min.setText(String.format("%s", duplicated.minValue))
            max.setText(String.format("%s", duplicated.maxValue))
            min.setOnKeyListener { _: View, _: Int, _: KeyEvent? ->
                val trim = min.text.toString().trim { it <= ' ' }
                if (trim.isEmpty()) {
                    duplicated.item.hasMinValue = false
                } else {
                    duplicated.item.hasMinValue = true
                    duplicated.item.minValue = trim.toDouble()
                }
                refresh?.invoke(duplicated)
                false
            }
            max.setOnKeyListener { _: View, _: Int, _: KeyEvent? ->
                val trim = max.text.toString().trim { it <= ' ' }
                if (trim.isEmpty()) {
                    duplicated.item.hasMaxValue = false
                } else {
                    duplicated.item.hasMaxValue = true
                    duplicated.item.maxValue = trim.toDouble()
                }
                refresh?.invoke(duplicated)
                false
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(container: FilterItemContainer): View {
            val frameLayout = container.frameLayout
            val context = frameLayout.context
            LayoutInflater.from(context)
                .inflate(R.layout.item_filter_value_range, frameLayout, true)
            return container.view
        }
    }
}