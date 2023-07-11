package com.TaskShare.Models.DataObjects

data class Group(
    val id: String = "",
    val groupName: String ="",
    val groupDescription: String = "",
    val createdBy: String = "",
    val groupMembers: MutableList<String> = mutableListOf()
)