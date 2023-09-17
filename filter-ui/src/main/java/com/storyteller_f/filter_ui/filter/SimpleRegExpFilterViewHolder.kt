package com.storyteller_f.filter_ui.filter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.filter.simple.SimpleRegExpFilter
import com.storyteller_f.filter_ui.R
import com.storyteller_f.filter_ui.adapter.FilterItemContainer
import com.storyteller_f.filter_ui.adapter.FilterItemViewHolder
import java.util.Locale

open class SimpleRegExpFilterViewHolder<T>(itemView: View) : FilterItemViewHolder<T>(itemView) {
    private var editText: EditText
    private var textView: TextView

    init {
        editText = itemView.findViewById(R.id.filter_input_regexp)
        textView = itemView.findViewById(R.id.filter_regexp_label)
    }

    val input: String
        get() = editText.text.toString().trim { it <= ' ' }

    override fun bind(filterChain: Filter<T>) {
        if (filterChain is SimpleRegExpFilter<T>) {
            editText.setText(filterChain.regexp)
            textView.text =
                String.format(Locale.CHINA, "请输入%s的正则表达式", filterChain.showName)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    @Suppress("UNCHECKED_CAST") val duplicated =
                        filterChain.dup() as SimpleRegExpFilter<T>
                    duplicated.item.regexp = input
                    refresh?.onChanged(editText, duplicated)
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun create(container: FilterItemContainer): View {
            val frameLayout = container.frameLayout
            val context = frameLayout.context
            LayoutInflater.from(context)
                .inflate(R.layout.item_filter_regexp, frameLayout, true)
            return container.view
        }
    }
}