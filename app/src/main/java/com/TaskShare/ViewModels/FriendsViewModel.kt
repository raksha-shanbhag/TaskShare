package com.TaskShare.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel


class FriendsViewModel: ViewModel() {
    val tasksState = mutableStateOf(FriendsViewState())

    fun getFriends(): List<FriendViewState>{
        var friend1 = FriendViewState("Timmy", "Nook")
        var friend2 = FriendViewState("Tommy", "Nook")
        var friend3 = FriendViewState("Mario", "Mario")
        var friend4 = FriendViewState("Luigi", "Mario")
        var friend5 = FriendViewState("Bob", "Smith")
        var friend6 = FriendViewState("Someone", "Something")
        var friend7 = FriendViewState("Ash", "Ketchum")

        var friends = mutableListOf<FriendViewState>(friend1, friend2, friend3, friend4, friend5,
                                                    friend6, friend6, friend7)

        return friends
    }

}

// States

data class FriendsViewState(
    val friends: MutableList<FriendViewState> = mutableListOf()
)

data class FriendViewState(
    val firstName: String = "",
    val lastName: String = ""
)