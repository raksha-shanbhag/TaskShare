package com.TaskShare.Models.Services

import android.util.Log
import com.TaskShare.Models.DataObjects.Activity
import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.TaskUpdateStrategy.TaskUpdater
import com.TaskShare.Models.Utilities.ActivityType
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.TaskShare.ViewModels.GroupMember
import com.TaskShare.Models.Utilities.UpdateLogger
import com.TaskShare.ViewModels.TaskViewState
import java.util.Date

class TaskManagementService {
    private val groupsRepository = TSGroupsRepository()
    private val taskRepository = TSTasksRepository()
    private val subTaskRepository = TSSubTasksRepository()
    private val userRepository = TSUsersRepository()
    private val updateLogger = UpdateLogger()

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

            // get all assignees and populate list of groupmember object
            var assignees = mutableListOf<GroupMember>()
            taskInfo.assignees.forEach { assignee ->
                var assigneeInfo = userRepository.getUserInfo(assignee)
                assignees.add(GroupMember(assigneeInfo.firstName, assigneeInfo.userId))
            }

            Log.i("Debug Raksha tasks", assigneeInfo.toString())

            var task = TaskViewState(
                taskName = taskInfo.taskName,
                cycle = taskInfo.cycle,
                assignees = assignees,
                assigner = assignerInfo.firstName,
                groupName = groupInfo.groupName,
                assignee = assigneeInfo.firstName,
                status = TSTaskStatus.toDisplay(subTask.taskStatus),
                id = subTask.subTaskId,
                deadline = subTask.endDate
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
        var currAssignees = mutableListOf<String>()
        currAssignees.addAll(assignees)
        if (assignees.size <=0 ){
            currAssignees.add(assignerId)
        }

        var updateLog = updateLogger.createUpdateLogArray(assignerId = assignerId)

        var taskId = taskRepository.createTask(
            assignerId = assignerId,
            taskName = taskName,
            groupId = groupId,
            lastDate = lastDate,
            cycle = cycle,
            assignees = currAssignees,
            startDate = startDate,
            updateLog = updateLog
        )

        if (taskId.isNotEmpty()) {
            // endDate Calculation
            var taskUpdater = TaskUpdater(cycle)
            var endDate = taskUpdater.getNextEndDate(startDate)

            // create sub Tasks
            subTaskRepository.createSubTask(
                taskId = taskId,
                assigneeId = currAssignees.first(),
                startDate = startDate,
                endDate = lastDate
            )

            var groupName = groupsRepository.getGroupFromId(groupId).groupName
            ActivityManagementService.addActivity(Activity(
                taskId = taskId,
                sourceUser = assignerId,
                affectedUsers = currAssignees,
                groupId = groupId,
                type = ActivityType.TASK_ASSIGNED,
                details = "A new task has been assigned to you in ${groupName}"
            ))
        }

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

        // get all assignees and populate list of groupmember object
        var assignees = mutableListOf<GroupMember>()
        taskInfo.assignees.forEach { assignee ->
            var assigneeInfo = userRepository.getUserInfo(assignee)
            assignees.add(GroupMember(assigneeInfo.firstName, assigneeInfo.userId))
        }

        return TaskViewState(
            taskName = taskInfo.taskName,
            cycle = taskInfo.cycle,
            assignees = assignees,
            assigner = assignerInfo.firstName,
            groupName = groupInfo.groupName,
            assignee = assigneeInfo.firstName,
            status = TSTaskStatus.toDisplay(subTaskInfo.taskStatus),
            id = subTaskInfo.subTaskId,
            deadline = subTaskInfo.endDate
        )
    }

    fun updateTask(subtaskId: String, taskName: String, endDate: Date, cycle: String, newTaskStatus: String) {
        val taskStatus = TSTaskStatus.fromString(newTaskStatus)
        val taskUpdater = TaskUpdater(cycle)
        taskUpdater.updateTaskInfo(subtaskId, taskName, endDate, cycle, taskStatus)
    }
}