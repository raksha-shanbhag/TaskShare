package com.TaskShare.Models.Services

import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.ViewModels.GroupViewState
import com.TaskShare.ViewModels.TaskViewState

class GroupManagementService {
    private val groupsRepository = TSGroupsRepository()
    private val taskRepository = TSTasksRepository()
    private val subTaskRepository = TSSubTasksRepository()
    private val usersRepository = TSUsersRepository()

    // API service for
    // params:- groupName, groupDescription, groupMemberEmails: Array<string>()
    // return:- groupId: string
    fun createGroup(
        creatorId: String,
        groupName: String,
        groupDescription: String,
        groupMemberEmails: MutableList<String>
    ) : String {
        var groupMemberIds = usersRepository.getUserIdsFromEmails(groupMemberEmails)
        groupMemberIds.add(creatorId)

        var result = groupsRepository.createGroup(creatorId, groupName, groupDescription, groupMemberIds)
        return result
    }

    // API service for my groups page with GroupsViewModel
    // params:-  userId : string
    // return:-  groups: MutableList<GroupViewModel>
    fun getGroupsForUserId(userId: String): MutableList<GroupViewState>
    {
        var groupIds = usersRepository.getGroupsForUserId(userId)
        var result = mutableListOf<GroupViewState>()

        for (groupId in groupIds){
            // get group information
            var groupInfo = groupsRepository.getGroupFromId(groupId)

            // get Group tasks information
            var groupTasks = emptyList<TaskViewState>()

            result.add(
                GroupViewState(
                    id = groupId,
                    groupName = groupInfo.groupName,
                    groupDescription = groupInfo.groupDescription,
                    groupMembers = groupInfo.groupMembers,
                    tasks = groupTasks.toMutableList()
                )
            )
        }

        return result.toMutableList()
    }
}