package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.DataObjects.Group
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.GroupManagementService
import com.TaskShare.Models.Services.TaskManagementService
import java.util.Date

//import com.TaskShare.Models.TSSubTask
//import com.TaskShare.Models.TSSubTaskData
//import com.TaskShare.Models.TSTask
//import com.TaskShare.Models.TSTaskData
//import com.TaskShare.Models.TSUser

class AddTaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val groupManager = GroupManagementService()
    val taskManager = TaskManagementService()

// updates for tasks
    fun updateTaskName(name: String) {
        state.value = state.value.copy(taskName = name)
    }

    fun updateTaskGroup(taskGroup: GroupData) {
        state.value = state.value.copy(groupName = taskGroup.groupName)
        state.value = state.value.copy(groupId = taskGroup.groupId)
    }

    fun updateAssignee(selectedMember: GroupMember) {
        state.value = state.value.copy(assignee = selectedMember)
    }


    fun updateDeadline(date: String) {
        var endDate = Date(date)
        state.value = state.value.copy(endDate = endDate)
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

    fun createTask() {
        // validate form data
        // call POST endpoint to send data to backend
        taskManager.createTask(
            taskName = state.value.taskName,
            groupId = state.value.groupId,
            lastDate = state.value.endDate,
            cycle = state.value.cycle,
            assignerId = TSUsersRepository.globalUserId,
            assignees = state.value.assignees
        )

        Log.i("Debug Raksha Frontend ViewModel", state.value.assignees.toString())
    }

//    fun createTask(){
//        // validate form data
//        // call POST endpoint to send data to backend
//        var taskId = TSTask.createTask(
//            TSTaskData(
//                TSUser.globalUser.getGroups()[0].id,
//                state.value.taskName,
//                TSUser.globalUser.id,
//                state.value.assignees,
//                mutableListOf(),
//                state.value.cycle
//            )
//        )
//        TSUser.globalUser.getGroups()[0].updateTask(taskId)
//
//        TSSubTask.createSubTask(
//            TSSubTaskData(
//                taskId,
//                state.value.assignees[0],
//                comments = mutableListOf("test", "comment")
//            )
//        )
//    }

    fun getAllGroupsForUser() : MutableList<GroupData> {
        var groups = groupManager.getGroupsForUserId(TSUsersRepository.globalUserId)
        var result = ArrayList<GroupData>()

        for (group in groups) {
            result.add(
                GroupData(
                    groupName = group.groupName,
                    groupId = group.id
                )
            )
        }

        return result
    }

    fun getGroupMembers() : MutableList<GroupMember> {
        if (state.value.groupId == ""){
            return ArrayList<GroupMember>()
        }

        var members = groupManager.getGroupMembersFromGroupID(state.value.groupId)
        var result = mutableListOf<GroupMember>()

        for (member in members) {
            result.add(
                GroupMember(
                    memberName = member.memberName,
                    memberId = member.memberId,
                    selected = false
                )
            )
        }
        return result
    }


}



// states
data class AddTaskState (
    var taskName: String ="",
    var groupName: String = "Select Group Name",
    val groupMembers: MutableList<GroupMember> = mutableListOf(),
    val assignTo: String = "",
    var assignees: MutableList<String> = mutableListOf(),
    val assignee: GroupMember = GroupMember(),
    var endDate: Date = Date(),
    var cycle: String = "Select Cycle",
    val groupId: String = ""

)

data class GroupData (
    val groupName: String = "",
    val groupId: String = ""
)

data class GroupMember (
    var memberName: String = "",
    val memberId: String = "",
    var selected: Boolean = true
){
    fun toggle(){
        selected=!selected
    }
}