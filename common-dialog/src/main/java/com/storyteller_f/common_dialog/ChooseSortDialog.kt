package com.storyteller_f.common_dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.config_core.Core

class ChooseSortDialog(context: Context, config: List<Core>) {
    private var alertDialog: AlertDialog? = null
    private var listener: ChooseSortAdapter.Listener? = null

    init {
        val inflate =
            LayoutInflater.from(context).inflate(R.layout.dialog_sort_choose, null, false)
        val recyclerView = inflate.findViewById<RecyclerView>(R.id.choose_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val chooseSortAdapter = ChooseSortAdapter(config)
        recyclerView.adapter = chooseSortAdapter
        chooseSortAdapter.setListener(object : ChooseSortAdapter.Listener {
            override fun onChoose(t: Core) {
                if (listener != null) listener!!.onChoose(t)
                alertDialog!!.dismiss()
            }

        })
        val builder = AlertDialog.Builder(context).setTitle("选择···").setView(inflate)
        alertDialog = builder.create()
    }

    fun setListener(listener: ChooseSortAdapter.Listener) {
        this.listener = listener
    }

    fun show() {
        alertDialog!!.show()
    }

}