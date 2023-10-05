package com.storyteller_f.recycleview_ui_extra

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.config_core.DefaultDialog

class DragItemHelper(
    private val myAdapter: RecyclerView.Adapter<*>
) :
    ItemTouchHelper.Callback() {
    @JvmField
    var dragAndSwipe:  DefaultDialog.DragAndSwipe? = null
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //得到当拖拽的viewHolder的Position
        val fromPosition = viewHolder.adapterPosition
        //拿到当前拖拽到的item的viewHolder
        val toPosition = target.adapterPosition
        dragAndSwipe?.onMoved(fromPosition, toPosition)
        myAdapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapterPosition = viewHolder.adapterPosition
        dragAndSwipe?.onRemoved(adapterPosition)
        myAdapter.notifyItemRemoved(adapterPosition)
    }

}