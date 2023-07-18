package com.TaskShare.Models.DataObjects

import com.TaskShare.Models.Utilities.TSFriendStatus

data class Friend(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    var status: TSFriendStatus = TSFriendStatus.NULL
)
