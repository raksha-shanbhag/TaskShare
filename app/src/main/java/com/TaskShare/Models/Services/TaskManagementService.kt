package com.TaskShare.Models.Services

import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.ViewModels.TaskViewState
import com.google.android.gms.tasks.Task
import java.util.Date

class TaskManagementService {
    private val groupsRepository = TSGroupsRepository()
    private val taskRepository = TSTasksRepository()
    private val subTaskRepository = TSSubTasksRepository()
    private val userRepository = TSUsersRepository()

    // API Service for getting tasks related to a user
    fun getAllTasksForUserId(userId: String): MutableList<TaskViewState> {
        var result = mutableListOf<TaskViewState>()
        var allSubtasks = subTaskRepository.getAllSubtasksForUserId(userId)

        var assigneeInfo = userRepository.getUserInfo(userId)

        for (subTask in allSubtasks) {
            // get Task info
            var taskInfo = taskRepository.getTask(subTask.taskId)

            // get group info
            var groupInfo = groupsRepository.getGroupFromId(taskInfo.groupId)

            // assigner Info
            var assignerInfo = userRepository.getUserInfo(taskInfo.assignerId)

            var task = TaskViewState(
                taskName = taskInfo.taskName,
                cycle = taskInfo.cycle,
                assignees = taskInfo.assignees,
                assigner = assignerInfo.firstName,
                groupName = groupInfo.groupName,
                assignee = assigneeInfo.firstName,
                status = subTask.taskStatus.toString(),
                id = subTask.subTaskId,
                deadline = subTask.endDate.toString()
            )
            result.add(task)

        }

        return result
    }

    // API Service for creating tasks
    fun createTask(
        taskName: String,
        groupId: String,
        lastDate: Date,
        cycle: String,
        assignerId: String,
        assignees: MutableList<String>
    ) : String {
        var startDate = Date()

        // create main task
        var curr_assignees = mutableListOf<String>()
        curr_assignees.addAll(assignees)
        if (assignees.size <=0 ){
            assignees.add(assignerId)
        }

        var taskId = taskRepository.createTask(
            assignerId = assignerId,
            taskName = taskName,
            groupId = groupId,
            lastDate = lastDate,
            cycle = cycle,
            assignees = assignees,
            startDate = startDate
        )

        // endDate Calculation
        var endDate = Date()

        // create sub Tasks
        subTaskRepository.createSubTask(
            taskId = taskId,
            assigneeId = assignees.first(),
            startDate = startDate,
            endDate = endDate
        )

        return taskId
    }

    fun getTaskInfoFromId(subTaskId: String) : TaskViewState {
        // subTask Info
        var subTaskInfo = subTaskRepository.getSubTaskInfoForId(subTaskId);

        // get Task info
        var taskInfo = taskRepository.getTask(subTaskInfo.taskId);

        // get group info
        var groupInfo = groupsRepository.getGroupFromId(taskInfo.groupId);

        // assignment users
        var assignerInfo = userRepository.getUserInfo(taskInfo.assignerId)
        var assigneeInfo = userRepository.getUserInfo(subTaskInfo.assigneeId)

        return TaskViewState(
            taskName = taskInfo.taskName,
            cycle = taskInfo.cycle,
            assignees = taskInfo.assignees,
            assigner = assignerInfo.firstName,
            groupName = groupInfo.groupName,
            assignee = assigneeInfo.firstName,
            status = subTaskInfo.taskStatus.toString(),
            id = subTaskInfo.subTaskId,
            deadline = subTaskInfo.endDate.toString(),
        )
    }
}