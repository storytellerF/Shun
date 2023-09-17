package com.storyteller_f.filter_core.filter

import com.storyteller_f.filter_core.Filter

interface ValueRange {
    val hasMinValue: Boolean
    val hasMaxValue: Boolean
    val minValue: Double
    val maxValue: Double
}

abstract class ValueRangeFilter<T>(showName: String) : Filter<T>(showName), ValueRange {
    override fun filter(t: T): Boolean {
        val value = getValue(t)
        if (hasMinValue) {
            if (minValue > value) {
                return false
            }
        }
        return if (hasMaxValue) {
            maxValue >= value
        } else true
    }

    abstract fun getValue(t: T): Double
}