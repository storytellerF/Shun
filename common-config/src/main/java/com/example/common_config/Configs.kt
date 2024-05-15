package com.example.common_config

import com.example.common_config.filter.AppSizeFilter
import com.example.common_config.filter.DateFilter
import com.example.common_config.filter.NameFilter
import com.example.common_config.filter.PackageFilter
import com.example.common_config.sort.DateSort
import com.example.common_config.sort.NameSort
import com.example.common_config.sort.PackageSort
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.SimpleListener
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.config.FilterConfigItem
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem

val List<FilterConfigItem>.buildFilter
    get() = mapNotNull { configItem ->
        when (configItem) {
            is PackageFilter.ConfigItem -> PackageFilter(configItem)

            is NameFilter.ConfigItem -> NameFilter(configItem)

            is DateFilter.ConfigItem -> DateFilter(configItem)

            is AppSizeFilter.ConfigItem -> AppSizeFilter(configItem)

            else -> null
        }
    }

val List<Filter<ApplicationItem>>.extractFilterConfig
    get() = mapNotNull {
        when (it) {
            is PackageFilter -> it.item

            is NameFilter -> it.item

            is DateFilter -> it.item

            is AppSizeFilter -> it.item

            else -> null
        }
    }

val filterAdapterFactory: RuntimeTypeAdapterFactory<ConfigItem> = RuntimeTypeAdapterFactory.of(
    ConfigItem::class.java, ConfigItem.DEFAULT_FACTORY_KEY
).registerSubtype(DateFilter.ConfigItem::class.java, "date")
    .registerSubtype(NameFilter.ConfigItem::class.java, "name")
    .registerSubtype(AppSizeFilter.ConfigItem::class.java, "app size")
    .registerSubtype(PackageFilter.ConfigItem::class.java, "package")

fun buildFilterListener() =
    object : SimpleListener<Filter<ApplicationItem>, FilterConfigItem>() {
        override fun onSaveState(oList: List<Filter<ApplicationItem>>) =
            oList.extractFilterConfig

        override fun onRestoreState(configItems: List<FilterConfigItem>) =
            configItems.buildFilter

    }

fun filters() = buildList {
    add(
        PackageFilter.ConfigItem("", 0, null)
    )
    add(
        NameFilter.ConfigItem("", 0, null)
    )
    add(
        DateFilter.ConfigItem(null, null, 0, null)
    )
    add(
        AppSizeFilter.ConfigItem(
            0.0, 0.0, hasMinValue = false, hasMaxValue = false, 0, null
        )
    )
}.buildFilter

val List<SortConfigItem>.buildSortChain
    get() = mapNotNull { configItem ->
        when (configItem) {
            is PackageSort.Config -> PackageSort(configItem)

            is NameSort.Config -> NameSort(configItem)

            else -> null
        }
    }

val List<SortChain<ApplicationItem>>.extractSortConfig
    get() = mapNotNull {
        when (it) {
            is PackageSort -> it.item

            is NameSort -> it.item

            is DateSort -> it.item
            else -> null
        }
    }

fun buildSortListener() =
    object : SimpleListener<SortChain<ApplicationItem>, SortConfigItem>() {
        override fun onSaveState(oList: List<SortChain<ApplicationItem>>) =
            oList.extractSortConfig

        override fun onRestoreState(configItems: List<SortConfigItem>) =
            configItems.buildSortChain
    }

val sortAdapterFactory: RuntimeTypeAdapterFactory<ConfigItem> = RuntimeTypeAdapterFactory.of(
    ConfigItem::class.java, ConfigItem.DEFAULT_SORT_FACTORY_KEY
).registerSubtype(PackageSort.Config::class.java, "package")
    .registerSubtype(NameSort.Config::class.java, "name")

fun sortChains() = buildList {
    add(PackageSort(PackageSort.Config(0, null)))
    add(NameSort(NameSort.Config(0, null)))
}