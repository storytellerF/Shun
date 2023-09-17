package com.storyteller_f.filter_core.config

import com.storyteller_f.filter_core.filter.DateRange
import java.util.Date

abstract class SimpleDateConfigItem(override var startTime: Date?, override var endTime: Date?) :
    FilterConfigItem(),
    DateRange {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleDateConfigItem

        if (startTime != other.startTime) return false
        return endTime == other.endTime
    }

    override fun hashCode(): Int {
        var result = startTime?.hashCode() ?: 0
        result = 31 * result + (endTime?.hashCode() ?: 0)
        return result
    }
}