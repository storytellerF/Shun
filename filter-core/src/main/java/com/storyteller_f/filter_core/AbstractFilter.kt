package com.storyteller_f.filter_core

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.Core
import com.storyteller_f.config_core.Duplicable
import com.storyteller_f.filter_core.config.FilterConfig

abstract class AbstractFilter<T>(showName: String) : Core(showName) {

    /**
     * @param t
     * @return 如果通过筛选，返回true
     */
    abstract fun filter(t: T): Boolean

    abstract val itemViewType: Int

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractFilter<*>

        return showName == other.showName
    }

    override fun hashCode(): Int {
        return showName.hashCode()
    }


}

abstract class Filter<T>(showName: String?) : AbstractFilter<T>(
    showName!!
), Duplicable

val filterConfigAdapterFactory: RuntimeTypeAdapterFactory<Config> = RuntimeTypeAdapterFactory.of(
    Config::class.java, "config_edit_filter_config_key"
).registerSubtype(
    FilterConfig::class.java, "filter-config"
)