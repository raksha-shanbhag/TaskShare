package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val dummyTask1 = mutableStateOf(TaskViewState()) // remove this after integration

    // updates for tasks
    fun updateTaskName(name: String) {
        state.value = state.value.copy(taskName = name)
    }

    fun updateGroupName(groupName: String) {
        state.value = state.value.copy(groupName = groupName)
    }

    fun updateDeadline(date: String) {
        state.value = state.value.copy(deadline = date)
    }

    fun updateCycle(cycle: String) {
        state.value = state.value.copy(cycle = cycle)
    }

    fun updateAssignTo(assignTo: String) {
        state.value = state.value.copy(assignTo = assignTo)
    }
    fun updateAssignees(name: String) {
        val currentList = state.value.assignees
        currentList.add(name)
        state.value = state.value.copy(assignees = currentList)
    }


    // remove this function after integration
    fun initTask( name: String, assigner: String, status: String, assignee: String, groupName: String, deadline: String, cycle: String){
        dummyTask1.value = dummyTask1.value.copy(taskName = name)
        dummyTask1.value = dummyTask1.value.copy(assigner = assigner)
        dummyTask1.value = dummyTask1.value.copy(status = status)
        dummyTask1.value = dummyTask1.value.copy(assignee = assignee)
        dummyTask1.value = dummyTask1.value.copy(groupName = groupName)
        dummyTask1.value = dummyTask1.value.copy(deadline = deadline)
        dummyTask1.value = dummyTask1.value.copy(cycle = cycle)
        tasksState.value.tasks.add(dummyTask1.value)
    }


    // update for integration: pass in id
    fun getTaskByID(): TaskViewState{
        // replace this with get request to get task info by id
        return dummyTask1.value
    }

    fun getTasks(): List<TaskViewState>{
        val currentList = tasksState.value.tasks
        return currentList
    }
}

// states

data class TasksViewState (
    val tasks: MutableList<TaskViewState> = mutableListOf()
)

data class TaskViewState (
    val taskName: String ="",
    val assigner: String ="",
    val status: String ="",
    val assignee: String="",
    val groupName: String = "",
    val deadline: String = "",
    val cycle: String = ""
)