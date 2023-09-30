package com.storyteller_f.shun.sort

import com.example.common_config.ApplicationItem
import com.example.common_config.sortChains
import com.example.common_config.buildSortListener
import com.example.common_config.sortAdapterFactory
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem
import com.storyteller_f.sort_ui.SortDialogFragment
import com.storyteller_f.sort_ui.adapter.SortViewHolderFactory

class AppSortDialogFragment : SortDialogFragment<ApplicationItem>() {
    override val dialogListener: DefaultDialog.Listener<SortChain<ApplicationItem>, SortConfigItem>
        get() = buildSortListener()

    override val sortChains: List<SortChain<ApplicationItem>>
        get() = sortChains()

    override val viewHolderFactory: SortViewHolderFactory<ApplicationItem>
        get() = ApplicationSortFactory()

    override val runtimeTypeAdapterFactory: RuntimeTypeAdapterFactory<ConfigItem>
        get() = sortAdapterFactory

}
