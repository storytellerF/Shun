package com.storyteller_f.sort_core.config

class SortChains<T>(private val sortChain: List<SortChain<T>>): Comparator<T> {
    init {
        assert(sortChain.isNotEmpty())
    }
    override fun compare(p0: T, p1: T): Int {
        for (it in sortChain) {
            val item = it.item
            val i = if (item.sortDirection == SortConfigItem.up) {
                it.compare(p0, p1)
            } else {
                it.compare(p1, p0)
            }
            if (i != 0) {
                return i
            }
        }
        return 0
    }
}