package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.TSGroupsAPI
import kotlinx.coroutines.runBlocking

class GroupViewModel: ViewModel() {
    val state = mutableStateOf(GroupViewState())
    val groupsState = mutableStateOf(GroupsViewState())
    private val groupAPI = TSGroupsAPI()

    val test = mutableStateOf("");

    fun setTest(str: String) {
        test.value = str;
    }

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

    fun updateGroupId(id: String) {
        state.value = state.value.copy(id = id);
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

    // view model to get a new group
    fun createGroup(groupName : String, groupDescription: String, groupMembers: MutableList<String>) {
        Log.i("Raksha debug", groupName)
        val groupId = groupAPI.createGroup(groupName, groupDescription, groupMembers)
        val newGroup = GroupViewState (
            groupName = groupName,
            groupDescription = groupDescription,
            id = groupId
        )
        appendNewGroup(newGroup)
    }

    // view model to get all groups
    fun getAllGroups() {
        runBlocking {
            val allGroups = groupAPI.getAllGroups()
            Log.i("Debugging Raksha old", allGroups.toString())
            for (group in allGroups) {
                appendNewGroup(
                    GroupViewState(
                        id = group.id,
                        groupName = group.groupName,
                        groupDescription = group.groupDescription,
                        groupMembers = group.groupMembers
                    )
                )
            }
        }
    }

    fun getAllGroupNames() : ArrayList<String> {
        var groupNames = ArrayList<String>()

        runBlocking {
            val allGroups = groupAPI.getAllGroups()
            for (group in allGroups) {
                groupNames.add(group.groupName)
            }
            Log.i("Debugging Raksha new", allGroups.toString())

        }

        return groupNames
    }

    fun appendNewGroup(group: GroupViewState) {
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
    val incompleteTasks: MutableList<TaskViewState> = mutableListOf(),
    val id: String = ""
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