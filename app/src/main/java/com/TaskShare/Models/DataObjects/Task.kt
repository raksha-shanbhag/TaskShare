package com.TaskShare.Models.DataObjects

import java.time.LocalDate
import java.util.Date

data class Task(
    val taskId: String = "",
    val taskName: String = "",
    val cycle: String = "",
    val groupId: String = "",
    val assignerId: String = "",
    val lastDate: Date = Date(),
    val startDate: Date = Date()
)
