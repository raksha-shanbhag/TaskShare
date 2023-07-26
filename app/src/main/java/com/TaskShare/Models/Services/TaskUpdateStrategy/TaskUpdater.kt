package com.TaskShare.Models.Services.TaskUpdateStrategy

import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Services.TaskManagementService
import com.TaskShare.Models.Utilities.TSTaskStatus
import java.util.Date

class TaskUpdater {
    var taskUpdateStrategy: TaskUpdateStrategy = DailyUpdateStrategy()
    var taskRepository = TSTasksRepository()
    var subTaskRepository = TSSubTasksRepository()

    constructor(cycle: String)  {
        when (cycle) {
            "Daily" -> {
                this.taskUpdateStrategy = DailyUpdateStrategy()
            }
            "Weekly" -> {
                this.taskUpdateStrategy = WeeklyUpdateStrategy()
            }
            "Monthly" -> {
                this.taskUpdateStrategy = MonthlyUpdateStrategy()
            }
        }
    }

    fun updateTaskInfo(subtaskId: String, taskName: String, endDate: Date, cycle: String, newTaskStatus: TSTaskStatus) {
        // update task
        var currentSubTaskInfo = subTaskRepository.getSubTaskInfoForId(subtaskId)
        var currentMainTaskInfo = taskRepository.getTask(currentSubTaskInfo.taskId)
        var index = currentMainTaskInfo.currentIndex

        // check update date
        var updateDate = endDate
        if (endDate < currentMainTaskInfo.lastDate) {
            updateDate = Date()
        }

        // update subTask status if not the below conditions
        if (!(currentSubTaskInfo.taskStatus == TSTaskStatus.OVERDUE && newTaskStatus == TSTaskStatus.IN_PROGRESS) ) {
            subTaskRepository.updateSubTaskStatus(subtaskId = subtaskId, newStatus = newTaskStatus)
            TaskManagementService().createSubTaskActivity(subtaskId = subtaskId, type = "overdue")
        }

        // create sub task if previous status was overdue and current is complete,
        // or if current is declined for next person
        // if cycle is not no-cycle
        if ( cycle != "No Cycle" &&
            (newTaskStatus == TSTaskStatus.DECLINED ||
            (currentSubTaskInfo.taskStatus == TSTaskStatus.OVERDUE && newTaskStatus == TSTaskStatus.COMPLETE )
        )){
            // change index and create next task
            index = taskUpdateStrategy.createNextSubtask(currentMainTaskInfo, endDate = updateDate)

        }

        // update task and rotation Index
        taskRepository.updateTaskInfo(taskId = currentSubTaskInfo.taskId, taskName = taskName, endDate = endDate, cycle = cycle, updateIndex = index)
    }

    fun getNextEndDate(startDate: Date) : Date {
        return taskUpdateStrategy.getNextEndDate(startDate = startDate)
    }

}