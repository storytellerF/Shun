package com.storyteller_f.shun.filter

import com.example.common_config.ApplicationItem
import com.storyteller_f.filter_ui.adapter.FilterItemContainer
import com.storyteller_f.filter_ui.adapter.FilterItemViewHolder
import com.storyteller_f.filter_ui.adapter.FilterViewHolderFactory
import com.storyteller_f.filter_ui.filter.SimpleDataRangeFilterViewHolder
import com.storyteller_f.filter_ui.filter.SimpleRegExpFilterViewHolder
import com.storyteller_f.filter_ui.filter.SimpleValueRangeFilterViewHolder
import com.storyteller_f.shun.filter.view_holder.DateFilterViewHolder
import com.storyteller_f.shun.filter.view_holder.NameFilterViewHolder
import com.storyteller_f.shun.filter.view_holder.PackageFilterViewHolder

class ApplicationFilterFactory : FilterViewHolderFactory<ApplicationItem>() {
    override fun create(
        viewType: Int, container: FilterItemContainer
    ): FilterItemViewHolder<ApplicationItem> {
        return when (viewType) {
            0 -> PackageFilterViewHolder(
                SimpleRegExpFilterViewHolder.create(container)
            )

            1 -> NameFilterViewHolder(SimpleRegExpFilterViewHolder.create(container))

            2 -> DateFilterViewHolder(SimpleDataRangeFilterViewHolder.create(container))

            3 -> SimpleValueRangeFilterViewHolder(
                SimpleValueRangeFilterViewHolder.create(
                    container
                )
            )

            else -> throw Exception(viewType.toString())
        }
    }
}