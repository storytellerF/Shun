package com.storyteller_f.sort_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.storyteller_f.sort_ui.R

class SortItemContainer(context: ViewGroup) {
    @JvmField
    val view: View

    @JvmField
    val frameLayout: FrameLayout

    init {
        view = LayoutInflater.from(context.context)
            .inflate(R.layout.item_sort_container, context, false)
        frameLayout = view.findViewById(R.id.sort_ui_frameLayout)
    }

    fun add(view: View?) {
        frameLayout.addView(view)
    }
}