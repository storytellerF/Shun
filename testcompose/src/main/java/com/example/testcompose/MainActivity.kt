package com.example.testcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.common_config.buildFilterListener
import com.example.common_config.buildFilters
import com.example.common_config.buildSortChains
import com.example.common_config.buildSortListener
import com.example.common_config.filter.DateFilter
import com.example.common_config.filter.NameFilter
import com.example.common_config.filter.PackageFilter
import com.example.common_config.filterAdapterFactory
import com.example.common_config.sortAdapterFactory
import com.example.testcompose.ui.theme.ShunTheme
import com.storytellerF.compose_ui.FilterDialog
import com.storytellerF.compose_ui.SimpleFilterView
import com.storytellerF.compose_ui.SortDialog
import com.storytellerF.compose_ui.SortView
import com.storyteller_f.config_core.listenerWrapper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShunTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    var showFilterDialog by remember {
        mutableStateOf(false)
    }
    var showSortDialog by remember {
        mutableStateOf(false)
    }


    Column {
        Button(onClick = {
            showFilterDialog = true
        }) {
            Text(text = "Filter")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Start Filter")
        }
        Button(onClick = {
            showSortDialog = true
        }) {
            Text(text = "Sort")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Start Sort")
        }
    }

    if (showFilterDialog) FilterDialog(
        "filter", {
            showFilterDialog = false
        }, listenerWrapper(buildFilterListener()).second, buildFilters(), filterAdapterFactory
    ) { filter, refresh ->
        when (filter) {
            is NameFilter -> SimpleFilterView(filter, refresh) {
                NameFilter(NameFilter.ConfigItem(it.trim()))
            }

            is PackageFilter -> SimpleFilterView(filter = filter, refresh = refresh) {
                PackageFilter(PackageFilter.ConfigItem(it.trim()))
            }

            is DateFilter -> SimpleFilterView(filter, refresh) { start, end ->
                DateFilter(DateFilter.ConfigItem(start, end))
            }
        }

    }
    if (showSortDialog) SortDialog(
        "sort",
        {
            showSortDialog = false
        },
        buildSortListener(), buildSortChains(), sortAdapterFactory,
    ) { chain, refresh ->
        SortView(chain, refresh)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShunTheme {
        Greeting()
    }
}