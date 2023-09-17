package com.storyteller_f.filter_core.config

import com.storyteller_f.filter_core.filter.RegExp

abstract class SimpleRegExpConfigItem(override var regexp: String) : FilterConfigItem(), RegExp {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleRegExpConfigItem

        return regexp == other.regexp
    }

    override fun hashCode(): Int {
        return regexp.hashCode()
    }
}