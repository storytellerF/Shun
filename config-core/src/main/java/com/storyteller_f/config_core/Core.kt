package com.storyteller_f.config_core

interface Identify {
    val id: Long
}

abstract class Core(val showName: String): Identify, Duplicable