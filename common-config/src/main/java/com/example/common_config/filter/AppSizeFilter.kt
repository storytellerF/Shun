package com.example.common_config.filter

import com.example.common_config.ApplicationItem
import com.storyteller_f.filter_core.config.SimpleValueRangeConfigItem
import com.storyteller_f.filter_core.filter.simple.SimpleValueRangeFilter

class AppSizeFilter(item: SimpleValueRangeConfigItem) :
    SimpleValueRangeFilter<ApplicationItem>("大小", item) {
    override fun getValue(t: ApplicationItem) = t.size.toDouble()

    override val itemViewType: Int
        get() = 3

    override fun dup() = AppSizeFilter(item.dup() as SimpleValueRangeConfigItem)

    class ConfigItem(min: Double, max: Double, hasMinValue: Boolean, hasMaxValue: Boolean) :
        SimpleValueRangeConfigItem(min, max, hasMinValue, hasMaxValue) {
        override fun dup() = ConfigItem(minValue, maxValue, hasMinValue, hasMaxValue)
    }
}