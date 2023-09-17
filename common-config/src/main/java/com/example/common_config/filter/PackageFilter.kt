package com.example.common_config.filter

import com.example.common_config.ApplicationItem
import com.storyteller_f.filter_core.config.SimpleRegExpConfigItem
import com.storyteller_f.filter_core.filter.simple.SimpleRegExpFilter

class PackageFilter(item: SimpleRegExpConfigItem) :
    SimpleRegExpFilter<ApplicationItem>("包 名", item) {
    override fun getMatchString(t: ApplicationItem) =
        t.packageName

    override val itemViewType: Int
        get() = 0

    override fun dup() = PackageFilter(item.dup() as SimpleRegExpConfigItem)

    class ConfigItem(regexp: String?) : SimpleRegExpConfigItem(regexp!!) {
        override fun dup() = ConfigItem(regexp)
    }
}