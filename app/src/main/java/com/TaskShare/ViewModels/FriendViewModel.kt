package com.TaskShare.ViewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.TaskShare.Models.Services.FriendsManagementService
import com.TaskShare.Models.Utilities.TSFriendStatus



class FriendViewModel: ViewModel() {
    val state = mutableStateOf(FriendViewState())
    private val friendsManagementService = FriendsManagementService()


    // Temporary vals
    var user1 = FriendViewState("Timmy Nook")
    var user2 = FriendViewState("Tommy Nook")
    var user3 = FriendViewState("Mario Mario")
    var user4 = FriendViewState("Luigi Mario")
    var user5 = FriendViewState("Bob Smith")
    var user6 = FriendViewState("Someone Something")
    var user7 = FriendViewState("Ash Ketchum")

    fun getFriends(): List<FriendViewState>{
        var friends = mutableListOf<FriendViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return friends
    }

    fun getIncomingRequests(): List<FriendViewState>{
        var incomingRequests = mutableListOf<FriendViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return incomingRequests
    }

    fun getOutgoingRequests(): List<FriendViewState>{
        var outgoingRequests = mutableListOf<FriendViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return outgoingRequests
    }

    fun getBlockedUsers(): List<FriendViewState>{
        var blockedUsers = mutableListOf<FriendViewState>(user1, user2, user3, user4,
            user5, user6, user7, user7, user7)

        return blockedUsers
    }

    fun sendFriendRequest(senderId: String, receiverEmail: String){
        state.value = state.value.copy(currentUserId = senderId)
        state.value = state.value.copy(FriendEmail = receiverEmail)

    }
    fun acceptFriendRequest(currUserId: String, friendToAccept: String){ //lines 30 - 31 are from ChatGpt
        state.value = state.value.copy(currentUserId = currUserId)
        state.value = state.value.copy(FriendUserId = friendToAccept)
    }

    fun declineOrRemoveFriendRequest(currUserId: String, friendToDecline: String){
        state.value = state.value.copy(currentUserId = currUserId)
        state.value = state.value.copy(FriendUserId = friendToDecline)
    }

    fun blockFriend(currUserId: String, friendToBeBlocked: String){
        state.value = state.value.copy(currentUserId = currUserId)
        state.value = state.value.copy(FriendUserId = friendToBeBlocked)
    }

    fun unblockFriend(currUserId: String, friendToBeUnblocked: String){
        state.value = state.value.copy(currentUserId = currUserId)
        state.value = state.value.copy(FriendUserId = friendToBeUnblocked)

    }

}
data class FriendViewState (
    val friendName: String = "",
    val currentUserId: String = "",
    val FriendUserId: String = "",
    val FriendEmail: String = ""
)
