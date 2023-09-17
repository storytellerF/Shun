package com.example.common_config.sort

import com.example.common_config.ApplicationItem
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem

class DateSort(showName: String, item: Config) : SortChain<ApplicationItem>(
    showName, item
) {
    override fun compare(o1: ApplicationItem, o2: ApplicationItem): Int {
        return o1.installTime.compareTo(o2.installTime)
    }

    override val itemViewType: Int
        get() = 2

    override fun dup(): DateSort {
        return DateSort(showName, item.dup() as Config)
    }

    class Config : SortConfigItem() {
        override fun dup(): Config {
            return Config()
        }
    }
}