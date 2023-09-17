package com.storyteller_f.filter_ui

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.config.FilterConfigItem
import com.storyteller_f.filter_ui.adapter.FilterViewHolderFactory
import java.io.IOException

abstract class FilterDialogFragment<T> : DialogFragment() {

    abstract val runtimeTypeAdapterFactory: RuntimeTypeAdapterFactory<ConfigItem>

    abstract val viewHolderFactory: FilterViewHolderFactory<T>

    abstract val dialogListener: DefaultDialog.Listener<Filter<T>, FilterConfigItem>

    abstract val filters: List<Filter<T>>

    override fun onCreateDialog(savedInstanceState: Bundle?) = setupFilterFunction().selfDialog

    @Throws(IOException::class)
    private fun setupFilterFunction() = FilterDialog(
        requireContext(), "filter", filters, dialogListener, viewHolderFactory, runtimeTypeAdapterFactory
    )

}