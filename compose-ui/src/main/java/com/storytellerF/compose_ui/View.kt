package com.storytellerF.compose_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.storyteller_f.config_core.Core
import com.storyteller_f.filter_core.filter.simple.SimpleDateRangeFilter
import com.storyteller_f.filter_core.filter.simple.SimpleRegExpFilter
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


enum class DatePickerAt {
    start, none, end
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <O : Core, T> SimpleFilterView(
    filter: SimpleDateRangeFilter<T>,
    refresh: ItemChange<O>,
    dup: (Date?, Date?) -> O,
) {

    var showDateDialog by remember {
        mutableStateOf(DatePickerAt.none)
    }
    var showTimeDialog by remember {
        mutableStateOf(DatePickerAt.none)
    }
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.CHINA)
    val timeFormat = SimpleDateFormat("hh:mm:ss:SSS", Locale.CHINA)

    Row {
        Column {
            Text(text = filter.showName)
            Column {
                Row {
                    Button(onClick = { showDateDialog = DatePickerAt.start }) {
                        Text(text = filter.item.startTime?.let { dateFormat.format(it) }
                            ?: "not set")
                    }
                    Button(onClick = { showTimeDialog = DatePickerAt.start }) {
                        Text(text = filter.item.startTime?.let { timeFormat.format(it) }
                            ?: "not set")
                    }
                }
                Row {
                    Button(onClick = { showDateDialog = DatePickerAt.end }) {
                        Text(text = filter.item.endTime?.let { dateFormat.format(it) } ?: "not set")
                    }
                    Button(onClick = { showTimeDialog = DatePickerAt.end }) {
                        Text(text = filter.item.endTime?.let { timeFormat.format(it) } ?: "not set")
                    }
                }
            }
        }
    }

    if (showDateDialog != DatePickerAt.none) {
        val old = if (showDateDialog == DatePickerAt.start) {
            filter.item.startTime
        } else filter.item.endTime
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = old?.time)
        DatePickerDialog(onDismissRequest = { showDateDialog = DatePickerAt.none },
            confirmButton = {
                val selectedDateMillis = datePickerState.selectedDateMillis
                if (selectedDateMillis != null) {
                    val selectedCalendar = Calendar.getInstance().apply {
                        timeInMillis = selectedDateMillis
                    }
                    val oldCalendar = Calendar.getInstance().apply {
                        time = old ?: Date()
                    }
                    selectedCalendar.set(Calendar.HOUR, oldCalendar.get(Calendar.HOUR))
                    selectedCalendar.set(Calendar.MINUTE, oldCalendar.get(Calendar.MINUTE))
                    selectedCalendar.set(Calendar.SECOND, oldCalendar.get(Calendar.SECOND))
                    if (showDateDialog == DatePickerAt.start) {
                        refresh.change(dup(selectedCalendar.time, filter.item.endTime))
                    } else
                        refresh.change(dup(filter.item.startTime, selectedCalendar.time))
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimeDialog != DatePickerAt.none) {
        val old = (if (showTimeDialog == DatePickerAt.start) {
            filter.item.startTime
        } else filter.item.endTime) ?: Date()
        val calendar = Calendar.getInstance().apply {
            time = old
        }
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerState = rememberTimePickerState(initialHour = hour, initialMinute = minute)
        AlertDialog(onDismissRequest = { showTimeDialog = DatePickerAt.none }, confirmButton = {
            val hour1 = timePickerState.hour
            val minute1 = timePickerState.minute
            calendar.set(Calendar.HOUR, hour1)
            calendar.set(Calendar.MINUTE, minute1)
            if (showTimeDialog == DatePickerAt.start) {
                refresh.change(dup(calendar.time, filter.item.endTime))
            } else
                refresh.change(dup(filter.item.startTime, calendar.time))
        }, text = {
            TimePicker(state = timePickerState)
        })
    }
}

@Composable
fun <T, O : Core> SimpleFilterView(
    filter: SimpleRegExpFilter<T>,
    refresh: ItemChange<O>,
    dup: (String) -> O,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = filter.showName, modifier = Modifier.padding(8.dp))
        TextField(value = filter.regexp.orEmpty(), onValueChange = {
            val n = dup(it)
            refresh.change(n)
        }, modifier = Modifier.padding(horizontal = 8.dp))
    }
}

@Composable
fun <Item> SortView(
    chain: SortChain<Item>,
    refresh: ItemChange<SortChain<Item>>,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Text(
            text = chain.showName, modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        )
        Switch(checked = chain.item.sortDirection == SortConfigItem.up, onCheckedChange = {
            val new = chain.dup() as SortChain<Item>
            new.item.sortDirection = if (it) SortConfigItem.up else SortConfigItem.down
            refresh.change(new)
        }, modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = if (chain.item.sortDirection == SortConfigItem.up) "asc" else "desc")
    }
}