package com.TaskShare.Models.DataObjects

data class Friend(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    var status: TSFriendStatus = TSFriendStatus.NULL
)
