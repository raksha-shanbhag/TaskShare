package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.GroupManagementService
import com.TaskShare.Models.Services.TaskManagementService
import java.util.Date

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val detailTaskState = mutableStateOf(TaskDetail())
    private val groupManager = GroupManagementService()
    private val taskManager = TaskManagementService()


    // detail task
    fun setDetailTaskInfo(taskID: String){
        detailTaskState.value = detailTaskState.value.copy(taskID = taskID)
        val taskInfo = taskManager.getTaskInfoFromId(subTaskId =  taskID)
        Log.i("Debug Raksha TaskInfo", taskInfo.toString())
        detailTaskState.value = detailTaskState.value.copy(taskDetail = taskInfo)
        Log.i("Debug Raksha TaskInfo detail", detailTaskState.value.taskDetail.toString())
    }


    fun getDetailTaskInfo(): TaskViewState{
        // pull from backend instead later
        return detailTaskState.value.taskDetail
    }

    // TODO: integrate with backend
    fun updateAssignee(newAssignee: String){
        detailTaskState.value.taskDetail.assignee = newAssignee
    }


    fun updateTaskName(name: String) {
        var newTaskDetail = detailTaskState.value.taskDetail
        newTaskDetail.taskName = name
        detailTaskState.value = detailTaskState.value.copy(taskDetail = newTaskDetail)
    }

    fun updateCycle(cycle: String) {
        var newTaskDetail = detailTaskState.value.taskDetail
        newTaskDetail.cycle = cycle
        detailTaskState.value = detailTaskState.value.copy(taskDetail = newTaskDetail)
    }


    // get all my task
    fun getTasksForUser(): List<TaskViewState>{
        var result = taskManager.getAllTasksForUserId(TSUsersRepository.globalUserId);
        Log.i("Debug Raksha GetTasksForUSer", result.toString())
        return result
    }

    fun getGroupMembers() : MutableList<GroupMember> {
        if (state.value.groupId == ""){
            return ArrayList<GroupMember>()
        }

        var members = groupManager.getGroupMembersFromGroupID(state.value.groupId)
        var result = mutableListOf<GroupMember>()

        for (member in members) {
            result.add(
                GroupMember(
                    memberName = member.memberName,
                    memberId = member.memberId,
                    selected = false
                )
            )
        }
        return result
    }
}

// states

data class TasksViewState (
    val tasks: MutableList<TaskViewState> = mutableListOf()
)

data class TaskDetail(
    val taskID: String = "",
    val taskDetail: TaskViewState = TaskViewState()
)

data class TaskViewState (
    var taskName: String = "",
    val assigner: String = "",
    val status: String = "",
    val assignees: MutableList<String> = mutableListOf(),
    var assignee: String = "",
    val groupName: String = "",
    var deadline: Date = Date(),
    var cycle: String = "",
    val id: String = ""
)