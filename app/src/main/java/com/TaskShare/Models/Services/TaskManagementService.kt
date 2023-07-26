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

            // get group info
            var groupInfo = groupsRepository.getGroupFromId(taskInfo.groupId)

            // assigner Info
            var assignerInfo = userRepository.getUserInfo(taskInfo.assignerId)

            // get all assignees and populate list of group member object
            var assignees = mutableListOf<GroupMember>()
            taskInfo.assignees.forEach { assignee ->
                var assigneeInfo = userRepository.getUserInfo(assignee)
                assignees.add(GroupMember(assigneeInfo.firstName, assigneeInfo.userId))
            }

            var task = TaskViewState(
                taskName = taskInfo.taskName,
                cycle = taskInfo.cycle,
                assignees = assignees,
                assigner = assignerInfo.firstName,
                groupName = groupInfo.groupName,
                assignee = GroupMember(assigneeInfo.firstName, assigneeInfo.userId),
                status = TSTaskStatus.toDisplay(subTask.taskStatus),
                id = subTask.subTaskId,
                groupId = groupInfo.id,
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
            var subtaskId = subTaskRepository.createSubTask(
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
                details = "You are invited to participate in the task ${taskName} from ${groupName}"
            ))

            createSubTaskActivity(subtaskId)
        }

        return taskId
    }

    fun createSubTaskActivity(subtaskId: String, sourceUser:String = "", destUser:String = "", type: String = "created") {
        var subtask = subTaskRepository.getSubTaskInfoForId(subtaskId)
        var task = taskRepository.getTask(subtask.taskId)

        var source = if (sourceUser.isEmpty()) task.assignerId else sourceUser
        var dest = if (destUser.isEmpty()) mutableListOf(subtask.assigneeId) else mutableListOf(destUser)
        var details = when (type) {
            "created" -> "A new instance of ${task.taskName} has been assigned to you"
            "updated" -> "The details of the task ${task.taskName} has been updated"
            "due" -> "The task ${task.taskName} is due soon"
            "overdue" -> "The task ${task.taskName} is overdue!"
            "requested" -> "A new instance of ${task.taskName} has been assigned to you"
            "accepted" -> "Your transfer request for ${task.taskName} has been accepted"
            "declined" -> "Your transfer request for ${task.taskName} has been declined"
            else -> {"Error"}
        }

        var activityType = when (type) {
            "created" -> ActivityType.TASK_ASSIGNED
            "requested", "accepted", "declined" -> ActivityType.TASK_TRANSFER
            "updated" -> ActivityType.TASK_CHANGED
            "due", "overdue" -> ActivityType.TASK_DUE
            else -> ActivityType.NULL
        }

        ActivityManagementService.addActivity(Activity(
            taskId = subtaskId,
            sourceUser = source,
            affectedUsers = dest,
            groupId = task.groupId,
            type = activityType,
            details = details
        ))
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
            assignee = GroupMember(assigneeInfo.firstName, assigneeInfo.userId),
            status = TSTaskStatus.toDisplay(subTaskInfo.taskStatus),
            id = subTaskInfo.subTaskId,
            groupId = groupInfo.id,
            deadline = subTaskInfo.endDate
        )
    }

    fun updateTask(subtaskId: String, taskName: String, endDate: Date, cycle: String, newTaskStatus: String, assignees: MutableList<String>) {
        val taskStatus = TSTaskStatus.fromString(newTaskStatus)
        val taskUpdater = TaskUpdater(cycle)
        taskUpdater.updateTaskInfo(subtaskId = subtaskId, taskName = taskName, endDate = endDate, cycle = cycle, assignees = assignees, newTaskStatus = taskStatus)
        createSubTaskActivity(subtaskId = subtaskId, sourceUser = TSUsersRepository.globalUserId, type = "updated")
    }

    // Params - transferToUserId is the user who received the task transfer
    // assigneeId is current assignee
    fun transferTask(subtaskId: String, transferToUserId: String, assigneeId: String) {
        // change task status to "Transfer Requested" and create new field for subtaskId
        subTaskRepository.updateSubtaskTransfer(
            subtaskId = subtaskId,
            assigneeId = assigneeId,
            taskTransferAssignee =  transferToUserId,
            status = TSTaskStatus.TRANSFER
        )
        createSubTaskActivity(subtaskId = subtaskId, sourceUser = assigneeId, destUser = transferToUserId, type = "requested")
    }

    // Params - transferToUserId is the user who received the task transfer
    // assigneeId is current assignee
    fun acceptTransfer(subtaskId: String, transferToUserId: String, assigneeId: String) {
        subTaskRepository.updateSubtaskTransfer(
            subtaskId = subtaskId,
            assigneeId = transferToUserId,
            taskTransferAssignee =  null,
            status = TSTaskStatus.TODO
        )
        createSubTaskActivity(subtaskId = subtaskId, sourceUser = transferToUserId, destUser = assigneeId, type = "accepted")
    }


    // Params - transferToUserId is the user who received the task transfer
    // assigneeId is current assignee
    fun declineTransfer(subtaskId: String, transferToUserId: String, assigneeId: String) {
        subTaskRepository.updateSubtaskTransfer(
            subtaskId = subtaskId,
            assigneeId = assigneeId,
            taskTransferAssignee =  null,
            status = TSTaskStatus.TODO
        )
        createSubTaskActivity(subtaskId = subtaskId, sourceUser = transferToUserId, destUser = assigneeId, type = "declined")
    }

    fun getActiveTasksNumberForUserId(userId: String) : Int {
        return subTaskRepository.getActiveTasksNumber(userId = userId)
    }
}