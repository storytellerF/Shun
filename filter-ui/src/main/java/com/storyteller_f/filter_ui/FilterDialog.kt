package com.storyteller_f.filter_ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.common_dialog.ChooseSortAdapter
import com.storyteller_f.common_dialog.ChooseSortDialog
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.Core
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.config_core.Editor
import com.storyteller_f.config_edit.ConfigEditor
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.config.FilterConfig
import com.storyteller_f.filter_core.config.FilterConfigItem
import com.storyteller_f.filter_core.filterConfigAdapterFactory
import com.storyteller_f.filter_ui.adapter.FilterItemAdapter
import com.storyteller_f.filter_ui.adapter.FilterViewHolderFactory
import com.storyteller_f.filter_ui.databinding.DialogFilterBinding
import com.storyteller_f.recycleview_ui_extra.DragItemHelper
import com.storyteller_f.recycleview_ui_extra.GeneralItemDecoration

class FilterDialog<Item>(
    context: Context,
    name: String,
    filters: List<Filter<Item>>,
    listener: Listener<Filter<Item>, FilterConfigItem>,
    factory: FilterViewHolderFactory<Item>,
    adapterFactory: RuntimeTypeAdapterFactory<ConfigItem>
) : DefaultDialog<FilterConfig, Item, Filter<Item>, FilterConfigItem>(listener) {
    private val chooseFilterDialog: ChooseSortDialog
    private val filterItemAdapter = FilterItemAdapter(editing, factory)
    private val filterView = setupView(context)
    private val configEditor: ConfigEditor<FilterConfig> =
        filterView.findViewById(R.id.configEditor)
    private val builder = AlertDialog.Builder(context).setTitle("筛选").setView(filterView)
        .setPositiveButton("保存更改") { _: DialogInterface?, _: Int -> saveCurrentConfig() }
        .setNegativeButton("关闭") { _: DialogInterface?, _: Int -> }
    val selfDialog: AlertDialog

    init {
        chooseFilterDialog = ChooseSortDialog(context, filters)
        chooseFilterDialog.setListener(object : ChooseSortAdapter.Listener {
            @Suppress("UNCHECKED_CAST")
            override fun onChoose(t: Core) = addToEnd(t as Filter<Item>)
        })
        selfDialog = builder.create()
        selfDialog.setOnShowListener {
            selfDialog.window!!
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }
        configEditor.init(name, object : Editor.Listener<FilterConfig> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onConfigSelectedChanged(
                configIndex: Int,
                config: FilterConfig?,
                total: Int
            ) {
                flashEditingFromLocal(config)
                filterItemAdapter.notifyDataSetChanged()
            }

            override fun onNew() = FilterConfig.create()

        }, filterConfigAdapterFactory, adapterFactory)
    }

    private fun addToEnd(t: Filter<Item>) {
        @Suppress("UNCHECKED_CAST")
        addToEditing(t.dup() as Filter<Item>)
        filterItemAdapter.notifyItemInserted(length - 1)
    }

    private fun setupView(context: Context?): View {
        val binding = DialogFilterBinding.inflate(LayoutInflater.from(context))
        binding.addButton.setOnClickListener {
            if (configEditor.lastConfig == null) return@setOnClickListener
            chooseFilterDialog.show()
        }
        setupRecycleView(binding, context, binding.root)
        return binding.root
    }

    private fun setupRecycleView(
        binding: DialogFilterBinding,
        context: Context?,
        root: CoordinatorLayout
    ) {
        val recyclerView = binding.filterItemList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(GeneralItemDecoration(context!!))
            adapter = filterItemAdapter
        }
        val callback = DragItemHelper(filterItemAdapter).apply {
            dragAndSwipe = object : DragAndSwipe {
                override fun onRemoved(position: Int) {
                    val removed = editing[position]
                    Snackbar.make(root, "remove " + removed.showName, Snackbar.LENGTH_SHORT)
                        .setAction("undo") {
                            editing.add(position, removed)
                            filterItemAdapter.notifyItemInserted(position)
                        }.show()
                }

                override fun onMoved(from: Int, to: Int) = tempMove(from, to)
            }
        }
        ItemTouchHelper(callback).apply {
            attachToRecyclerView(recyclerView)
        }
    }

    companion object {

        fun <T> filter(list: List<T>, filters: List<Filter<T>>): ArrayList<T> {
            val temp = ArrayList<T>()
            val size = filters.size
            for (t in list) {
                var i = 0
                while (i < size) {
                    if (!filters[i].filter(t)) break
                    i++
                }
                if (i == size) temp.add(t)
            }
            return temp
        }
    }

    override val lastConfig: FilterConfig?
        get() = configEditor.lastConfig as FilterConfig?

    override fun replace(c: FilterConfig) = configEditor.replaceConfig(c, configEditor.lastIndex)

    override fun save() = configEditor.save()
}