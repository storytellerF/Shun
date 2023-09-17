package com.storyteller_f.sort_ui.choose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_ui.R
import com.storyteller_f.sort_ui.choose.ChooseSortAdapter.ChooseViewHolder

class ChooseSortAdapter<T>(private val config: List<SortChain<T>>) :
    RecyclerView.Adapter<ChooseViewHolder>() {
    private var listener: Listener<SortChain<T>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.bind(config[position])
        holder.itemView.setOnClickListener { view: View? ->
            if (listener != null) listener!!.onChoose(
                config[position]
            )
        }
    }

    fun setListener(listener: Listener<SortChain<T>>) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return config.size
    }

    @FunctionalInterface
    interface Listener<T> {
        fun onChoose(t: T)
    }

    class ChooseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var icon: ImageView

        init {
            name = itemView.findViewById(R.id.choose_name)
            icon = itemView.findViewById(R.id.choose_icon)
        }

        fun bind(sortChain: SortChain<*>) {
            name.text = sortChain.showName
        }

        companion object {
            fun create(parent: ViewGroup): ChooseViewHolder {
                return ChooseViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_choose, parent, false)
                )
            }
        }
    }

    companion object
}