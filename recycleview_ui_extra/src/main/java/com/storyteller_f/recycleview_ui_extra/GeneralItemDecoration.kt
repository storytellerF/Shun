package com.storyteller_f.recycleview_ui_extra

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GeneralItemDecoration(context: Context) : ItemDecoration() {
    private val divider: Drawable?

    init {
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        divider = typedArray.getDrawable(0)
        typedArray.recycle()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (view.layoutParams.height == 0) {
            outRect[0, 0, 0] = 0
        } else outRect[0, 0, 0] = divider!!.intrinsicWidth
    }
}