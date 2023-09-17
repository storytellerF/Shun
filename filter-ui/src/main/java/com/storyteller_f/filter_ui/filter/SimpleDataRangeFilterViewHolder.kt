package com.storyteller_f.filter_ui.filter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.storyteller_f.filter_core.Filter
import com.storyteller_f.filter_core.filter.simple.SimpleDateRangeFilter
import com.storyteller_f.filter_ui.R
import com.storyteller_f.filter_ui.adapter.FilterItemContainer
import com.storyteller_f.filter_ui.adapter.FilterItemViewHolder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

abstract class SimpleDataRangeFilterViewHolder<T>(itemView: View) :
    FilterItemViewHolder<T>(itemView) {
    private var startDate: Button
    private var endDate: Button
    private var startTime: Button
    private var endTime: Button
    private var name: TextView
    private var dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.CHINA)
    private var timeFormat = SimpleDateFormat("hh:mm:ss:SSS", Locale.CHINA)

    init {
        startDate = itemView.findViewById(R.id.date_range_start_date_button)
        endDate = itemView.findViewById(R.id.data_range_end_date_button2)
        name = itemView.findViewById(R.id.data_range_show_name_textView4)
        startTime = itemView.findViewById(R.id.date_range_start_time_button)
        endTime = itemView.findViewById(R.id.data_range_end_time_button2)
    }

    override fun bind(filterChain: Filter<T>) {
        name.text = filterChain.showName
        if (filterChain is SimpleDateRangeFilter<T>) {
            val start = filterChain.startTime
            if (start != null) {
                startDate.text = dateFormat.format(start)
                val instance = Calendar.getInstance(Locale.CHINA)
                instance.time = start
                startDate.tag = instance
                startTime.text = timeFormat.format(start)
                startTime.tag = instance
            }
            val end = filterChain.endTime
            if (end != null) {
                val instance = Calendar.getInstance(Locale.CHINA)
                instance.time = end
                endDate.text = dateFormat.format(end)
                endDate.tag = instance
                endTime.text = timeFormat.format(end)
                endTime.tag = instance
            }
        }
        @Suppress("UNCHECKED_CAST") val duplicated = filterChain.dup() as Filter<T>
        startDate.setOnClickListener { v: View ->
            dateClick(v as Button, duplicated)
        }
        startTime.setOnClickListener { v: View ->
            timeClick(v as Button, duplicated)
        }
        endDate.setOnClickListener { v: View ->
            dateClick(v as Button, duplicated)
        }
        endTime.setOnClickListener { v: View ->
            timeClick(v as Button, duplicated)
        }
    }

    private fun timeClick(v: Button, filterChain: Filter<T>) {
        val time: Calendar
        if (v.tag != null) {
            time = v.tag as Calendar
        } else {
            time = Calendar.getInstance(Locale.CHINA)
            time[Calendar.SECOND] = 0
            time[Calendar.MILLISECOND] = 0
            v.tag = time
        }
        TimePickerDialog(v.context, { _: TimePicker?, hourOfDay: Int, minute: Int ->
            time[Calendar.HOUR_OF_DAY] = hourOfDay
            time[Calendar.MINUTE] = minute
            v.text = timeFormat.format(time.time)
            if (filterChain is SimpleDateRangeFilter<T>) {
                filterChain.item.startTime = getStartTime().time
                filterChain.item.endTime = getEndTime().time
                refresh?.onChanged(v, filterChain)
            }
        }, time[Calendar.HOUR_OF_DAY], time[Calendar.MINUTE], true).show()
    }

    private fun dateClick(v: Button, filterChain: Filter<T>) {
        val date: Calendar
        if (v.tag != null) {
            date = v.tag as Calendar
        } else {
            date = Calendar.getInstance(Locale.CHINA)
            v.tag = date
        }
        DatePickerDialog(
            v.context,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                date[Calendar.YEAR] = year
                date[Calendar.MONTH] = month
                date[Calendar.DAY_OF_MONTH] = dayOfMonth
                v.text = dateFormat.format(date.time)
                if (filterChain is SimpleDateRangeFilter<T>) {
                    filterChain.item.startTime = getStartTime().time
                    filterChain.item.endTime = getEndTime().time
                    refresh?.onChanged(v, filterChain)
                }
            },
            date[Calendar.YEAR],
            date[Calendar.MONTH],
            date[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun getEndTime(): Calendar {
        return getCalendar(endDate, endTime)
    }

    private fun getCalendar(date: Button, time: Button): Calendar {
        val calendar = Calendar.getInstance(Locale.CHINA)
        val endDateCalendar =
            if (date.tag != null) date.tag as Calendar else Calendar.getInstance(
                Locale.CHINA
            )
        calendar[Calendar.YEAR] = endDateCalendar[Calendar.YEAR]
        calendar[Calendar.MONTH] = endDateCalendar[Calendar.MONTH]
        calendar[Calendar.DAY_OF_MONTH] = endDateCalendar[Calendar.DAY_OF_MONTH]
        val endTimeCalendar = if (time.tag != null) {
            time.tag as Calendar
        } else {
            Calendar.getInstance(Locale.CHINA)
        }
        calendar[Calendar.HOUR_OF_DAY] = endTimeCalendar[Calendar.HOUR_OF_DAY]
        calendar[Calendar.MINUTE] = endTimeCalendar[Calendar.MINUTE]
        return calendar
    }

    private fun getStartTime(): Calendar {
        return getCalendar(startDate, startTime)
    }

    companion object {
        @JvmStatic
        fun create(container: FilterItemContainer): View {
            val frameLayout = container.frameLayout
            val context = frameLayout.context
            LayoutInflater.from(context)
                .inflate(R.layout.item_filter_date_range, frameLayout, true)
            return container.view
        }
    }
}