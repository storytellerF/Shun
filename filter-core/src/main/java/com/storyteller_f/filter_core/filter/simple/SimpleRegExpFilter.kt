package com.storyteller_f.filter_core.filter.simple

import com.storyteller_f.config_core.Identify
import com.storyteller_f.filter_core.config.SimpleRegExpConfigItem
import com.storyteller_f.filter_core.filter.RegExp
import com.storyteller_f.filter_core.filter.RegExpFilter
import java.util.Objects

abstract class SimpleRegExpFilter<T>(
    showName: String,
    val item: SimpleRegExpConfigItem
) :
    RegExpFilter<T>(
        showName
    ), RegExp by item, Identify by item {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SimpleRegExpFilter<*>
        return item == that.item
    }

    override fun hashCode(): Int {
        return Objects.hash(item)
    }


}