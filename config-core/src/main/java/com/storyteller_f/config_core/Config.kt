package com.storyteller_f.config_core

import java.io.Serializable

abstract class Config(var name: String, var id: Int = 0) : Duplicable, Serializable {

    val configItems = mutableListOf<ConfigItem>()

    fun update(sortConfigItems: List<ConfigItem>) {
        configItems.clear()
        configItems.addAll(sortConfigItems)
    }

    companion object {
        const val NONE_INDEX = -1
        const val NONE_ID = -1
    }
}

/**
 * @param name 默认会继承Core的showName，但是也可以后续修改
 */
abstract class ConfigItem(override val id: Long, var name: String?) : Duplicable, Serializable,
    Identify
