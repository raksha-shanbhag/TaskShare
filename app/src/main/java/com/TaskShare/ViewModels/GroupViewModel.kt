package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.TSGroup
import com.TaskShare.Models.TSGroupData
import com.TaskShare.Models.TSGroupsAPI
import com.TaskShare.Models.TSUser
import kotlinx.coroutines.runBlocking

class GroupViewModel: ViewModel() {
    val state = mutableStateOf(GroupViewState())
    val groupsState = mutableStateOf(GroupsViewState())
    private val groupAPI = TSGroupsAPI()

    val test = mutableStateOf("");
    fun updateGroupMember(mem: String) {
        state.value = state.value.copy(member = mem)
    }

    fun setGroupId(id: String) {
        state.value = state.value.copy(id = id)
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

    // view model to get a new group
    fun createGroup(groupName : String, groupDescription: String, groupMembers: MutableList<String>) {
        var userIds = mutableListOf<String>()
        for (member in groupMembers) {
            var userId = TSUser.getIdFromEmail(member)

            if (!userId.isEmpty()) {
                userIds.add(userId)
            }
        }

        val groupId = TSGroup.createGroup(
            TSGroupData(
                groupName = groupName,
                groupDescription = groupDescription,
                groupMembers = userIds
            )
        )

        for (userId in userIds) {
            TSUser.updateGroup(userId, groupId)
        }

        val newGroup = GroupViewState (
            groupName = groupName,
            groupDescription = groupDescription,
        )
        appendNewGroup(newGroup)
    }

    // view model to get all groups
    fun getAllGroups() {
        groupsState.value.groups.clear()

        runBlocking {
            TSUser.globalUser.read()
        }

        for (group in TSUser.globalUser.getGroups()) {
            appendNewGroup(group.getState())
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

    fun getAssigneeTasks( id: String): Map<String, List<TaskViewState>>{

        val group = groupsState.value.groups.find { temp -> temp.id == id }
        if(group != null){
            val currentList = group.tasks
            return currentList.groupBy { it.assignee }
        }
         return mapOf();


    }
}

data class GroupViewState (
    val groupName: String ="",
    val groupDescription: String = "",
    val groupMembers: MutableList<String> = mutableListOf(),
    val tasks: MutableList<TaskViewState> = mutableListOf(),
    val incompleteTasks: MutableList<TaskViewState> = mutableListOf(),
    val id: String = "", // Used for navigation
    val member: String = "" // Temp val for creating new groups
)

data class GroupsViewState (
    val groups: MutableList<GroupViewState> = mutableListOf()
)
