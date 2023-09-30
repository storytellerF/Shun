package com.storyteller_f.sort_core.config

import com.storyteller_f.config_core.ConfigItem
import java.io.Serializable
import java.util.Objects

abstract class SortConfigItem(id: Long, var sortDirection: Int = up) : ConfigItem(id), Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SortConfigItem
        return sortDirection == that.sortDirection
    }

    override fun hashCode(): Int {
        return Objects.hash(sortDirection)
    }

    companion object {
        const val up = 1
        const val down = -1
    }
}