package com.storyteller_f.sort_ui.choose

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_ui.R

class ChooseSortDialog<T>(context: Context, config: List<SortChain<T>>) {
    private var alertDialog: AlertDialog? = null
    private var listener: ChooseSortAdapter.Listener<SortChain<T>>? = null

    init {
        val inflate =
            LayoutInflater.from(context).inflate(R.layout.dialog_sort_choose, null, false)
        val recyclerView = inflate.findViewById<RecyclerView>(R.id.choose_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val chooseSortAdapter = ChooseSortAdapter(config)
        recyclerView.adapter = chooseSortAdapter
        chooseSortAdapter.setListener(object : ChooseSortAdapter.Listener<SortChain<T>> {
            override fun onChoose(t: SortChain<T>) {
                if (listener != null) listener!!.onChoose(t)
                alertDialog!!.dismiss()
            }

        })
        val builder = AlertDialog.Builder(context).setTitle("选择···").setView(inflate)
        alertDialog = builder.create()
    }

    fun setListener(listener: ChooseSortAdapter.Listener<SortChain<T>>) {
        this.listener = listener
    }

    fun show() {
        alertDialog!!.show()
    }

    fun close() {
        alertDialog!!.dismiss()
    }
}