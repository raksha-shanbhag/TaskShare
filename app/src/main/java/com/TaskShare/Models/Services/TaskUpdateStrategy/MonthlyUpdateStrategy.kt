package com.TaskShare.Models.Services.TaskUpdateStrategy

import com.TaskShare.Models.DataObjects.Task
import java.util.Calendar
import java.util.Date


class MonthlyUpdateStrategy : TaskUpdateStrategy {
    private fun getOneMonthFromDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    override fun createNextSubtask(taskInfo: Task): Int {
        var index = taskInfo.currentIndex
        var startDate = getTomorrow()

        // check if start date < endDate
        if (startDate < taskInfo.lastDate){
            var endDate = getOneMonthFromDate(startDate)
            var newIndex = index + 1

            if (newIndex >= taskInfo.assignees.size) {
                newIndex = 0
            }

            var nextAssignee = taskInfo.assignees[newIndex]
            createSubTask(taskId =  taskInfo.taskId, assigneeId = nextAssignee, startDate = startDate, endDate = endDate)
            index = newIndex
        }

        return index
    }
}