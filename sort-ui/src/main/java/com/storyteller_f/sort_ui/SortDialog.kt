package com.storyteller_f.sort_ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.config_core.Editor
import com.storyteller_f.config_edit.ConfigEditor
import com.storyteller_f.recycleview_ui_extra.DragItemHelper
import com.storyteller_f.recycleview_ui_extra.GeneralItemDecoration
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfig
import com.storyteller_f.sort_core.config.SortConfigItem
import com.storyteller_f.sort_core.config.sortConfigAdapterFactory
import com.storyteller_f.sort_ui.adapter.SortItemAdapter
import com.storyteller_f.sort_ui.adapter.SortViewHolderFactory
import com.storyteller_f.sort_ui.choose.ChooseSortAdapter
import com.storyteller_f.sort_ui.choose.ChooseSortDialog
import com.storyteller_f.sort_ui.databinding.DialogSortBinding

class SortDialog<Item>(
    context: Context,
    name: String,
    sortChains: List<SortChain<Item>>,
    listener: Listener<SortChain<Item>, SortConfigItem>,
    factory: SortViewHolderFactory<Item>,
    adapterFactory: RuntimeTypeAdapterFactory<in ConfigItem>,
) : DefaultDialog<SortConfig, Item, SortChain<Item>, SortConfigItem>(listener) {
    private val chooseSortDialog: ChooseSortDialog<Item> = ChooseSortDialog(context, sortChains)
    private val sortItemAdapter: SortItemAdapter<Item> = SortItemAdapter(editing, factory)
    private val inflate = setupView(context)
    private val configEditor: ConfigEditor<SortConfig> = inflate.findViewById(R.id.configEditor)
    private val builder: AlertDialog.Builder =
        AlertDialog.Builder(context).setTitle("排序").setView(inflate)
            .setPositiveButton("保存更改") { _: DialogInterface?, _: Int ->
                saveCurrentConfig()
            }.setNegativeButton("关闭") { _: DialogInterface?, _: Int ->
            }
    val selfDialog: AlertDialog

    init {
        chooseSortDialog.setListener(object : ChooseSortAdapter.Listener<SortChain<Item>> {
            override fun onChoose(t: SortChain<Item>) = addToEnd(t)
        })
        selfDialog = builder.create()
        configEditor.init(name, object : Editor.Listener<SortConfig> {
            override fun onConfigSelectedChanged(
                configIndex: Int,
                config: SortConfig?,
                total: Int
            ) {
                flashEditingFromLocal(config)
                sortItemAdapter.notifyDataSetChanged()
            }

            override fun onNew(): SortConfig {
                return SortConfig.create()
            }

        }, adapterFactory, sortConfigAdapterFactory)
    }

    private fun addToEnd(t: SortChain<Item>) {
        addToEditing(t)
        sortItemAdapter.notifyItemInserted(length - 1)
    }

    private fun setupView(context: Context): View {
        val inflate = DialogSortBinding.inflate(LayoutInflater.from(context))
        inflate.addButton.setOnClickListener {
            configEditor.lastConfig
            chooseSortDialog.show()
        }
        setupRecycleView(inflate, context, inflate.root)
        return inflate.root
    }

    private fun setupRecycleView(
        inflate: DialogSortBinding,
        context: Context,
        root: CoordinatorLayout
    ) {
        val recyclerView = inflate.sortItemList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(GeneralItemDecoration(context))
            adapter = sortItemAdapter
        }

        val callback = DragItemHelper(editing, sortItemAdapter).apply {
            listener = object : DragItemHelper.Listener {
                override fun onRemoved(position: Int, removed: Any?) {
                    val filter = removed as SortChain<Item>
                    Snackbar.make(root, "remove " + filter.showName, Snackbar.LENGTH_SHORT)
                        .setAction("undo") { _: View? ->
                            editing.add(position, filter)
                            sortItemAdapter.notifyItemInserted(position)
                        }.show()
                }
            }
        }
        ItemTouchHelper(callback).apply {
            attachToRecyclerView(recyclerView)
        }
    }

    companion object;

    override val lastConfig: SortConfig?
        get() = configEditor.lastConfig as SortConfig?

    override fun save() = configEditor.save()
}
