package com.storyteller_f.filter_core.config

import com.storyteller_f.filter_core.filter.DateRange
import java.util.Date

abstract class SimpleDateConfigItem(
    override var startTime: Date?, override var endTime: Date?,
    id: Long, name: String?
) :
    FilterConfigItem(id, name),
    DateRange {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleDateConfigItem

        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startTime?.hashCode() ?: 0
        result = 31 * result + (endTime?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}