package com.TaskShare.Models.DataObjects

import com.TaskShare.Models.DataObjects.Friend

data class User(
    val userId: String = "",
    val firstName: String = "",
    val email: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val friends: MutableList<Friend> = mutableListOf(),
    val groups: MutableList<String> = mutableListOf(),
)
