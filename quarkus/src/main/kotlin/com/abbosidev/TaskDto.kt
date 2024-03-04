package com.abbosidev

import java.time.LocalDate
import java.util.Date

data class TaskDto(
    val title: String,
    val type: String,
    val description: String,
    val dueDate: LocalDate
)
