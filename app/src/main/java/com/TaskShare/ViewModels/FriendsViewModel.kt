package com.TaskShare.ViewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.TaskShare.Models.Repositories.TSUser
import com.TaskShare.Models.Repositories.TSUsersRepository
import kotlinx.coroutines.runBlocking



class FriendsViewModel: ViewModel() {
    val state = mutableStateOf(FriendsViewState())
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
data class FriendsViewState (
    val currentUserId: String = "",
    val FriendUserId: String = "",
    val FriendEmail: String = ""
)
