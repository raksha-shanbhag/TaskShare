package com.TaskShare.Models.DataObjects

import com.TaskShare.Models.Repositories.Friend

data class User(
    val firstName: String = "",
    val email: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val friends: MutableList<Friend> = mutableListOf()
)
