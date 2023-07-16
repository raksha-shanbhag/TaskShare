package com.TaskShare.Models.Services

import com.TaskShare.Models.DataObjects.Task
import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.ViewModels.GroupMember
import com.google.android.gms.tasks.Tasks
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

    suspend fun getMyTasks(groupId: String): MutableList<TaskViewState> {
        var result = mutableListOf<TaskViewState>()
        var tasks = taskRepository.getTasksForGroupId(groupId)
        for (task in tasks){

            var resultTask = TaskViewState(
                taskName = task.taskName,
                assigner = usersRepository.getUserInfo(task.assignerId).firstName,
                status = "todo", // get from subtask,
                assignees = "",
                assignee = "",
                groupName = groupsRepository.getGroupFromId(task.groupId).groupName,
                deadline = "",
                cycle = ""
            )


            result.add(resultTask)
        }


        return result

    }
}

    data class TaskViewState (
        val taskName: String = "",
        val assigner: String = "",
        val status: String = "",
        val assignees: String = "",
        val assignee: String = "",
        val groupName: String = "",
        val deadline: String = "",
        val cycle: String = "",
        val id: String = ""
    )