package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.FriendsManagementService
import com.TaskShare.Models.Services.GroupManagementService
import kotlinx.coroutines.runBlocking

class GroupViewModel: ViewModel() {
    val state = mutableStateOf(GroupViewState())
    val groupsState = mutableStateOf(GroupsViewState())
    private val groupManager = GroupManagementService()
    private val friendsManager = FriendsManagementService()

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
        groupManager.createGroup(
            creatorId = TSUsersRepository.globalUserId,
            groupName = groupName,
            groupDescription = groupDescription,
            groupMemberEmails = groupMembers
        )

        val newGroup = GroupViewState (
            groupName = groupName,
            groupDescription = groupDescription,
        )
        appendNewGroup(newGroup)
    }

    // view model to get all groups
    fun getAllGroupsForUser() {
        groupsState.value.groups.clear()
        var allGroups = mutableListOf<GroupViewState>()
        allGroups.addAll(groupManager.getGroupsForUserId(TSUsersRepository.globalUserId))

        for (group in allGroups) {
            appendNewGroup(group)
        }
    }

    // get rid of this. Make Frontend use only Ids or come up with new View Models?
    fun getAllGroupNames() : ArrayList<String> {
        var groupNames = ArrayList<String>()

        runBlocking {
            val allGroups = groupManager.getGroupsForUserId(TSUsersRepository.globalUserId)
            for (group in allGroups) {
                groupNames.add(group.groupName)
            }

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

    fun removeMember(){
        groupManager.removeMemberFromGroup(state.value.id, TSUsersRepository.globalUserId)
    }

    fun addMember(userID: String){
        groupManager.addMemberToGroup(state.value.id, TSUsersRepository.globalUserId)
    }

    fun getFriendsList(): List<friendItem> {
//        var listOfFriends =  friendsManager.getFriendsWithAnyStatus(
//            currentUserId = TSUsersRepository.globalUserId,
//            friendStatus = TSFriendStatus.FRIEND
//        )
//        var friends =  listOfFriends.map { friend ->
//            friendItem(friend.email, false)
//
//        }
//        return friends
//    }
        return listOf(friendItem("h33qin@uwaterloo.ca", false), friendItem("lam@lam.ca", false))
    }



    fun getFriendsNotInGroup(): List<friendItem> {
//        var listOfFriends =  friendsManager.getFriendsWithAnyStatus(
//            currentUserId = TSUsersRepository.globalUserId,
//            friendStatus = TSFriendStatus.FRIEND
//        )
//
//        var remaining = listOfFriends.filter { friend -> !state.value.groupMembers.contains(friend.email) }
//
//        var canAdd =  remaining.map { mem ->
//            friendItem(mem.email, false)
//
//        }
//
//        return canAdd
        return listOf(friendItem("h33qin@uwaterloo.ca", false), friendItem("lam@lam.ca", false))

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

data class friendItem (
    val name: String,
    val isSelected: Boolean
)