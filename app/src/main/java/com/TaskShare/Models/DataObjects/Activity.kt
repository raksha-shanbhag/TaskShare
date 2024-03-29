package com.TaskShare.Models.DataObjects

import com.TaskShare.Models.Utilities.ActivityType
import java.util.Date

data class Activity(
    val time: Date = Date(),
    val taskId: String = "",
    val groupId: String = "",
    val affectedUsers: MutableList<String> = mutableListOf(),
    val sourceUser: String = "",
    val type: ActivityType = ActivityType.NULL,
    val details: String = ""
)
