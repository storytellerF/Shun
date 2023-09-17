package com.storyteller_f.filter_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.storyteller_f.filter_ui.R

class FilterItemContainer(context: ViewGroup) {
    @JvmField
    val view: View
    var frameLayout: FrameLayout
        protected set

    init {
        view = LayoutInflater.from(context.context)
            .inflate(R.layout.item_filter_container, context, false)
        frameLayout = view.findViewById(R.id.filter_ui_frameLayout)
    }
}