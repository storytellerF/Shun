package com.example.common_config.sort

import com.example.common_config.ApplicationItem
import com.storyteller_f.config_core.Identify
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem

class NameSort(sortConfigItem: SortConfigItem) :
    SortChain<ApplicationItem>("应用名", sortConfigItem), Identify by sortConfigItem {
    override fun compare(o1: ApplicationItem, o2: ApplicationItem): Int {
        return o1.name.compareTo(o2.name)
    }

    override val itemViewType: Int
        get() = 1

    override fun dup(): NameSort {
        return NameSort(item.dup() as SortConfigItem)
    }

    class Config(id: Long) : SortConfigItem(id) {
        override fun dup(): Config {
            return Config(System.currentTimeMillis())
        }
    }
}