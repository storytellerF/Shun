package com.storyteller_f.recycleview_ui_extra

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class DragItemHelper(
    private val objects: MutableList<*>,
    private val myAdapter: RecyclerView.Adapter<*>
) :
    ItemTouchHelper.Callback() {
    @JvmField
    var listener: Listener? = null
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //得到当拖拽的viewHolder的Position
        val fromPosition = viewHolder.adapterPosition
        //拿到当前拖拽到的item的viewHolder
        val toPosition = target.adapterPosition
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(objects, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(objects, i, i - 1)
            }
        }
        myAdapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapterPosition = viewHolder.adapterPosition
        val removed: Any? = objects.removeAt(adapterPosition)
        myAdapter.notifyItemRemoved(adapterPosition)
        if (listener != null) listener!!.onRemoved(adapterPosition, removed)
    }

    @FunctionalInterface
    interface Listener {
        fun onRemoved(position: Int, removed: Any?)
    }
}