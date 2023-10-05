package com.storyteller_f.filter_ui.filter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.storyteller_f.common_dialog.RenameDialog
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.filter.simple.SimpleRegExpFilter
import com.storyteller_f.filter_ui.R
import com.storyteller_f.filter_ui.adapter.FilterItemContainer
import com.storyteller_f.filter_ui.adapter.FilterItemViewHolder

open class SimpleRegExpFilterViewHolder<T>(itemView: View) : FilterItemViewHolder<T>(itemView) {
    private var editText: EditText
    private var textView: TextView
    private val editButton: ImageButton

    init {
        editText = itemView.findViewById(R.id.filter_input_regexp)
        textView = itemView.findViewById(R.id.filter_regexp_label)
        editButton = itemView.findViewById(R.id.edit_name)
    }

    val input: String
        get() = editText.text.toString().trim { it <= ' ' }

    override fun bind(filterChain: Filter<T>) {
        if (filterChain is SimpleRegExpFilter<T>) {
            editButton.setOnClickListener {
                RenameDialog(it.context, filterChain.currentName) { newName ->
                    @Suppress("UNCHECKED_CAST") val dup = filterChain.dup() as SimpleRegExpFilter<T>
                    dup.item.name = newName
                    refresh?.invoke(dup)
                }.show()
            }
            editText.setText(filterChain.regexp)
            textView.text = filterChain.currentName
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
                    refresh?.invoke(duplicated)
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