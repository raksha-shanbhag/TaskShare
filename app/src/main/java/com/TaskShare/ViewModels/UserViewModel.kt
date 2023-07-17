package com.TaskShare.ViewModels

import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    // Temporary vals
    var user1 = UserViewState("Timmy", "Nook")
    var user2 = UserViewState("Tommy", "Nook")
    var user3 = UserViewState("Mario", "Mario")
    var user4 = UserViewState("Luigi", "Mario")
    var user5 = UserViewState("Bob", "Smith")
    var user6 = UserViewState("Someone", "Something")
    var user7 = UserViewState("Ash", "Ketchum")

    fun getFriends(): List<UserViewState>{
        var friends = mutableListOf<UserViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return friends
    }

    fun getIncomingRequests(): List<UserViewState>{
        var incomingRequests = mutableListOf<UserViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return incomingRequests
    }

    fun getOutgoingRequests(): List<UserViewState>{
        var outgoingRequests = mutableListOf<UserViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return outgoingRequests
    }

    fun getBlockedUsers(): List<UserViewState>{
        var blockedUsers = mutableListOf<UserViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return blockedUsers
    }
}

// States

data class UserViewState(
    val firstName: String = "",
    val lastName: String = ""
)
