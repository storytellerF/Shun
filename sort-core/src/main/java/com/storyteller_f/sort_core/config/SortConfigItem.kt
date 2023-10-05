package com.storyteller_f.sort_core.config

import com.storyteller_f.config_core.ConfigItem
import java.io.Serializable
import java.util.Objects

abstract class SortConfigItem(id: Long, name: String?, var sortDirection: Int = UP) : ConfigItem(id, name), Serializable {

    companion object {
        const val UP = 1
        const val DOWN = -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SortConfigItem

        if (name != other.name) return false
        if (sortDirection != other.sortDirection) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + sortDirection
        return result
    }
}