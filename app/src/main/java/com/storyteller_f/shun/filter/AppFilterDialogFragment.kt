package com.storyteller_f.shun.filter

import com.example.common_config.ApplicationItem
import com.example.common_config.buildFilterListener
import com.example.common_config.filterAdapterFactory
import com.example.common_config.filters
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.DefaultDialog
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.config.FilterConfigItem
import com.storyteller_f.filter_ui.FilterDialogFragment

class AppFilterDialogFragment : FilterDialogFragment<ApplicationItem>() {

    companion object;

    override val runtimeTypeAdapterFactory: RuntimeTypeAdapterFactory<ConfigItem>
        get() = filterAdapterFactory

    override val viewHolderFactory: ApplicationFilterFactory
        get() = ApplicationFilterFactory()

    override val dialogListener: DefaultDialog.Listener<Filter<ApplicationItem>, FilterConfigItem>
        get() = buildFilterListener()

    override val filters: List<Filter<ApplicationItem>>
        get() = filters()

}
