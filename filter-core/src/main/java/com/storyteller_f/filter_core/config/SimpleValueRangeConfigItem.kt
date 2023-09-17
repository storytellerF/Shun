package com.storyteller_f.filter_core.config

import com.storyteller_f.filter_core.filter.ValueRange

abstract class SimpleValueRangeConfigItem(
    override var minValue: Double,
    override var maxValue: Double,
    override var hasMinValue: Boolean,
    override var hasMaxValue: Boolean
) : FilterConfigItem(), ValueRange {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleValueRangeConfigItem

        if (minValue != other.minValue) return false
        if (maxValue != other.maxValue) return false
        if (hasMinValue != other.hasMinValue) return false
        return hasMaxValue == other.hasMaxValue
    }

    override fun hashCode(): Int {
        var result = minValue.hashCode()
        result = 31 * result + maxValue.hashCode()
        result = 31 * result + hasMinValue.hashCode()
        result = 31 * result + hasMaxValue.hashCode()
        return result
    }
}