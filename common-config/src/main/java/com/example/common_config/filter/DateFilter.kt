package com.example.common_config.filter

import com.example.common_config.ApplicationItem
import com.storyteller_f.filter_core.config.SimpleDateConfigItem
import com.storyteller_f.filter_core.filter.simple.SimpleDateRangeFilter
import java.util.Date

class DateFilter(item: SimpleDateConfigItem) :
    SimpleDateRangeFilter<ApplicationItem>("安装时间", item) {
    override fun getTime(t: ApplicationItem) = t.installTime

    override val itemViewType: Int
        get() = 2

    override fun dup() = DateFilter(item.dup() as SimpleDateConfigItem)

    class ConfigItem(start: Date?, end: Date?, id: Long) : SimpleDateConfigItem(start, end, id) {
        override fun dup() = ConfigItem(startTime, endTime, System.currentTimeMillis())
    }
}