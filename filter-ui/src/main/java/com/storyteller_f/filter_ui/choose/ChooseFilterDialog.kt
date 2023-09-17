package com.storyteller_f.filter_ui.choose

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_ui.R

class ChooseFilterDialog<T>(context: Context?, filters: List<Filter<T>>) {
    private var alertDialog: AlertDialog? = null
    private var listener: ChooseFilterAdapter.Listener<Filter<T>>? = null

    init {
        val inflate =
            LayoutInflater.from(context).inflate(R.layout.dialog_choose_filter, null, false)
        val recyclerView = inflate.findViewById<RecyclerView>(R.id.sort_ui_choose)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val chooseFilterAdapter = ChooseFilterAdapter(filters)
        recyclerView.adapter = chooseFilterAdapter
        chooseFilterAdapter.setListener(object : ChooseFilterAdapter.Listener<Filter<T>> {
            override fun onChoose(t: Filter<T>) {
                if (listener != null) {
                    listener!!.onChoose(t)
                }
                alertDialog!!.dismiss()
            }

        })
        val builder = AlertDialog.Builder(context).setTitle("选择···").setView(inflate)
        alertDialog = builder.create()
    }

    fun setResultListener(listener: ChooseFilterAdapter.Listener<Filter<T>>) {
        this.listener = listener
    }

    fun show() {
        alertDialog!!.show()
    }

}