package com.TaskShare.ViewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.FriendsManagementService
import com.TaskShare.Models.Services.GroupManagementService
import com.TaskShare.Models.Utilities.TSFriendStatus



class FriendViewModel: ViewModel() {
    val state = mutableStateOf(FriendViewState())
    private val friendsManager = FriendsManagementService()

    fun getFriends(): List<FriendViewState>{
      var listOfFriends =  friendsManager.getFriendsWithAnyStatus(
           currentUserId = TSUsersRepository.globalUserId,
           friendStatus = TSFriendStatus.FRIEND
        )
        val listOfFriendViewState = listOfFriends.map { friend ->
            FriendViewState(
                // Convert relevant properties from Friend to FriendViewState
                // Example:
                friendName = friend.name,
                friendEmail = friend.email,
            )
        }
        return listOfFriendViewState
    }

    fun getIncomingRequests(): List<FriendViewState>{
        var listOfFriends =  friendsManager.getFriendsWithAnyStatus(
            currentUserId = TSUsersRepository.globalUserId,
            friendStatus = TSFriendStatus.INCOMING
        )
        val listOfFriendViewState = listOfFriends.map { friend ->
            FriendViewState(
                friendName = friend.name,
                friendEmail = friend.email,
            )
        }
        return listOfFriendViewState
    }

    fun getOutgoingRequests(): List<FriendViewState>{
        var listOfFriends =  friendsManager.getFriendsWithAnyStatus(
            currentUserId = TSUsersRepository.globalUserId,
            friendStatus = TSFriendStatus.OUTGOING
        )
        val listOfFriendViewState = listOfFriends.map { friend ->
            FriendViewState(
                // Convert relevant properties from Friend to FriendViewState
                // Example:
                friendName = friend.name,
                friendEmail = friend.email,
            )
        }
        return listOfFriendViewState
    }

    fun getBlockedUsers(): List<FriendViewState>{
        var listOfFriends =  friendsManager.getFriendsWithAnyStatus(
            currentUserId = TSUsersRepository.globalUserId,
            friendStatus = TSFriendStatus.BLOCKED
        )
        val listOfFriendViewState = listOfFriends.map { friend ->
            FriendViewState(
                // Convert relevant properties from Friend to FriendViewState
                // Example:
                friendName = friend.name,
                friendEmail = friend.email,
            )
        }
        return listOfFriendViewState
    }

    fun sendFriendRequest(receiverEmail: String){
        state.value = state.value.copy(friendEmail = receiverEmail)
        friendsManager.sendFriendRequest(
            senderUserId = TSUsersRepository.globalUserId,
            receiverEmail = receiverEmail)
    }

    fun acceptFriendRequest(friendToAcceptId: String){
        state.value = state.value.copy(friendUserId = friendToAcceptId)
        friendsManager.acceptFriendRequest(
            currentUserId = TSUsersRepository.globalUserId,
            incomingFriendId = friendToAcceptId)
    }

    fun declineOrRemoveFriendRequest(friendToDeclineId: String){
        state.value = state.value.copy(friendUserId = friendToDeclineId)
        friendsManager.removeOrDeclineFriendRequest(
            currentUserId = TSUsersRepository.globalUserId,
            incomingFriendId = friendToDeclineId)
    }

    fun blockFriend(friendToBeBlockedId: String){
        state.value = state.value.copy(friendUserId = friendToBeBlockedId)
        friendsManager.blockFriend(
            currentUserId = TSUsersRepository.globalUserId,
            incomingFriendId = friendToBeBlockedId)
    }

    fun unblockFriend(friendToBeUnblockedId: String){
        state.value = state.value.copy(friendUserId = friendToBeUnblockedId)
        friendsManager.unblockFriend(
            currentUserId = TSUsersRepository.globalUserId,
            blockedFriendId = friendToBeUnblockedId)
    }

}
data class FriendViewState (
    val friendName: String = "",
    val currentUserId: String = "",
    val friendUserId: String = "",
    val friendEmail: String = ""
)
