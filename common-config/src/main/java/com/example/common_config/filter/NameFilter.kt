package com.example.common_config.filter

import com.example.common_config.ApplicationItem
import com.storyteller_f.filter_core.config.SimpleRegExpConfigItem
import com.storyteller_f.filter_core.filter.simple.SimpleRegExpFilter

class NameFilter(item: SimpleRegExpConfigItem) :
    SimpleRegExpFilter<ApplicationItem>("应用名", item) {
    override val itemViewType: Int
        get() = 1

    override fun getMatchString(t: ApplicationItem) =
        t.name

    override fun dup() = NameFilter(item.dup() as SimpleRegExpConfigItem)

    class ConfigItem(regexp: String?, id: Long) : SimpleRegExpConfigItem(regexp!!, id) {
        override fun dup() = ConfigItem(regexp, System.currentTimeMillis())
    }
}