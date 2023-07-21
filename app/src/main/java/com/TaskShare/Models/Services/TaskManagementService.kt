package com.TaskShare.Models.Services

import android.util.Log
import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Utilities.TSTaskStatus
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
            Log.i("Debug Raksha tasks", taskInfo.toString())

            // get group info
            var groupInfo = groupsRepository.getGroupFromId(taskInfo.groupId)
            Log.i("Debug Raksha tasks", groupInfo.toString())

            // assigner Info
            var assignerInfo = userRepository.getUserInfo(taskInfo.assignerId)
            Log.i("Debug Raksha tasks", assigneeInfo.toString())

            var task = TaskViewState(
                taskName = taskInfo.taskName,
                cycle = taskInfo.cycle,
                assignees = taskInfo.assignees,
                assigner = assignerInfo.firstName,
                groupName = groupInfo.groupName,
                assignee = assigneeInfo.firstName,
                status = TSTaskStatus.toDisplay(subTask.taskStatus),
                id = subTask.subTaskId,
                deadline = taskInfo.lastDate
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

        Log.i("Debug raksha assignees- ", assignees.toString())
        // create main task
        var curr_assignees = mutableListOf<String>()
        curr_assignees.addAll(assignees)
        if (assignees.size <=0 ){
            curr_assignees.add(assignerId)
        }

        var taskId = taskRepository.createTask(
            assignerId = assignerId,
            taskName = taskName,
            groupId = groupId,
            lastDate = lastDate,
            cycle = cycle,
            assignees = curr_assignees,
            startDate = startDate
        )



        Log.i("Debug raksha assignees curr", curr_assignees.toString())

        // create sub Tasks
        subTaskRepository.createSubTask(
            taskId = taskId,
            assigneeId = curr_assignees.first(),
            startDate = startDate,
            endDate = lastDate
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

        Log.i("Debug Task", taskInfo.toString())
        Log.i("Debug Task", subTaskInfo.toString())

        return TaskViewState(
            taskName = taskInfo.taskName,
            cycle = taskInfo.cycle,
            assignees = taskInfo.assignees,
            assigner = assignerInfo.firstName,
            groupName = groupInfo.groupName,
            assignee = assigneeInfo.firstName,
            status = TSTaskStatus.toDisplay(subTaskInfo.taskStatus),
            id = subTaskInfo.subTaskId,
            deadline = taskInfo.lastDate
        )
    }
}