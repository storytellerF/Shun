package com.storyteller_f.config_edit

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.gson.TypeAdapterFactory
import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.ConfigIndex
import com.storyteller_f.config_core.Editor
import com.storyteller_f.config_core.EditorKey
import com.storyteller_f.config_core.editor
import java.io.IOException

/**
 * 所有选中的ui 上的变动都转发给editor，然后通过editor.listener 更下下游和ui
 */
class ConfigEditor<C : Config> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), ConfigIndex {
    private val spinner: Spinner
    private val popupMenu: PopupMenu
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var editor: Editor<C>

    init {
        LayoutInflater.from(this.context).inflate(R.layout.layout_view_config, this, true)
        spinner = findViewById(R.id.config_list)
        val imageButton = findViewById<ImageButton>(R.id.more_button)
        popupMenu = PopupMenu(this.context, imageButton, Gravity.BOTTOM).apply {
            inflate(R.menu.popop_menu_config_edit_function)
            setOnMenuItemClickListener { item: MenuItem -> handleMenu(item) }
        }
        imageButton.setOnClickListener {
            showMenu()
        }
    }

    @Throws(IOException::class)
    fun init(
        name: String,
        editorListener: Editor.Listener<C>,
        vararg factory: TypeAdapterFactory?
    ) {
        val spinnerAdapter = createSpinnerAdapter()
        this.spinnerAdapter = spinnerAdapter
        editor = createEditor(name, editorListener, factory, spinnerAdapter)
        spinner.onItemSelectedListener = createSpinnerListener()
        spinner.adapter = spinnerAdapter
        spinner.setSelection(editor.lastIndex + 1)
    }

    private fun createEditor(
        name: String,
        editorListener: Editor.Listener<C>,
        factory: Array<out TypeAdapterFactory?>,
        spinnerAdapter: SpinnerAdapter
    ) = EditorKey.createEditorKey(context.filesDir.absolutePath, name)
        .editor(object : Editor.Listener<C> {
            override fun onConfigSelectedChanged(configIndex: Int, config: C?, total: Int) {
                //接受editor 的变化
                (spinnerAdapter as BaseAdapter).notifyDataSetChanged()
                spinner.setSelection(configIndex + 1)
                //向下传递
                editorListener.onConfigSelectedChanged(configIndex, config, total)
            }

            override fun onNew() = editorListener.onNew()
        }, *factory)

    private fun createSpinnerListener() =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) = editor.chooseConfigAndNotify(position)

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(TAG, "onNothingSelected() called with: parent = [$parent]")
            }
        }

    private fun createSpinnerAdapter() = object : BaseAdapter() {
        override fun getCount() = editor.count() + 1

        override fun getItem(position: Int): C? {
            return if (position == 0) null else editor.getConfigAt(position - 1) as C
        }

        override fun getItemId(position: Int): Long {
            val item: C = getItem(position) ?: return Config.NONE_ID.toLong()
            return item.id.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
            val text = view.findViewById<TextView>(android.R.id.text1)
            val item: C? = getItem(position)
            text.text = item?.name ?: "none"
            text.append(" ")
            return view
        }
    }

    private fun showMenu() {
        if (spinner.selectedItemPosition == Spinner.INVALID_POSITION) {
            Toast.makeText(context, "invalid", Toast.LENGTH_SHORT).show()
            return
        }
        popupMenu.show()
    }

    private fun handleMenu(item: MenuItem): Boolean {
        val index = spinner.selectedItemPosition
        if (index == Spinner.INVALID_POSITION) return true
        if (item.itemId == R.id.config_edit_popop_menu_rename) {
            return !rename()
        } else {
            val command = when (item.itemId) {
                R.id.config_edit_popop_menu_delete -> "delete"
                R.id.config_edit_popup_menu_new -> "new"
                R.id.config_edit_popop_menu_clone -> "clone"
                else -> return false
            }
            editor.sendCommand(command, index)
        }
        return true
    }

    private fun rename(): Boolean {
        val selectedItem = spinner.selectedItem ?: return true
        Log.i(TAG, "initTwo: " + selectedItem.javaClass)
        if (selectedItem !is Config) return true
        val selected = selectedItem as C
        val editText = EditText(context)
        editText.setText(selected.name)
        val alertDialog =
            AlertDialog.Builder(context).setMessage("请输入").setTitle("重命名").setView(editText)
                .setPositiveButton("确认", null)
                .setNegativeButton("取消") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .show()
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { v: View? ->
            val trim = editText.text.toString().trim { it <= ' ' }
            if (trim.isNotEmpty()) {
                selected.name = trim
                (spinnerAdapter as BaseAdapter).notifyDataSetChanged()
                alertDialog.dismiss()
            }
        }
        return false
    }

    companion object {
        private const val TAG = "ConfigEditor"
    }

    override val lastIndex: Int
        get() = editor.lastIndex
    override val lastConfig: Config?
        get() = editor.lastConfig

    override fun getConfigAt(index: Int) = editor.getConfigAt(index)

    override fun count(): Int = editor.count()

    override fun removeAt(selectedIndex: Int) = editor.removeAt(selectedIndex)

    override fun chooseAt(selectedIndex: Int) = editor.chooseAt(selectedIndex)

    override fun choose(id: Int) = editor.choose(id)

    override fun addConfig(newConfig: Config) = editor.addConfig(newConfig)

    /**
     * spinner 选择和点击保存时触发
     */
    fun save() = editor.save()

}