package com.TaskShare.Models.Services

import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import java.util.Date

class TaskManagementService {
    private val groupsRepository = TSGroupsRepository()
    private val taskRepository = TSTasksRepository()
    private val subTaskRepository = TSSubTasksRepository()
    private val usersRepository = TSUsersRepository()

    // API Service for creating tasks
    fun createTask(
        taskName: String,
        groupId: String,
        lastDate: Date,
        cycle: String,
        assignerId: String,
        assignees: MutableList<String>
    ) : String {
        // create main task
        var taskId = taskRepository.createTask(
            assignerId = assignerId,
            taskName = taskName,
            groupId = groupId,
            lastDate = lastDate,
            cycle = cycle
        )

        // get groupMembers
        var groupMembers = groupsRepository.getGroupMembersFromGroupId(groupId)

        // create sub Tasks
        subTaskRepository.createSubTaskForGroup(
            taskId = taskId,
            groupMemberIds = groupMembers,
            cycle = cycle,
            startDate = Date()
        )

        return taskId
    }
}