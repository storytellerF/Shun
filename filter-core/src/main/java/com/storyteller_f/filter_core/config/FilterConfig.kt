package com.storyteller_f.filter_core.config

import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.Editor
import java.util.Calendar
import java.util.Locale

class FilterConfig(name: String) : Config(name, 0) {

    override fun dup(): FilterConfig {
        val filterConfig = FilterConfig(name)
        for (configItem in configItems) {
            filterConfig.configItems.add(configItem.dup() as FilterConfigItem)
        }
        return filterConfig
    }

    companion object {
        fun create(): FilterConfig {
            return FilterConfig("未命名" + Calendar.getInstance(Locale.CHINA).timeInMillis)
        }

        val emptyFilterListener = object : Editor.Listener<FilterConfig> {
            override fun onConfigSelectedChanged(configIndex: Int, config: FilterConfig?, total: Int) {}

            override fun onNew() = create()
        }
    }
}

abstract class FilterConfigItem : ConfigItem()
