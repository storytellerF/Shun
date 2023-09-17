package com.storyteller_f.shun

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common_config.buildFilter
import com.example.common_config.buildSortChain
import com.example.common_config.filterAdapterFactory
import com.example.common_config.sortAdapterFactory
import com.storyteller_f.config_core.EditorKey
import com.storyteller_f.config_core.editor
import com.storyteller_f.filter_core.config.FilterConfig
import com.storyteller_f.filter_core.config.FilterConfigItem
import com.storyteller_f.filter_core.filterConfigAdapterFactory
import com.storyteller_f.filter_ui.FilterDialog.Companion.filter
import com.storyteller_f.shun.filter.AppFilterDialogFragment
import com.storyteller_f.shun.sort.AppSortDialogFragment
import com.storyteller_f.sort_core.config.SortChains
import com.storyteller_f.sort_core.config.SortConfig
import com.storyteller_f.sort_core.config.SortConfigItem
import java.io.File
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val origin: MutableList<com.example.common_config.ApplicationItem> = ArrayList()
    private lateinit var applicationAdapter: ApplicationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendar = Calendar.getInstance(Locale.CHINA)
        val installedApplications = packageManager.getInstalledApplications(0)
        for (installedApplication in installedApplications) {
            val e = installedApplication.getApplicationItem(calendar)
            origin.add(e)
        }
        sortEvent()
        filterEvent()
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        applicationAdapter = ApplicationAdapter()
        recyclerView.adapter = applicationAdapter
    }

    private fun ApplicationInfo.getApplicationItem(
        calendar: Calendar
    ): com.example.common_config.ApplicationItem {
        val applicationLabel = packageManager.getApplicationLabel(this)
        try {
            val firstInstallTime = packageManager.getPackageInfo(packageName, 0).firstInstallTime
            calendar.timeInMillis = firstInstallTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            calendar.timeInMillis = 0
        }
        val installTime = calendar.time
        val length = File(sourceDir).length()
        return com.example.common_config.ApplicationItem(
            packageName, applicationLabel.toString(), installTime, length
        )
    }

    private fun filterEvent() {
        findViewById<Button>(R.id.show_filter_dialog).setOnClickListener {
            AppFilterDialogFragment().show(supportFragmentManager, "filter")
        }
        findViewById<Button>(R.id.start_filter).setOnClickListener {
            sortFilter()
        }
    }

    private fun sortFilter() {
        val createEditorKey = EditorKey.createEditorKey(filesDir.absolutePath, "filter")
        val filters = createEditorKey.editor(
            FilterConfig.emptyFilterListener,
            filterConfigAdapterFactory,
            filterAdapterFactory
        ).lastConfig?.run {

            configItems.filterIsInstance<FilterConfigItem>().buildFilter
        } ?: return
        val filter = filter(origin, filters)
        applicationAdapter.submitList(filter)
    }

    private fun sortEvent() {
        findViewById<Button>(R.id.show_sort_dialog).setOnClickListener {
            AppSortDialogFragment().show(supportFragmentManager, "sort")
        }
        findViewById<Button>(R.id.start_sort).setOnClickListener {
            startSort()
        }
    }

    private fun startSort() {
        val createEditorKey = EditorKey.createEditorKey(filesDir.absolutePath, "sort")
        val sortChains = createEditorKey.editor(
            SortConfig.emptySortListener,
            filterConfigAdapterFactory,
            sortAdapterFactory
        ).lastConfig?.run {
            configItems.filterIsInstance<SortConfigItem>().buildSortChain
        } ?: return

        applicationAdapter.submitList(origin.sortedWith(SortChains(sortChains)))
    }

    companion object;
}