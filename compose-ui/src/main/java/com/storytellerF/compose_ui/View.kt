package com.storytellerF.compose_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.storyteller_f.filter_core.filter.simple.SimpleDateRangeFilter
import com.storyteller_f.filter_core.filter.simple.SimpleRegExpFilter
import com.storyteller_f.sort_core.config.SortChain
import com.storyteller_f.sort_core.config.SortConfigItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


enum class DatePickerAt {
    START, NONE, END
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SimpleFilterView(
    filter: SimpleDateRangeFilter<T>,
    updateDate: (Date?, Date?) -> Unit,
    updateName: (String) -> Unit,
) {

    var showDateDialog by remember {
        mutableStateOf(DatePickerAt.NONE)
    }
    var showTimeDialog by remember {
        mutableStateOf(DatePickerAt.NONE)
    }
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.CHINA)
    val timeFormat = SimpleDateFormat("hh:mm:ss:SSS", Locale.CHINA)

    Row {
        Column {
            CoreName(filter.item.name ?: filter.showName, updateName)
            Column {
                Row {
                    Button(onClick = { showDateDialog = DatePickerAt.START }) {
                        Text(text = filter.item.startTime?.let { dateFormat.format(it) }
                            ?: "not set")
                    }
                    Button(onClick = { showTimeDialog = DatePickerAt.START }) {
                        Text(text = filter.item.startTime?.let { timeFormat.format(it) }
                            ?: "not set")
                    }
                }
                Row {
                    Button(onClick = { showDateDialog = DatePickerAt.END }) {
                        Text(text = filter.item.endTime?.let { dateFormat.format(it) } ?: "not set")
                    }
                    Button(onClick = { showTimeDialog = DatePickerAt.END }) {
                        Text(text = filter.item.endTime?.let { timeFormat.format(it) } ?: "not set")
                    }
                }
            }
        }
    }

    if (showDateDialog != DatePickerAt.NONE) {
        val old = if (showDateDialog == DatePickerAt.START) {
            filter.item.startTime
        } else filter.item.endTime
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = old?.time)
        DatePickerDialog(onDismissRequest = { showDateDialog = DatePickerAt.NONE },
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
                    if (showDateDialog == DatePickerAt.START) {
                        updateDate(selectedCalendar.time, filter.item.endTime)
                    } else
                        updateDate(filter.item.startTime, selectedCalendar.time)
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimeDialog != DatePickerAt.NONE) {
        val old = (if (showTimeDialog == DatePickerAt.START) {
            filter.item.startTime
        } else filter.item.endTime) ?: Date()
        val calendar = Calendar.getInstance().apply {
            time = old
        }
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerState = rememberTimePickerState(initialHour = hour, initialMinute = minute)
        AlertDialog(onDismissRequest = { showTimeDialog = DatePickerAt.NONE }, confirmButton = {
            val hour1 = timePickerState.hour
            val minute1 = timePickerState.minute
            calendar.set(Calendar.HOUR, hour1)
            calendar.set(Calendar.MINUTE, minute1)
            if (showTimeDialog == DatePickerAt.START) {
                updateDate(calendar.time, filter.item.endTime)
            } else
                updateDate(filter.item.startTime, calendar.time)
        }, text = {
            TimePicker(state = timePickerState)
        })
    }
}

@Composable
private fun CoreName(
    name: String,
    updateName: (String) -> Unit
) {
    var renameText by remember {
        mutableStateOf("")
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = name)
        Image(
            imageVector = Icons.Filled.Edit,
            contentDescription = "edit name",
            modifier = Modifier
                .clickable {
                    updateName(renameText)
                }
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    if (renameText.isNotEmpty()) {
        CommonRenameDialog(renameText, { renameText = "" }) {
            updateName(it)
        }
    }
}

@Composable
fun <T> SimpleFilterView(
    filter: SimpleRegExpFilter<T>,
    updateName: (String) -> Unit,
    updateRegExp: (String) -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        CoreName(
            filter.currentName, updateName
        )
        TextField(value = filter.regexp, onValueChange = {
            updateRegExp(it)
        }, modifier = Modifier.padding(horizontal = 8.dp))
    }

}

@Composable
fun <Item> SortView(
    chain: SortChain<Item>,
    refresh: ItemChange<SortChain<Item>>,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        CoreName(name = chain.item.name ?: chain.showName, updateName = {
            @Suppress("UNCHECKED_CAST") val new = chain.dup() as SortChain<Item>
            new.item.name = it
            refresh.change(new)
        })
        Switch(checked = chain.item.sortDirection == SortConfigItem.UP, onCheckedChange = {
            @Suppress("UNCHECKED_CAST") val new = chain.dup() as SortChain<Item>
            new.item.sortDirection = if (it) SortConfigItem.UP else SortConfigItem.DOWN
            refresh.change(new)
        }, modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = if (chain.item.sortDirection == SortConfigItem.UP) "asc" else "desc")
    }
}