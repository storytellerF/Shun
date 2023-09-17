package com.storyteller_f.filter_core.filter

import com.storyteller_f.filter_core.Filter
import java.util.Date

interface DateRange {
    val endTime: Date?
    val startTime: Date?
}

abstract class DateRangeFilter<T>(showName: String) : Filter<T>(showName), DateRange {
    override fun filter(t: T): Boolean {
        val time = getTime(t)
        val startTime = startTime
        val endTime = endTime
        return time.after(startTime) && time.before(endTime)
    }

    abstract fun getTime(t: T): Date

    abstract override val itemViewType: Int

    companion object
}