package com.storyteller_f.config_core

interface Identify {
    val id: Long
}

/**
 * @param showName 用于在ui 上作为区分。和ConfigItem 的name 不同。
 */
abstract class Core(val showName: String): Identify, Duplicable