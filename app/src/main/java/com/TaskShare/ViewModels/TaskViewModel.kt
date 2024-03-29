package com.TaskShare.ViewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.GroupManagementService
import com.TaskShare.Models.Services.TaskManagementService
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class TaskViewModel: ViewModel() {
    val state = mutableStateOf(AddTaskState())
    val tasksState = mutableStateOf(TasksViewState())
    val detailTaskState = mutableStateOf(TaskDetail())
    private val groupManager = GroupManagementService()
    private val taskManager = TaskManagementService()


    // detail task
    fun setDetailTaskInfo(taskID: String){
        detailTaskState.value = detailTaskState.value.copy(taskID = taskID)
        val taskInfo = taskManager.getTaskInfoFromId(subTaskId =  taskID)
        detailTaskState.value = detailTaskState.value.copy(taskDetail = taskInfo)
    }


    fun getDetailTaskInfo(): TaskViewState{
        // pull from backend instead later
        return detailTaskState.value.taskDetail
    }



    fun updateTaskName(name: String) {
        var newTaskDetail = detailTaskState.value.taskDetail
        newTaskDetail.taskName = name
        detailTaskState.value = detailTaskState.value.copy(taskDetail = newTaskDetail)
    }

    fun updateCycle(cycle: String) {
        var newTaskDetail = detailTaskState.value.taskDetail
        newTaskDetail.cycle = cycle
        detailTaskState.value = detailTaskState.value.copy(taskDetail = newTaskDetail)
    }


    // get all my task
    fun getTasksForUser(): List<TaskViewState>{
        var result = taskManager.getAllTasksForUserId(TSUsersRepository.globalUserId);
        return result
    }

    fun getActiveTasksLen(): Int{
        return taskManager.getActiveTasksNumberForUserId(TSUsersRepository.globalUserId)
    }

    fun getGroupMembers() : MutableList<GroupMember> {
        if (detailTaskState.value.taskDetail.groupId == ""){
            return ArrayList<GroupMember>()
        }

        var members = groupManager.getGroupMembersFromGroupID(detailTaskState.value.taskDetail.groupId)
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

    fun transferTask(transferTo: GroupMember){
        taskManager.transferTask(detailTaskState.value.taskDetail.id,transferTo.memberId , detailTaskState.value.taskDetail.assignee.memberId)
    }

    fun deleteTask(taskID: String){
        // TODO backend: delete task endpoint
    }

    private fun getSelectedMemberIds(groupMembers: MutableList<GroupMember>) : MutableList<String> {
        var result = mutableListOf<String>()
        for (member in groupMembers) {
            if(member.selected) result.add(member.memberId)
        }

        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTask(groupMembers: MutableList<GroupMember>, taskName: String, date: String) {
        val formatter = DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.ENGLISH)
        var endDate = LocalDate.parse(date, formatter)
        var datee = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        taskManager.updateTask(
            subtaskId = detailTaskState.value.taskID,
            taskName = taskName,
            endDate = datee,
            newTaskStatus = detailTaskState.value.taskDetail.status,
            cycle = detailTaskState.value.taskDetail.cycle,
            assignees = getSelectedMemberIds(groupMembers)
        )
    }

    fun declineTransfer(){
        taskManager.declineTransfer(detailTaskState.value.taskDetail.id, TSUsersRepository.globalUserId, detailTaskState.value.taskDetail.assignee.memberId)
    }

    fun acceptTransfer(){
        taskManager.acceptTransfer(detailTaskState.value.taskDetail.id, TSUsersRepository.globalUserId, detailTaskState.value.taskDetail.assignee.memberId)
    }
}

// states

data class TasksViewState (
    val tasks: MutableList<TaskViewState> = mutableListOf()
)

data class TaskDetail(
    val taskID: String = "",
    val taskDetail: TaskViewState = TaskViewState()
)

data class TaskViewState (
    var taskName: String = "",
    val assigner: String = "",
    var status: String = "",
    val assignees: MutableList<GroupMember> = mutableListOf(),
    var assignee: GroupMember = GroupMember(),
    val groupName: String = "",
    var deadline: Date = Date(),
    var cycle: String = "",
    val id: String = "",
    val groupId: String = ""
)
