package com.storyteller_f.sort_core.config

import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.Editor
import java.util.Calendar
import java.util.Locale

class SortConfig(name: String) : Config(name, 0) {

    override fun dup(): SortConfig {
        val sortConfig = SortConfig(name)
        for (sortConfigItem in configItems) {
            sortConfig.configItems.add(sortConfigItem.dup() as SortConfigItem)
        }
        return sortConfig
    }

    companion object {
        fun create(): SortConfig {
            return SortConfig("未命名" + Calendar.getInstance(Locale.CHINA).timeInMillis)
        }

        val emptySortListener = object : Editor.Listener<SortConfig> {
            override fun onConfigSelectedChanged(configIndex: Int, config: SortConfig?, total: Int) {

            }

            override fun onNew() = create()

        }
    }
}