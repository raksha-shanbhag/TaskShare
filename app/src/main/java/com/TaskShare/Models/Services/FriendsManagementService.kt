package com.TaskShare.Models.Services

import com.TaskShare.Models.Repositories.TSUser
import com.TaskShare.Models.Repositories.TSUsersRepository


class FriendsManagementService {
    private val usersRepository = TSUsersRepository()

    // API service for sending a friend request
    // params:- sender, receiverEmail
    // return:-
    fun sendFriendRequest(currentUserId: String, receiverEmail: String){

        val sender = TSUser.getFromId(currentUserId)
        var receiverId = usersRepository.getUserIdFromEmail(receiverEmail)
        val receiver = TSUser.getFromId(receiverId)

        if (currentUserId.isNotEmpty()) {
            val senderNewFriend =  TSUser.Friend(userId = receiverId, name = receiver.userData.firstName, status = "Outgoing")
            sender.userData.friends.add(senderNewFriend)
        }

        if (receiverId.isNotEmpty()) {
            val newFriend =  TSUser.Friend(userId = currentUserId, name = sender.userData.firstName, status = "Incoming")
            receiver.userData.friends.add(newFriend)
        }
    }

    fun acceptFriendRequest(currentUserId: String, friendToAcceptId: String){

        val acceptor = TSUser.getFromId(currentUserId)
        var friendToAccept = TSUser.getFromId(friendToAcceptId)

        if (currentUserId.isNotEmpty()) {
            var incomingToFriend = acceptor.userData.friends.find { friend -> friend.userId == friendToAcceptId }
            incomingToFriend?.let { friend ->
                friend.status = "Friend"
            }
        }
        if (friendToAcceptId.isNotEmpty()) {
            var outgoingToFriend = friendToAccept.userData.friends.find{friend -> friend.userId == currentUserId}
            outgoingToFriend?.let { friend ->
                friend.status = "Friend"
            }

        }
    }

    fun declineFriendRequest(currentUserId: String, friendToDeclineId: String){
        val decliner = TSUser.getFromId(currentUserId)
        var friendToDecline = TSUser.getFromId(friendToDeclineId)

        if (currentUserId.isNotEmpty()) {
            var incomingToDeclined = decliner.userData.friends.find { friend -> friend.userId == friendToDeclineId }
            incomingToDeclined?.let { friend ->
                friend.status = "Declined" //delete entery
            }
        }
        if (friendToDeclineId.isNotEmpty()) {
            var outgoingToRejected= friendToDecline.userData.friends.find{friend -> friend.userId == currentUserId}
            outgoingToRejected?.let { friend ->
                friend.status = "Rejected"//delete entery
            }
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
    fun showMyFriends(){

    }

    fun showMyBlockedFriends(){

    }
    fun showIncomingFriendRequests(){

    }
    fun showOutgoingFriendRequests(){

    }

}