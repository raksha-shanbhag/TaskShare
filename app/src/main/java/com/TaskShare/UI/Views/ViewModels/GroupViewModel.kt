package com.TaskShare.UI.Views.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class GroupViewModel: ViewModel() {
    val state = mutableStateOf(GroupViewState())

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
}

data class GroupViewState (
    val groupName: String ="",
    val groupDescription: String = "",
    val member: String = "",
    val groupMembers: MutableList<String> = mutableListOf()
)