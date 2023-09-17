package com.storyteller_f.config_core

interface ConfigIndex {
    val lastIndex: Int
    val lastConfig: Config?

    fun getConfigAt(index: Int): Config

    fun count(): Int

    fun removeAt(selectedIndex: Int)

    fun chooseAt(selectedIndex: Int)

    fun choose(id: Int)

    fun addConfig(newConfig: Config): Int
}

/**
 * id 和索引都是从0 开始计算
 */
class ConfigManager : ConfigIndex {
    private val configs = ArrayList<Config>()

    /**
     * 当前选中的id
     */
    private var lastId = 0

    /**
     * 最大的id。从0 开始
     */
    private var nextId = 0

    /**
     * 上一次的索引位置
     *
     * @return 如果找不到返回-1
     */
    override val lastIndex: Int
        get() = getIndex(lastId)

    override val lastConfig: Config?
        /**
         * 获取正在选中的项
         *
         * @return 正在选中的项
         */
        get() = getConfig(lastId)

    /**
     * 会自动设置id
     *
     * @param newConfig 对象
     * @return 返回生成的id
     */
    override fun addConfig(newConfig: Config): Int {
        newConfig.id = nextId
        nextId++
        configs.add(newConfig)
        return newConfig.id
    }

    override fun getConfigAt(index: Int) = configs[index]

    /**
     * @return 如果不存在，返回-1
     */
    private fun getIndex(id: Int): Int {
        for ((index, i) in configs.indices.withIndex()) {
            val config = configs[i]
            if (config.id == id) {
                return index
            }
        }
        return -1
    }

    private fun getConfig(id: Int): Config? {
        val index = getIndex(id)
        if (index == -1) return null
        return configs[index]
    }

    override fun choose(id: Int) {
        lastId = id
    }

    override fun chooseAt(selectedIndex: Int) {
        val configAt = getConfigAt(selectedIndex)
        choose(configAt.id)
    }

    override fun removeAt(selectedIndex: Int) {
        configs.removeAt(selectedIndex)
    }

    override fun count() = configs.size

    val iterator: Iterator<Config?>
        get() = configs.iterator()
}