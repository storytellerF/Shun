package com.storyteller_f.filter_core.filter.simple

import com.storyteller_f.config_core.Identify
import com.storyteller_f.filter_core.config.SimpleDateConfigItem
import com.storyteller_f.filter_core.filter.DateRange
import com.storyteller_f.filter_core.filter.DateRangeFilter
import java.util.Date
import java.util.Objects

abstract class SimpleDateRangeFilter<T>(
    showName: String,
    val item: SimpleDateConfigItem
) :
    DateRangeFilter<T>(
        showName
    ), DateRange by item, Identify by item {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SimpleDateRangeFilter<*>
        return item == that.item
    }

    override fun hashCode(): Int {
        return Objects.hash(item)
    }

    abstract override fun getTime(t: T): Date

    override val itemViewType: Int
        get() = 1
}