package com.TaskShare.Models.DataObjects

import com.TaskShare.Models.Utilities.TSTaskStatus
import java.util.Date

data class SubTask(
    val subTaskId : String = "",
    val taskId : String = "",
    val assigneeId : String = "",
    val startDate : Date = Date(),
    val endDate  : Date = Date(),
    val taskStatus : TSTaskStatus = TSTaskStatus.TODO,
    val comments: MutableList<String> = mutableListOf(),
    val updateLog: MutableList<UpdateLog> = mutableListOf(),
    val taskTransferAssignee: String? = null // user receiving transfer
)
