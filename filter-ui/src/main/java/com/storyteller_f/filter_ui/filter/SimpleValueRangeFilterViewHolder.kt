package com.storyteller_f.filter_ui.filter

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.filter.simple.SimpleValueRangeFilter
import com.storyteller_f.filter_ui.R
import com.storyteller_f.filter_ui.adapter.FilterItemContainer
import com.storyteller_f.filter_ui.adapter.FilterItemViewHolder

class SimpleValueRangeFilterViewHolder<T>(itemView: View) : FilterItemViewHolder<T>(itemView) {
    var name: TextView
    private var min: EditText
    private var max: EditText

    init {
        name = itemView.findViewById(R.id.value_range_label)
        min = itemView.findViewById(R.id.value_range_min)
        max = itemView.findViewById(R.id.value_range_max)
    }

    override fun bind(filterChain: Filter<T>) {
        name.text = filterChain.showName
        if (filterChain is SimpleValueRangeFilter<T>) {
            @Suppress("UNCHECKED_CAST") val duplicated =
                filterChain.dup() as SimpleValueRangeFilter<T>
            min.setText(String.format("%s", duplicated.minValue))
            max.setText(String.format("%s", duplicated.maxValue))
            min.setOnKeyListener { v: View, _: Int, _: KeyEvent? ->
                val trim = min.text.toString().trim { it <= ' ' }
                if (trim.isEmpty()) {
                    duplicated.item.hasMinValue = false
                } else {
                    duplicated.item.hasMinValue = true
                    duplicated.item.minValue = trim.toDouble()
                }
                refresh?.onChanged(v, duplicated)
                false
            }
            max.setOnKeyListener { v: View, _: Int, _: KeyEvent? ->
                val trim = max.text.toString().trim { it <= ' ' }
                if (trim.isEmpty()) {
                    duplicated.item.hasMaxValue = false
                } else {
                    duplicated.item.hasMaxValue = true
                    duplicated.item.maxValue = trim.toDouble()
                }
                refresh?.onChanged(v, duplicated)
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