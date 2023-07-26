package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.GroupManagementService
import com.TaskShare.Models.Services.TaskManagementService
import com.TaskShare.Models.Services.TaskUpdateStrategy.TaskUpdater
import com.TaskShare.Models.Utilities.TSTaskStatus
import java.util.Date

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val detailTaskState = mutableStateOf(TaskDetail())
    private val groupManager = GroupManagementService()
    private val taskManager = TaskManagementService()
    private val taskUpdater = TaskUpdater("No Cycle") // default value,updated


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

    fun getActiveTasksLen(): Int{
        // todo: Backend
        return 0
    }

    fun getGroupMembers() : MutableList<GroupMember> {
        if (detailTaskState.value.taskDetail.groupId == ""){
            return ArrayList<GroupMember>()
        }

        var members = groupManager.getGroupMembersFromGroupID(detailTaskState.value.taskDetail.groupId)
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

    fun deleteTask(taskID: String){
        // TODO backend: delete task endpoint
    }

    fun testEditTask(){
        Log.i("Debug J ", detailTaskState.value.toString())

        Log.i("Debug J ", "Calling Edit")
        taskUpdater.updateTaskInfo(detailTaskState.value.taskDetail.id, "Task Updated", detailTaskState.value.taskDetail.deadline, detailTaskState.value.taskDetail.cycle, TSTaskStatus.fromString(detailTaskState.value.taskDetail.status))
        Log.i("Debug J ", "Completed Edit")
        Log.i("Debug J ", detailTaskState.value.toString())
    }
}

// statest

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
    var status: String = "",
    val assignees: MutableList<GroupMember> = mutableListOf(),
    var assignee: String = "",
    val groupName: String = "",
    var deadline: Date = Date(),
    var cycle: String = "",
    val id: String = "",
    val groupId: String = ""
)
