package com.example.common_config

import java.util.Date

data class ApplicationItem(
    val packageName: String,
    var name: String,
    var installTime: Date,
    val size: Long
)