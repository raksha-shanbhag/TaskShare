package com.TaskShare.Models.Services.TaskUpdateStrategy

import com.TaskShare.Models.DataObjects.Task
import java.util.Date

class DailyUpdateStrategy : TaskUpdateStrategy {
    override fun getNextEndDate(date: Date) : Date {
        return getTomorrow()
    }

    override fun createNextSubtask(taskInfo: Task, endDate: Date, newAssignees: MutableList<String>): Int {
        var index = taskInfo.currentIndex
        var startDate = getTomorrow()

        // check if start date < endDate
        if (startDate <= endDate){
            var nextEndDate = getTomorrow()
            var newIndex = index + 1

            if (newIndex >= newAssignees.size) {
                newIndex = 0
            }

            var nextAssignee = newAssignees[newIndex]
            createSubTask(taskId =  taskInfo.taskId, assigneeId = nextAssignee, startDate = startDate, endDate = nextEndDate)
            index = newIndex
        }

        return index
    }
}