package com.storyteller_f.sort_ui

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem
import com.storyteller_f.sort_ui.adapter.SortViewHolderFactory
import java.io.IOException

abstract class SortDialogFragment<T : Any> : DialogFragment() {
    abstract val dialogListener: DefaultDialog.Listener<SortChain<T>, SortConfigItem>

    abstract val sortChains: List<SortChain<T>>

    abstract val viewHolderFactory: SortViewHolderFactory<T>

    abstract val runtimeTypeAdapterFactory: RuntimeTypeAdapterFactory<ConfigItem>

    override fun onCreateDialog(savedInstanceState: Bundle?) = setupSortDialog().selfDialog

    @Throws(IOException::class)
    private fun setupSortDialog() = SortDialog(
        requireContext(),
        "sort",
        sortChains,
        dialogListener,
        viewHolderFactory,
        runtimeTypeAdapterFactory
    )
}