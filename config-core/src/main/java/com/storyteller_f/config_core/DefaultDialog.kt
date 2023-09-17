package com.storyteller_f.config_core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Dialog 操作Editor，Editor 操作ConfigManager
 */
abstract class DefaultDialog<C : Config, Item, O, CItem : ConfigItem>(val listener: Listener<O, CItem>) {
    /**
     * 当前选中的，与磁盘保持同步
     */
    private val active: MutableList<O> = ArrayList()
    val editing: MutableList<O> = ArrayList()
    abstract val lastConfig: C?

    private fun saveEditingToActive() {
        active.clear()
        active.addAll(editing)
        listener.onActiveChanged(active.toList())
    }

    /**
     * 本地数据发生变化，读取本地数据到变量中
     */
    fun flashEditingFromLocal(config: C?) {
        readHistoryToEditing(config)
        saveEditingToActive()
    }

    private fun readHistoryToEditing(config: C?) {
        editing.clear()
        if (config != null) {
            addAll(listener.onRestoreState(config.configItems.toList() as List<CItem>))
        }
        listener.onEditingChanged(editing.toList())
    }

    fun addToEditing(o: O) {
        add(o)
        listener.onEditingChanged(editing.toList())
    }

    private fun add(o: O) {
        if (!editing.contains(o)) editing.add(o)
    }

    private fun addAll(list: List<O>) {
        for (tFilter in list) add(tFilter)
    }

    fun replace(o: O, index: Int) {
        editing[index] = o
        listener.onEditingChanged(editing.toList())
    }

    /**
     * 保存当前修改的配置
     */
    fun saveCurrentConfig() {
        saveEditingToActive()
        saveActive()
    }

    private fun saveActive() {
        val lastConfig = lastConfig ?: return
        val configItems = listener.onSaveState(active.toList())
        lastConfig.update(configItems)
        try {
            save()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val length get() = editing.size

    abstract fun save()

    interface Listener<O, CItem : ConfigItem> {
        fun onSaveState(oList: List<O>): List<CItem>

        fun onRestoreState(configItems: List<CItem>): List<O>

        fun onActiveChanged(activeList: List<O>)

        fun onEditingChanged(editing: List<O>)
    }
}

open class SimpleDialog<C : Config, Item, O, CItem : ConfigItem>(
    listener: Listener<O, CItem>,
    val editor: Editor<C>
) : DefaultDialog<C, Item, O, CItem>(
    listener
) {
    override val lastConfig: C?
        get() = editor.lastConfig as? C

    override fun save() = editor.save()

    /**
     * 需要确保当前选中了配置，否则会出现异常。
     */
    fun sendCommand(name: String, position: Int) = editor.sendCommand(name, position)

    /**
     * @param position 0 代表未选中
     */
    fun chooseConfig(position: Int) {
        editor.chooseConfigAndNotify(position)
        flashEditingFromLocal(lastConfig)
    }


}

class WrappedDialogListener<O, CItem : ConfigItem>(
    private val l: DefaultDialog.Listener<O, CItem>,
    private val _active: MutableStateFlow<List<O>>,
    private val _editing: MutableStateFlow<List<O>>
) : DefaultDialog.Listener<O, CItem> {
    val active = _active.asStateFlow()
    val editing = _editing.asStateFlow()
    override fun onSaveState(oList: List<O>): List<CItem> {
        return l.onSaveState(oList)
    }

    override fun onRestoreState(configItems: List<CItem>): List<O> {
        return l.onRestoreState(configItems)
    }

    override fun onActiveChanged(activeList: List<O>) {
        _active.value = activeList
        l.onActiveChanged(activeList)
    }

    override fun onEditingChanged(editing: List<O>) {
        _editing.value = editing
        l.onEditingChanged(editing)
    }

}

fun <O, CItem : ConfigItem> listenerWrapper(l: DefaultDialog.Listener<O, CItem>): Pair<Pair<StateFlow<List<O>>, StateFlow<List<O>>>, WrappedDialogListener<O, CItem>> {
    val active = MutableStateFlow<List<O>>(emptyList())
    val editing = MutableStateFlow<List<O>>(emptyList())
    return (active.asStateFlow() to editing.asStateFlow()) to WrappedDialogListener(
        l,
        active,
        editing
    )
}