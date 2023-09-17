package com.storyteller_f.shun.filter.view_holder

import android.view.View
import com.storyteller_f.filter_ui.filter.SimpleDataRangeFilterViewHolder
import com.storyteller_f.filter_ui.filter.SimpleRegExpFilterViewHolder

class DateFilterViewHolder<T>(itemView: View) : SimpleDataRangeFilterViewHolder<T>(itemView)

class NameFilterViewHolder<T>(itemView: View) : SimpleRegExpFilterViewHolder<T>(itemView)

class PackageFilterViewHolder<T>(itemView: View) : SimpleRegExpFilterViewHolder<T>(itemView)
