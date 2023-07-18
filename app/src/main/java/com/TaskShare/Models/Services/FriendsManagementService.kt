package com.TaskShare.Models.Services

import com.TaskShare.Models.DataObjects.Friend
import com.TaskShare.Models.Utilities.TSFriendStatus
import com.TaskShare.Models.Repositories.TSUsersRepository


class FriendsManagementService {
    private val usersRepository = TSUsersRepository()

    // API service for sending a friend request
    // params:- sender, receiverEmail
    // return:- "SUCCESS" for correct case, other have "Error" message
    fun sendFriendRequest(senderUserId: String, receiverEmail: String): String {
        var receiverInfo = usersRepository.getUserInfoFromEmail(receiverEmail)
        var senderInfo = usersRepository.getUserInfo(senderUserId)

        // if receiver is already has sender as an entry ("Blocked", "Incoming", "Outgoing", "Friend")
        var ifSenderInReceiverList = receiverInfo.friends.find { friend -> friend.userId == senderUserId }
        if (ifSenderInReceiverList != null){
            // Sends error based on status
            return TSFriendStatus.senderFriendRequestErrorFromReceiverFriendStatus(ifSenderInReceiverList.status)
        }

        // if sender had previously blocked receiver, then receiver will be in sender's list as "BLOCKED"
        var ifReceiverInSenderList = senderInfo.friends.find { friend -> friend.userId == receiverInfo.userId }
        if (ifReceiverInSenderList != null) {
            return "Please unblock user to add as Friend"
        }

        // if no entry on both sides, then create "Friend" on both sides
        var receiversFriend = Friend(
            status = TSFriendStatus.INCOMING,
            name = senderInfo.firstName + " " + senderInfo.lastName,
            email = senderInfo.email,
            userId = senderUserId
        )
        usersRepository.addFriend(receiverInfo.userId, receiversFriend)

        var sendersFriend = Friend(
            status = TSFriendStatus.OUTGOING,
            name = receiverInfo.firstName + " " + receiverInfo.lastName,
            email = receiverInfo.email,
            userId = receiverInfo.userId
        )
        usersRepository.addFriend(senderUserId, sendersFriend)

        return "SUCCESS"
    }

//        var receiverInfo = usersRepository.getUserInfoFromEmail(receiverEmail)
//
//        if()
//
//        var sendersFriend = Friend(
//            userId = receiverInfo.userId,
//            name = receiverInfo.firstName + " " + receiverInfo.lastName,
//            status = TSFriendStatus.OUTGOING
//        )
//
//        var receiversFriend = Friend(
//            userId = receiverInfo.userId
//        )

//
//        val sender = TSUser.getFromId(currentUserId)
//        var receiverId = usersRepository.getUserIdFromEmail(receiverEmail)
//        val receiver = TSUser.getFromId(receiverId)
//
//        if (currentUserId.isNotEmpty()) {
//            val senderNewFriend =  TSUser.Friend(userId = receiverId, name = receiver.userData.firstName, status = "Outgoing")
//            sender.userData.friends.add(senderNewFriend)
//        }
//
//        if (receiverId.isNotEmpty()) {
//            val newFriend =  TSUser.Friend(userId = currentUserId, name = sender.userData.firstName, status = "Incoming")
//            receiver.userData.friends.add(newFriend)
//        }

//    fun acceptFriendRequest(currentUserId: String, friendToAcceptId: String){
//
//        val acceptor = TSUser.getFromId(currentUserId)
//        var friendToAccept = TSUser.getFromId(friendToAcceptId)
//
//        if (currentUserId.isNotEmpty()) {
//            var incomingToFriend = acceptor.userData.friends.find { friend -> friend.userId == friendToAcceptId }
//            incomingToFriend?.let { friend ->
//                friend.status = "Friend"
//            }
//        }
//        if (friendToAcceptId.isNotEmpty()) {
//            var outgoingToFriend = friendToAccept.userData.friends.find{friend -> friend.userId == currentUserId}
//            outgoingToFriend?.let { friend ->
//                friend.status = "Friend"
//            }
//
//        }
//    }
//
//    fun declineFriendRequest(currentUserId: String, friendToDeclineId: String){
//        val decliner = TSUser.getFromId(currentUserId)
//        var friendToDecline = TSUser.getFromId(friendToDeclineId)
//
//        if (currentUserId.isNotEmpty()) {
//            var incomingToDeclined = decliner.userData.friends.find { friend -> friend.userId == friendToDeclineId }
//            incomingToDeclined?.let { friend ->
//                friend.status = "Declined" //delete entery
//            }
//        }
//        if (friendToDeclineId.isNotEmpty()) {
//            var outgoingToRejected= friendToDecline.userData.friends.find{friend -> friend.userId == currentUserId}
//            outgoingToRejected?.let { friend ->
//                friend.status = "Rejected"//delete entery
//            }
//        }
//    }
//
//    fun removeFriend(currentUserId: String, friendToBeRemoved: String){
//        var currentUser = TSUser.getFromId(currentUserId)
//        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToBeRemoved }
//        friendRequest?.let { friend ->
//            friend.status = "Not A Friend"
//        }
//
//    }
//
//    fun blockFriend(currentUserId: String, friendToBeBlocked: String){
//        var currentUser = TSUser.getFromId(currentUserId)
//        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToBeBlocked }
//        friendRequest?.let { friend ->
//            friend.status = "Blocked"
//        }
//    }
//
//    fun unblockFriend(currentUserId: String, friendToBeUnblocked: String){
//        var currentUser = TSUser.getFromId(currentUserId)
//        var friendRequest = currentUser.userData.friends.find { friend -> friend.userId == friendToBeUnblocked }
//        friendRequest?.let { friend ->
//            friend.status = "Not A Friend"
//        }
//
//    }
    fun showMyFriends(){

    }

    fun showMyBlockedFriends(){

    }
    fun showIncomingFriendRequests(){

    }
    fun showOutgoingFriendRequests(){

    }

}