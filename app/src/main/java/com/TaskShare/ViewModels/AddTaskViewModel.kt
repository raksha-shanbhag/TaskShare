package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddTaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
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
}

// states
data class AddTaskState (
    val taskName: String ="",
    val assignTo: String ="",
    val assignees: MutableList<String> = mutableListOf(),
    val groupName: String = "",
    //    how are we actually storing dates in DB?
    val deadline: String = "",
    val cycle: String = ""
)