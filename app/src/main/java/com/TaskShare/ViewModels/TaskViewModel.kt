package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.TaskManagementService

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val detailTaskState = mutableStateOf(TaskDetail())
    private val taskManager = TaskManagementService()

    // detail task
    fun setDetailTaskInfo(taskID: String){
        detailTaskState.value = detailTaskState.value.copy(taskID = taskID)
        val taskInfo = taskManager.getTaskInfoFromId(subTaskId =  taskID)
        detailTaskState.value = detailTaskState.value.copy(taskDetail = taskInfo)
    }


    fun getDetailTaskInfo(): TaskViewState{
        // pull from backend instead later
        return detailTaskState.value.taskDetail
    }

    // TODO: integrate with backend
    fun updateAssignee(newAssignee: String){
        detailTaskState.value.taskDetail.assignee = newAssignee
    }


    // get all my task
    fun getTasksForUser(): List<TaskViewState>{
        var result = taskManager.getAllTasksForUserId(TSUsersRepository.globalUserId);
        Log.i("Debug Raksha GetTasksForUSer", result.toString())
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
    val taskName: String = "",
    val assigner: String = "",
    val status: String = "",
    val assignees: MutableList<String> = mutableListOf(),
    var assignee: String = "",
    val groupName: String = "",
    val deadline: String = "",
    val cycle: String = "",
    val id: String = ""
)