package com.storyteller_f.filter_core.config

import com.storyteller_f.filter_core.filter.RegExp

abstract class SimpleRegExpConfigItem(override var regexp: String, id: Long, name: String?) : FilterConfigItem(id, name), RegExp {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleRegExpConfigItem

        if (regexp != other.regexp) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = regexp.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}