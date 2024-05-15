package com.storyteller_f.config_core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

class EditorKey(private val prefix: String, private val suffix: String) {
    val file get() = File(prefix, "config-editor-$suffix.json")

    companion object {
        fun createEditorKey(prefix: String, suffix: String): EditorKey {
            return EditorKey(prefix, suffix)
        }
    }
}

val editors = mutableMapOf<EditorKey, Editor<*>>()
fun <C : Config> EditorKey.editor(
    listener: Editor.Listener<C>,
    vararg factory: TypeAdapterFactory?
): Editor<C> {
    val cache = editors[this]

    @Suppress("UNCHECKED_CAST")
    return if (cache == null) {
        val gson: Gson = factory.createGson()

        if (!file.exists() && !file.createNewFile()) {
            throw Exception("${file.absolutePath} 不存在，且创建失败")
        }
        val shun = file.inputStream().use {
            gson.fromJson(InputStreamReader(it), Shun::class.java)
                ?: Shun().apply {
                    newConfig(listener)
                }
        }
        val defaultValue = Editor(this, listener, gson, shun)

        editors[this] = defaultValue
        defaultValue
    } else cache as Editor<C>
}

/**
 * 负责修改，保存configManager
 * 如果只是为了获得数据，可以直接使用这个类。如果需要UI 操作，需要使用DefaultDialog。
 */
class Editor<C : Config>(
    editorKey: EditorKey,
    private val listener: Listener<C>,
    private val gson: Gson,
    private val shun: Shun,
) : ConfigIndex by shun {
    private val file = editorKey.file

    fun save() = FileWriter(file).use {
        val output = gson.toJson(shun)
        it.write(output)
        it.flush()
    }

    init {
        listener.onConfigChanged(list())//初始化成功
    }

    /**
     * 第0个元素代表spinner 中的空选项，所以如果要真正执行操作，需要大于0
     * @param selectedIndex 大于等于0
     */
    fun sendCommand(command: String, selectedIndex: Int) {
        assert(selectedIndex >= 0)
        if (command == "new") {
            val (index, config) = shun.newConfig(listener)
            listener.onConfigChanged(list())//增加
            listener.onConfigSelectedChanged(index, config)//选中新添加的
        } else if (selectedIndex != UNSELECTED_INDEX) {
            val indexAtCore = selectedIndex - 1
            @Suppress("UNCHECKED_CAST")
            when (command) {
                "clone" -> clone(getConfigAt(indexAtCore) as C)
                "delete" -> delete(indexAtCore)
            }
        }

    }

    /**
     * @param selectedIndex 大于等于0
     */
    private fun delete(selectedIndex: Int) {
        assert(selectedIndex >= 0)
        removeAt(selectedIndex)
        listener.onConfigChanged(list())//删除
        if (count() > 0) {//如果不为空，选中第一个
            val index = 0
            val config = getConfigAt(index)
            choose(config.id)
            @Suppress("UNCHECKED_CAST")
            listener.onConfigSelectedChanged(index, config as C)//删除后更新索引
        } else {
            listener.onConfigSelectedChanged(Config.NONE_INDEX, null)
        }
    }

    private fun clone(config: C) {
        @Suppress("UNCHECKED_CAST") val clone: C = config.dup() as C
        clone.name += "克隆"
        choose(addConfig(clone))
        listener.onConfigChanged(list())//克隆后增加
        listener.onConfigSelectedChanged(lastIndex, clone)
    }

    /**
     * 因为是外部改变导致的变化，所以不会触发{@link com.storyteller_f.config_core.Listener<F>#onConfigChanged(Int, F?)}
     * 选中结果会被立刻保存
     * @param position 应该大于等于0，其中0 会被认为没有选中任何Config
     * @return 返回当前选中的配置。
     */
    private fun chooseConfig(position: Int): C? {
        assert(position >= 0)
        val config = if (position == UNSELECTED_INDEX) null else getConfigAt(position - 1)
        val i = config?.id ?: Config.NONE_ID//如果查找不到合法的id，相当于当前没有启用任何配置
        choose(i)
        save()
        @Suppress("UNCHECKED_CAST")
        return config as C?
    }

    /**
     *
     * @param position 为0代表未选中
     */
    fun chooseConfigAndNotify(position: Int) {
        assert(position >= 0)
        val chooseConfig = chooseConfig(position)
        listener.onConfigSelectedChanged(position - 1, chooseConfig)
    }

    interface Listener<F> {
        /**
         * 选中了新的Config
         *
         * @param configIndex 选中的索引,如果是-1，代表选择的是none。是Config 的索引，而不是ui 上的索引
         */
        fun onConfigSelectedChanged(configIndex: Int, config: F?)

        /**
         * 配置变化，但是不包含配置内部的配置变化。
         * 包括增加，删除，修改名称。发生在[onConfigSelectedChanged] 之前
         */
        fun onConfigChanged(list: List<Config>)

        /**
         * 创建一个新的配置
         */
        fun onNew(): F
    }

    companion object {
        /**
         * 用户选中了spinner 的第一项，spinner 的第一项被认为是没有选中任何选项
         * 对应ConfigManager 的[Config.NONE_INDEX]
         */
        const val UNSELECTED_INDEX = 0
    }
}

private fun Array<out TypeAdapterFactory?>.createGson(): Gson {
    val gsonBuilder = GsonBuilder()
    for (factory in this) {
        gsonBuilder.registerTypeAdapterFactory(factory)
    }
    return gsonBuilder.create()
}

private fun <C : Config> Shun.newConfig(listener: Editor.Listener<C>): Pair<Int, C> {
    val config = listener.onNew()
    addConfig(config)
    val index = count() - 1
    chooseAt(index)
    return index to config
}