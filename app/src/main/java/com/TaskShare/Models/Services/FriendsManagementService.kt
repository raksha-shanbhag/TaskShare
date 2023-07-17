package com.TaskShare.Models.Services

import com.TaskShare.Models.Repositories.TSUser
import com.TaskShare.Models.Repositories.TSUsersRepository


class FriendsManagementService {
    private val usersRepository = TSUsersRepository()

    // API service for sending a friend request
    // params:- sender, receiverEmail
    // return:-
    fun sendFriendRequest(senderId: String, receiverEmail: String){
        if (senderId.isEmpty()) {
            //need to know what to return
        }
        var receiverId = usersRepository.getUserIdFromEmail(receiverEmail)
        if (receiverId.isNotEmpty()) {
            val receiver = TSUser.getFromId(receiverId)
            val newFriend =  TSUser.Friend(userId = receiverId, name = receiver.userData.firstName, status = "Pending")
            receiver.userData.friends.add(newFriend)
        }
    }

    fun acceptFriendRequest(currentUserId: String, friendToAccept: String){ //lines 30 - 31 are from ChatGpt
        var currentUser = TSUser.getFromId(currentUserId)
        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToAccept }
        friendRequest?.let { friend ->
            friend.status = "Friend"
        }

    }

    fun declineFriendRequest(currentUserId: String, friendToDecline: String){
        var currentUser = TSUser.getFromId(currentUserId)
        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToDecline }
        friendRequest?.let { friend ->
            friend.status = "Not A Friend"
        }
    }

    fun removeFriend(currentUserId: String, friendToBeRemoved: String){
        var currentUser = TSUser.getFromId(currentUserId)
        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToBeRemoved }
        friendRequest?.let { friend ->
            friend.status = "Not A Friend"
        }

    }

    fun blockFriend(currentUserId: String, friendToBeBlocked: String){
        var currentUser = TSUser.getFromId(currentUserId)
        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToBeBlocked }
        friendRequest?.let { friend ->
            friend.status = "Blocked"
        }
    }

    fun unblockFriend(currentUserId: String, friendToBeUnblocked: String){
        var currentUser = TSUser.getFromId(currentUserId)
        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToBeUnblocked }
        friendRequest?.let { friend ->
            friend.status = "Not A Friend"
        }

    }
}