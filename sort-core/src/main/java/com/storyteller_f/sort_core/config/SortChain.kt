package com.storyteller_f.sort_core.config

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.Core
import com.storyteller_f.config_core.Duplicable
import java.util.Objects

/**
 * 如果当前无法判断大小，通过next 继续判断
 *
 * @param <T> 需要处理的数据类型
</T> */
abstract class SortChain<T>(showName: String, var item: SortConfigItem) : Core(showName),
    Comparator<T>, Duplicable {

    abstract val itemViewType: Int
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val sortChain = other as SortChain<*>
        return showName == sortChain.showName && item == sortChain.item
    }

    override fun hashCode(): Int {
        return Objects.hash(showName, item)
    }
}

val sortConfigAdapterFactory: RuntimeTypeAdapterFactory<Config> = RuntimeTypeAdapterFactory.of(
    Config::class.java, "config_edit_sort_config_key"
).registerSubtype(
    SortConfig::class.java, "sort-config"
)