package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.TSSubTask
import com.TaskShare.Models.TSSubTaskData
import com.TaskShare.Models.TSTask
import com.TaskShare.Models.TSTaskData
import com.TaskShare.Models.TSUser

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

    fun createTask(){
        // validate form data
        // call POST endpoint to send data to backend
        var taskId = TSTask.createTask(
            TSTaskData(
                TSUser.globalUser.getGroups()[0].id,
                state.value.taskName,
                TSUser.globalUser.id,
                state.value.assignees,
                mutableListOf(),
                state.value.cycle
            )
        )
        TSUser.globalUser.getGroups()[0].updateTask(taskId)

        TSSubTask.createSubTask(
            TSSubTaskData(
                taskId,
                state.value.assignees[0],
                comments = mutableListOf("test", "comment")
            )
        )
    }
}

// states
data class AddTaskState (
    val taskName: String ="",
    val assignTo: String ="",
    val assignees: MutableList<String> = mutableListOf(),
    val groupName: String = "Select Group Name",
    //    how are we actually storing dates in DB?
    val deadline: String = "",
    val cycle: String = "Select Cycle"
)