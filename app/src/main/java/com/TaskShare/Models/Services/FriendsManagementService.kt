package com.TaskShare.Models.Services

import com.TaskShare.Models.DataObjects.Friend
import com.TaskShare.Models.Utilities.TSFriendStatus
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.ViewModels.FriendViewState


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

//        var friend = FriendViewState(
//            friendName = receiverInfo.firstName + " " + receiverInfo.lastName,
//            currentUserId =  senderInfo.userId,
//            friendUserId =  receiverInfo.userId,
//            friendEmail = receiverInfo.email
//        )

        return "SUCCESS"
    }
    fun acceptFriendRequest(currentUserId: String, incomingFriendId: String){
        var currentUserInfo = usersRepository.getUserInfo(currentUserId)
        var incomingFriendInfo = usersRepository.getUserInfo(incomingFriendId)

        var ifIncomingFriendInCurrentUserList = currentUserInfo.friends.find { friend -> friend.userId == incomingFriendInfo.userId }

        if (ifIncomingFriendInCurrentUserList != null){

            var acceptorFriend = Friend(
                status = TSFriendStatus.FRIEND,
                name = incomingFriendInfo.firstName + " " + incomingFriendInfo.lastName,
                email = incomingFriendInfo.email,
                userId = incomingFriendInfo.userId
            )
            usersRepository.updateFriendshipStatus(currentUserId, incomingFriendId, acceptorFriend)
        }

        var ifCurrentUserInIncomingFriendList = incomingFriendInfo.friends.find { friend -> friend.userId == currentUserId }

        if (ifCurrentUserInIncomingFriendList!= null){

            var outgoingFriend = Friend(
                status = TSFriendStatus.FRIEND,
                name = currentUserInfo.firstName + " " + currentUserInfo.lastName,
                email = currentUserInfo.email,
                userId = currentUserId
            )

            usersRepository.updateFriendshipStatus(incomingFriendId,currentUserId, outgoingFriend)

        }
    }

    fun removeOrDeclineFriendRequest(currentUserId: String, incomingFriendId: String){
        var currentUserInfo = usersRepository.getUserInfo(currentUserId)
        var incomingFriendInfo = usersRepository.getUserInfo(incomingFriendId)

        var ifIncomingFriendInCurrentUserList = currentUserInfo.friends.find { friend -> friend.userId == incomingFriendInfo.userId }

        if (ifIncomingFriendInCurrentUserList != null){
            usersRepository.removeFriend(currentUserId, incomingFriendId)
        }
        var ifCurrentUserInIncomingFriendList = incomingFriendInfo.friends.find { friend -> friend.userId == currentUserId }

        if (ifCurrentUserInIncomingFriendList!= null){
            usersRepository.removeFriend(incomingFriendId,currentUserId)
        }

    }

    fun blockFriend(currentUserId: String, incomingFriendId: String){
        var currentUserInfo = usersRepository.getUserInfo(currentUserId)
        var incomingFriendInfo = usersRepository.getUserInfo(incomingFriendId)

        var ifIncomingFriendInCurrentUserList = currentUserInfo.friends.find { friend -> friend.userId == incomingFriendInfo.userId }

        if (ifIncomingFriendInCurrentUserList != null){
            var acceptorFriend = Friend(
                status = TSFriendStatus.BLOCKED,
                name = incomingFriendInfo.firstName + " " + incomingFriendInfo.lastName,
                email = incomingFriendInfo.email,
                userId = incomingFriendInfo.userId
            )

            usersRepository.updateFriendshipStatus(currentUserId, incomingFriendId, acceptorFriend)
        }
        var ifCurrentUserInIncomingFriendList = incomingFriendInfo.friends.find { friend -> friend.userId == currentUserId }

        if (ifCurrentUserInIncomingFriendList!= null){
            usersRepository.removeFriend(incomingFriendId,currentUserId)
        }
    }
    fun unblockFriend(currentUserId: String, blockedFriendId: String){
        var currentUserInfo = usersRepository.getUserInfo(currentUserId)
        var blockedFriendIdInfo = usersRepository.getUserInfo(blockedFriendId)

        var ifIncomingFriendInCurrentUserList = currentUserInfo.friends.find { friend -> friend.userId == blockedFriendIdInfo.userId }

        if (ifIncomingFriendInCurrentUserList != null){
            usersRepository.removeFriend(currentUserId, blockedFriendId)
        }

    }
    fun getFriendsWithAnyStatus(currentUserId: String, friendStatus: TSFriendStatus? = null): List<Friend> {
        var currentUserInfo = usersRepository.getUserInfo(currentUserId)
        var friends = currentUserInfo.friends

        return if (friendStatus != null) {
            friends.filter { it.status == friendStatus }
        } else {
            friends
        }
    }
}