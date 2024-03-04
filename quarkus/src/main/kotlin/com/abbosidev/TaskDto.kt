package com.abbosidev

import java.util.Date

data class TaskDto(
    val title: String,
    val type: String,
    val description: String,
    val dueDate: Date
)
