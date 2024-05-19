package com.storyteller_f.common_dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.storyteller_f.common_dialog.ChooseSortAdapter.ChooseViewHolder
import com.storyteller_f.config_core.Core

class ChooseSortAdapter(private val config: List<Core>) :
    RecyclerView.Adapter<ChooseViewHolder>() {
    private var listener: Listener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.bind(config[position])
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onChoose(
                config[position]
            )
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return config.size
    }

    @FunctionalInterface
    interface Listener {
        fun onChoose(t: Core)
    }

    class ChooseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.choose_name)
        private var icon: ImageView = itemView.findViewById(R.id.choose_icon)

        fun bind(sortChain: Core) {
            name.text = sortChain.showName
            icon.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    android.R.color.darker_gray
                )
            )
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