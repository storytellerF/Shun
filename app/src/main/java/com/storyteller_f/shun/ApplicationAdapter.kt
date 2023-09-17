package com.storyteller_f.shun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ApplicationAdapter :
    ListAdapter<com.example.common_config.ApplicationItem, ApplicationAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<com.example.common_config.ApplicationItem>() {
        override fun areItemsTheSame(
            oldItem: com.example.common_config.ApplicationItem,
            newItem: com.example.common_config.ApplicationItem
        ) =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(
            oldItem: com.example.common_config.ApplicationItem,
            newItem: com.example.common_config.ApplicationItem
        ) = oldItem == newItem

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_application, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        private var packageName: TextView
        private var date: TextView
        private var simpleDateFormat =
            SimpleDateFormat("yyyy年MM月dd日 hh时 mm:ss:SSS", Locale.CHINA)

        init {
            name = itemView.findViewById(R.id.application_name)
            packageName = itemView.findViewById(R.id.application_package)
            date = itemView.findViewById(R.id.application_time)
        }

        fun bind(applicationItem: com.example.common_config.ApplicationItem) {
            name.text = applicationItem.name
            packageName.text = applicationItem.packageName
            date.text = simpleDateFormat.format(applicationItem.installTime)
        }
    }
}