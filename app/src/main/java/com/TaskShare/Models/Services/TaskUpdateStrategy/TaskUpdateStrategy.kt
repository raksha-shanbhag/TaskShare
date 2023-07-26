package com.TaskShare.Models.Services.TaskUpdateStrategy

import java.util.Date
import com.TaskShare.Models.DataObjects.Task
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Services.TaskManagementService
import java.util.Calendar

interface TaskUpdateStrategy {
    fun createNextSubtask(taskInfo: Task, endDate: Date) : Int

    fun getTomorrow(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    fun createSubTask(taskId: String, assigneeId: String, startDate: Date, endDate: Date) {
        var subTasksRepository = TSSubTasksRepository()
        var subtaskId = subTasksRepository.createSubTask(taskId, assigneeId, startDate, endDate)
        TaskManagementService().createSubTaskActivity(subtaskId)
    }

    fun getNextEndDate(startDate: Date) : Date
}