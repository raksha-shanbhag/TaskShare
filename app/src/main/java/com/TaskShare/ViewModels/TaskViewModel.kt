package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Services.TaskManagementService
import com.example.greetingcard.screens.RenderTaskCard
import java.util.Date

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val detailTaskState = mutableStateOf(TaskDetail())

    val taskManager = TaskManagementService()



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
    fun getTasks(): List<TaskViewState>{
        var task1 = TaskViewState("Clean Counters", "Jaishree", "inprogress", mutableListOf("Jaishree", "John"), "Lamia", "Roommates", "09/14/2022", "No Cycle", "")
        var task2 = TaskViewState("Clean Room", "Jaishree", "done", mutableListOf("Jaishree"), "Lamia", "Roommates 3A", "24/10/2022", "No Cycle", "")
        var task3 = TaskViewState("Clean Bathroom", "Cheng", "todo", mutableListOf("Cheng", "John", "Jaishree", "Lamia"), "Lamia", "Roommates", "01/14/2022", "Daily", "")
        var task4 = TaskViewState("Clean Kitchen", "Lamia", "inprogress", mutableListOf("Lamia", "John"), "Jaishree", "Roommates", "14/12/2022", "Monthly", "")

        return mutableListOf<TaskViewState>(task1, task2, task3, task4)

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