package com.TaskShare.Models.Services

import com.TaskShare.Models.DataObjects.Activity
import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Utilities.ActivityType
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.TaskShare.ViewModels.GroupViewState
import com.TaskShare.ViewModels.TaskViewState
import com.TaskShare.ViewModels.GroupMember

class GroupManagementService {
    private val groupsRepository = TSGroupsRepository()
    private val tasksRepository = TSTasksRepository()
    private val subTasksRepository = TSSubTasksRepository()
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

        if (!groupMemberIds.contains(creatorId)) {
            groupMemberIds.add(creatorId)
        }

        var newGroupId = groupsRepository.createGroup(creatorId, groupName, groupDescription, groupMemberIds)

        if(newGroupId.isNotEmpty()) {
            usersRepository.addGroupToUserId(creatorId, newGroupId)
            var sourceInfo = usersRepository.getUserInfo(creatorId)

            ActivityManagementService.addActivity(Activity(
                groupId = newGroupId,
                sourceUser = creatorId,
                affectedUsers = groupMemberIds,
                type = ActivityType.GROUP_REQUEST,
                details = "${sourceInfo.firstName} ${sourceInfo.lastName} added you to the group ${groupName}!"
            ))
        }
        return newGroupId
    }

    // API service for my groups page with GroupsViewModel
    // params:-  userId : string
    // return:-  groups: MutableList<GroupViewModel>
    fun getGroupsForUserId(userId: String): MutableList<GroupViewState>
    {
        if (userId.isEmpty()) {
            return mutableListOf()
        }

        var groupIds = usersRepository.getGroupsForUserId(userId)
        var result = mutableListOf<GroupViewState>()

        for (groupId in groupIds){
            // get group information
            var groupInfo = groupsRepository.getGroupFromId(groupId)

            // get Group tasks information
            var groupTasks = getAllTasksForGroupId(groupId = groupId)

            result.add(
                GroupViewState(
                    id = groupId,
                    groupName = groupInfo.groupName,
                    groupDescription = groupInfo.groupDescription,
                    groupMembers = groupInfo.groupMembers,
                    tasks = groupTasks
                )
            )
        }

        return result.toMutableList()
    }

    fun getGroupMembersFromGroupID(groupId: String): MutableList<GroupMember> {
        var result = mutableListOf<GroupMember>()
        var groupMemberIds = groupsRepository.getGroupMembersFromGroupId(groupId)

        for (memberId in groupMemberIds){
            var userInfo = usersRepository.getUserInfo(memberId)

            var groupMember = GroupMember(
                memberId = memberId,
                memberName = userInfo.firstName
            )
            result.add(groupMember)
        }

        return result
    }

    // API Service for getting tasks for a group
    fun getAllTasksForGroupId(groupId: String): MutableList<TaskViewState> {
        var result = mutableListOf<TaskViewState>()
        var groupTasks = tasksRepository.getTasksForGroupId(groupId = groupId)
        for (task in groupTasks){
            var allSubTasks = subTasksRepository.getAllSubtasksForTaskId(taskId = task.taskId)
            for (subTask in allSubTasks) {
                // get group info
                var groupInfo = groupsRepository.getGroupFromId(groupId)

                // assigner Info
                var assignerInfo = usersRepository.getUserInfo(task.assignerId)
                var assigneeInfo = usersRepository.getUserInfo(task.assignerId)

                // get all assignees and populate list of groupmember object
                var assignees = mutableListOf<GroupMember>()
                task.assignees.forEach { assignee ->
                    var assigneeInfo = usersRepository.getUserInfo(assignee)
                    assignees.add(GroupMember(assigneeInfo.firstName, assigneeInfo.userId))
                }

                var task = TaskViewState(
                    taskName = task.taskName,
                    cycle = task.cycle,
                    assignees = assignees,
                    assigner = assignerInfo.firstName,
                    groupName = groupInfo.groupName,
                    assignee = assigneeInfo.firstName,
                    status = TSTaskStatus.toDisplay(subTask.taskStatus),
                    id = subTask.subTaskId,
                    deadline = subTask.endDate
                )
                result.add(task)

            }
        }
        return result
    }

    // API to add a new group Member
    fun addMemberToGroup(groupId: String, memberId: String) {
        // update group doc array
        groupsRepository.addMemberToGroup(groupId = groupId, newMemberUserId = memberId)

        // update user doc array
        usersRepository.addGroupToUserId(userId = memberId, groupId = groupId)
    }

    // API to remove member from a group
    fun removeMemberFromGroup(groupId: String, memberId: String) {
        // update group doc array
        groupsRepository.removeUserFromGroup(groupId = groupId, memberId = memberId)

        // remove members from tasks assigned to the group
        var groupTasks = tasksRepository.getTasksForGroupId(groupId = groupId)
        for(task in groupTasks) {
            var checkAssignee = task.assignees.find { assignee -> assignee == memberId}
            if (checkAssignee != null) {
                tasksRepository.removeAssigneeFromTask(taskId = task.taskId, assigneeId = checkAssignee)
            }
        }

        // update user doc array
        usersRepository.removeGroupForUserId(userId = memberId, groupId = groupId)
    }
}
