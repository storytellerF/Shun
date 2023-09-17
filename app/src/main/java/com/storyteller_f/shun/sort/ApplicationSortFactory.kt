package com.storyteller_f.shun.sort

import com.example.common_config.ApplicationItem
import com.storyteller_f.shun.sort.view_holder.DateSortViewHolder
import com.storyteller_f.shun.sort.view_holder.NameSortViewHolder
import com.storyteller_f.shun.sort.view_holder.PackageSortViewHolder
import com.storyteller_f.sort_ui.adapter.SortItemContainer
import com.storyteller_f.sort_ui.adapter.SortItemViewHolder
import com.storyteller_f.sort_ui.adapter.SortViewHolderFactory

class ApplicationSortFactory : SortViewHolderFactory<ApplicationItem>() {
    override fun create(
        viewType: Int,
        container: SortItemContainer
    ): SortItemViewHolder<ApplicationItem> {
        val itemView = SortItemViewHolder.Simple.create(container)
        return when (viewType) {
            0 -> PackageSortViewHolder(itemView)

            1 -> NameSortViewHolder(itemView)

            2 -> DateSortViewHolder(itemView)

            else -> throw Exception(viewType.toString())
        }
    }
}