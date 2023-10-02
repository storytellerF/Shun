package com.storytellerF.compose_ui

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.TypeAdapterFactory
import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.Core
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.config_core.Editor
import com.storyteller_f.config_core.EditorKey
import com.storyteller_f.config_core.SimpleDialog
import com.storyteller_f.config_core.WrappedDialogListener
import com.storyteller_f.config_core.editor
import com.storyteller_f.config_core.listenerWrapper
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.config.FilterConfig
import com.storyteller_f.filter_core.filterConfigAdapterFactory
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfig
import com.storyteller_f.sort_core.config.sortConfigAdapterFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

fun interface ItemChange<O> {
    fun change(new: O)
}

@Composable
fun <Item, CItem : ConfigItem> FilterDialog(
    suffix: String,
    close: () -> Unit,
    listener: DefaultDialog.Listener<Filter<Item>, CItem>,
    filters: List<Filter<Item>>,
    factory: TypeAdapterFactory?,
    content: @Composable (Filter<Item>, ItemChange<Filter<Item>>) -> Unit
) {
    val flow = MutableSharedFlow<Unit>()
    val scope = rememberCoroutineScope()
    AlertDialog(onDismissRequest = close, title = {
        Text("Filter")
    }, text = {
        ConfigEditor<FilterConfig, Item, Filter<Item>, CItem>(suffix, listener, {
            FilterConfig.create()
        }, filters, flow, content, filterConfigAdapterFactory, factory)
    }, confirmButton = {
        Button(onClick = {
            scope.launch {
                flow.emit(Unit)
            }
        }) {
            Text(text = "Save")
        }
    }, dismissButton = {
        Button(onClick = close) {
            Text(text = "Close")
        }
    })
}

@Composable
fun <Item, CItem : ConfigItem> SortDialog(
    suffix: String,
    close: () -> Unit,
    listener: DefaultDialog.Listener<SortChain<Item>, CItem>,
    filters: List<SortChain<Item>>,
    factory: TypeAdapterFactory?,
    content: @Composable (SortChain<Item>, ItemChange<SortChain<Item>>) -> Unit
) {
    val flow = MutableSharedFlow<Unit>()
    val scope = rememberCoroutineScope()
    AlertDialog(onDismissRequest = close, title = {
        Text("Sort")
    }, text = {
        ConfigEditor<SortConfig, Item, SortChain<Item>, CItem>(suffix, listener, {
            SortConfig.create()
        }, filters, flow, content, sortConfigAdapterFactory, factory)
    }, confirmButton = {
        Button(onClick = {
            scope.launch {
                flow.emit(Unit)
            }
        }) {
            Text(text = "Save")
        }
    }, dismissButton = {
        Button(onClick = close) {
            Text(text = "Close")
        }
    })
}

@Composable
fun <C : Config, Item, O : Core, CItem : ConfigItem> ConfigEditor(
    suffix: String,
    listener: DefaultDialog.Listener<O, CItem>,
    createNew: () -> C,
    filters: List<O>,
    flow: MutableSharedFlow<Unit>,
    content: @Composable (O, ItemChange<O>) -> Unit,
    vararg factory: TypeAdapterFactory?
) {
    val context = LocalContext.current
    val wrapped by remember {
        derivedStateOf {
            if (listener is WrappedDialogListener) {
                (listener.active to listener.editing) to listener
            } else {
                listenerWrapper(listener)
            }
        }
    }
    val state = wrapped.first
    val safeListener = wrapped.second

    val editingList by state.second.collectAsState()
    var currentSelected by remember {
        mutableStateOf<C?>(null)
    }
    var count by remember {
        mutableIntStateOf(0)
    }
    var currentConfigIndex by remember {
        mutableIntStateOf(0)
    }
    val simpleDialog by simpleDialogState<C, CItem, Item, O>(
        context, suffix,
        object : Editor.Listener<C> {
            override fun onConfigSelectedChanged(configIndex: Int, config: C?, total: Int) {
                currentSelected = config
                currentConfigIndex = configIndex
                count = total
            }

            override fun onNew() = createNew()

        },
        safeListener,
        *factory,
    )
    val scope = rememberCoroutineScope()
    val draggingState = rememberReorderableLazyListState(onMove = { from, to ->
        simpleDialog?.tempMove(from.index, to.index)
    })
    Column {
        Header(simpleDialog, currentSelected, count, chooseConfig = {
            simpleDialog?.chooseConfig(it)
        }) { name: String ->
            simpleDialog?.sendCommand(name, currentConfigIndex + 1)
            Unit
        }
        AddFunction(filters) {
            simpleDialog?.addToEditing(it)
        }

        LazyColumn(
            modifier = Modifier
                .height(120.dp)
                .reorderable(draggingState).detectReorderAfterLongPress(draggingState),
            state = draggingState.listState
        ) {
            items(editingList.size, key = {
                editingList[it].id
            }) { index ->
                val data = editingList[index]
                val refresh = ItemChange<O> {
                    simpleDialog?.replace(it, index)
                }

                ReorderableItem(reorderableState = draggingState, key = data.id) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 1.dp else 0.dp, label = "ele")
                    Box(modifier = Modifier.shadow(elevation = elevation.value)) {
                        content(data, refresh)
                    }
                }

            }
        }
    }

    DisposableEffect(flow) {
        val launch = scope.launch {
            flow.collectLatest {
                simpleDialog?.saveCurrentConfig()
            }
        }
        onDispose {
            launch.cancel()
        }
    }

}

@Composable
private fun <C : Config, CItem : ConfigItem, Item, O : Core> simpleDialogState(
    context: Context,
    suffix: String,
    editorListener: Editor.Listener<C>,
    safeListener: WrappedDialogListener<O, CItem>,
    vararg factory: TypeAdapterFactory?,
) = produceState<SimpleDialog<C, Item, O, CItem>?>(initialValue = null) {
    val editor = EditorKey.createEditorKey(context.filesDir.absolutePath, suffix)
        .editor(editorListener, *factory)
    value =
        SimpleDialog<C, Item, O, CItem>(
            safeListener, editor
        ).apply {
            //恢复上一次的选中
            chooseConfig(editor.lastIndex + 1)
        }
}

@Composable
private fun <C : Config, CItem : ConfigItem, Item, O : Core> Header(
    simpleDialog: SimpleDialog<C, Item, O, CItem>?,
    lastConfig: C?,
    count: Int,
    chooseConfig: (Int) -> Unit,
    sendCommand: (String) -> Unit
) {
    var showMenu by remember {
        mutableStateOf(false)
    }
    var showSpinner by remember {
        mutableStateOf(false)
    }
    val sendCommandAndClose = { it: String ->
        sendCommand(it)
        showMenu = false
    }
    val chooseAndHideSpinner = { i: Int ->
        chooseConfig(i)
        showSpinner = false
    }
    var renameText by remember {
        mutableStateOf("")
    }
    var showRenameDialog by remember {
        mutableStateOf(false)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = {
            showSpinner = true
        }, modifier = Modifier.weight(1f)) {
            Text(
                lastConfig?.name ?: "unknown"
            )
        }
        DropdownMenu(expanded = showSpinner, onDismissRequest = {
            showSpinner = false
        }) {
            Text(text = "none", modifier = Modifier
                .padding(8.dp)
                .clickable {
                    chooseAndHideSpinner(0)
                })
            repeat(count) {
                val configAt = simpleDialog?.editor?.getConfigAt(it)
                Text(text = configAt?.name.toString(), modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        chooseAndHideSpinner(it + 1)
                    })
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            showMenu = true
        }) {
            Image(imageVector = Icons.Default.Menu, contentDescription = "menu")
        }
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(text = {
                Text(text = "new")
            }, onClick = { sendCommandAndClose("new") })
            DropdownMenuItem(
                text = { Text(text = "clone") },
                onClick = { sendCommandAndClose("clone") })
            DropdownMenuItem(text = { Text(text = "rename") }, onClick = {
                showMenu = false
                if (lastConfig != null) {
                    renameText = lastConfig.name
                    showRenameDialog = true
                }
            })
            DropdownMenuItem(
                text = { Text(text = "delete") },
                onClick = { sendCommandAndClose("delete") })
            DropdownMenuItem(text = { Text(text = "import") }, onClick = { /*TODO*/ })
            DropdownMenuItem(text = { Text(text = "output") }, onClick = { /*TODO*/ })
        }
    }
    if (showRenameDialog) {
        AlertDialog(onDismissRequest = {
            showRenameDialog = false
        }, confirmButton = {
            Button(onClick = {
                lastConfig?.name = renameText
                showRenameDialog = false
            }) {
                Text(text = "ok")
            }

        }, dismissButton = {
            Button(onClick = { showRenameDialog = false }) {
                Text(text = "cancel")
            }
        }, title = {
            Text(text = "rename")
        }, text = {
            TextField(value = renameText, onValueChange = {
                renameText = it
            })
        })
    }


}

@Composable
private fun <O : Core> AddFunction(
    filters: List<O>,
    addCore: (O) -> Unit
) {
    Row {
        var showFilterMenu by remember {
            mutableStateOf(false)
        }
        Button(onClick = {
            showFilterMenu = true
        }) {
            Text(text = "add")
        }

        DropdownMenu(expanded = showFilterMenu, onDismissRequest = { showFilterMenu = false }) {
            filters.forEach {
                DropdownMenuItem(text = { Text(text = it.showName) }, onClick = {
                    @Suppress("UNCHECKED_CAST")
                    addCore(it.dup() as O)
                    showFilterMenu = false
                })
            }
        }
    }
}

