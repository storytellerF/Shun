package com.storyteller_f.filter_ui.choose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_ui.R
import com.storyteller_f.filter_ui.choose.ChooseFilterAdapter.ChooseFilterViewHolder

class ChooseFilterAdapter<T>(private val config: List<Filter<T>>) :
    RecyclerView.Adapter<ChooseFilterViewHolder?>() {
    private var listener: Listener<Filter<T>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseFilterViewHolder {
        return ChooseFilterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ChooseFilterViewHolder, position: Int) {
        holder.bind(config[position])
        holder.itemView.setOnClickListener { view: View? ->
            if (listener != null) listener!!.onChoose(
                config[position]
            )
        }
    }

    fun setListener(listener: Listener<Filter<T>>?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return config.size
    }

    interface Listener<T> {
        fun onChoose(t: T)
    }

    class ChooseFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var icon: ImageView

        init {
            name = itemView.findViewById(R.id.choose_name)
            icon = itemView.findViewById(R.id.choose_icon)
        }

        fun bind(filterChain: Filter<*>) {
            name.text = filterChain.showName
        }

        companion object {
            fun create(parent: ViewGroup): ChooseFilterViewHolder {
                return ChooseFilterViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_choose, parent, false)
                )
            }
        }
    }
}