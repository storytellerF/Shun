package com.storyteller_f.filter_core.filter.simple

import com.storyteller_f.config_core.Identify
import com.storyteller_f.filter_core.config.SimpleValueRangeConfigItem
import com.storyteller_f.filter_core.filter.ValueRange
import com.storyteller_f.filter_core.filter.ValueRangeFilter
import java.util.Objects

abstract class SimpleValueRangeFilter<T>(
    showName: String,
    val item: SimpleValueRangeConfigItem
) :
    ValueRangeFilter<T>(
        showName
    ), ValueRange by item, Identify by item {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        if (!super.equals(other)) return false
        val that = other as SimpleValueRangeFilter<*>
        return item == that.item
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), item)
    }

    override fun getValue(t: T): Double {
        return 0.0
    }


}