package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.TaskManagementService

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val detailTaskState = mutableStateOf(TaskDetail())
    val taskManager = TaskManagementService()
    val globalUserId = TSUsersRepository.globalUserId

    // detail task
    fun setDetailTaskInfo(taskID: String){
        detailTaskState.value = detailTaskState.value.copy(taskID = taskID)
        // pull from backend instead later
//         val taskInfo = taskManager.getTaskByID(taskID)
        val taskInfo = TaskViewState("Clean Kitchen", "Lamia", "inprogress", mutableListOf("Cheng", "John", "Jaishree", "Lamia"), "Jaishree", "Roommates", "14/12/2022", "Monthly", "")
        detailTaskState.value = detailTaskState.value.copy(taskDetail = taskInfo)
    }


    fun getDetailTaskInfo(): TaskViewState{
        // pull from backend instead later
        return detailTaskState.value.taskDetail

    }

    // get all my task
    fun getTasksForUser(): List<TaskViewState>{
        return taskManager.getAllTasksForUserId(globalUserId);
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
    val assignee: String = "",
    val groupName: String = "",
    val deadline: String = "",
    val cycle: String = "",
    val id: String = ""
)