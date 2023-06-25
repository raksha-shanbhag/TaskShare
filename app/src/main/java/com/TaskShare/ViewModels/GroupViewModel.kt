package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GroupViewModel: ViewModel() {
    val state = mutableStateOf(GroupViewState())
    val groupsState = mutableStateOf(GroupsViewState())
    fun updateGroupName(name: String) {
        state.value = state.value.copy(groupName = name)
    }

    fun updateGroupDesc(desc: String) {
        state.value = state.value.copy(groupDescription = desc)
    }

    fun updateGroupMember(mem: String) {
        state.value = state.value.copy(member = mem)
    }

    fun updateMembers(name: String) {
       val currentList = state.value.groupMembers
        currentList.add(name)
        state.value = state.value.copy(groupMembers = currentList)
    }

    fun getIncompleteTasks() {
        state.value.tasks.forEach{
            if(it.status != "done") {
                val currentList = state.value.incompleteTasks
                currentList.add(it)
                state.value = state.value.copy(incompleteTasks = currentList)
            }
        }
    }

    fun updateTasks(task: TaskViewState) {
        val currentList = state.value.tasks
        currentList.add(task)
        state.value = state.value.copy(tasks = currentList)
    }

    fun addGroupAndTasks(group: GroupViewState) {
        val currentList = groupsState.value.groups
        currentList.add(group)
        groupsState.value = groupsState.value.copy(groups = currentList)
    }

    fun getAssigneeTasks( id: Int): Map<String, List<TaskViewState>>{
         val currentList = groupsState.value.groups[id].tasks
         return currentList.groupBy { it.assignee }

    }
}

data class GroupViewState (
    val groupName: String ="",
    val groupDescription: String = "",
    val member: String = "",
    val groupMembers: MutableList<String> = mutableListOf(),
    val tasks: MutableList<TaskViewState> = mutableListOf(),
    val incompleteTasks: MutableList<TaskViewState> = mutableListOf()
)

data class GroupsViewState (
    val groups: MutableList<GroupViewState> = mutableListOf()
)
//
//data class TaskViewState (
//    val taskName: String ="",
//    val assignee: String = "",
//    val assigner: String = "",
//    val dueDate: String = "",
//    val status: String = ""
//)