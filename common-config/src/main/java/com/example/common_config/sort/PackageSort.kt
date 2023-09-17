package com.example.common_config.sort

import com.example.common_config.ApplicationItem
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem

class PackageSort(item: SortConfigItem) : SortChain<ApplicationItem>("包名", item) {
    override fun compare(o1: ApplicationItem, o2: ApplicationItem): Int {
        return o1.packageName.compareTo(o2.packageName)
    }

    override val itemViewType: Int
        get() = 0

    override fun dup(): PackageSort {
        return PackageSort(item.dup() as SortConfigItem)
    }

    class Config : SortConfigItem() {
        override fun dup(): Config {
            return Config()
        }
    }
}