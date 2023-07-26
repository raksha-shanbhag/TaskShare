package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.Friend
import com.TaskShare.Models.DataObjects.User
import com.TaskShare.Models.Services.NotificationsService
import com.TaskShare.Models.Utilities.TSFriendStatus
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class TSUsersRepository() {
    private val TAG = "TSUserApi"
    val db = Firebase.firestore
    val users = db.collection("Users")

    companion object {
        var globalUserId: String = ""
        var setNotifTokenOnLogin: Boolean = false

        fun setNotifToken(userId: String) {
            runBlocking {
                var token = FirebaseMessaging.getInstance().token.await()
                Firebase.firestore.collection("Users").document(userId).update("notifToken", token).await()
            }
        }

        fun setNotifEnabled(userId: String, enabled: Boolean) {
            runBlocking {
                Firebase.firestore.collection("Users").document(userId).update("notifEnabled", enabled).await()
                NotificationsService.notifEnabled = enabled
            }
        }

        fun isNotifEnabled(userId: String): Boolean {
            var enabled = false

            runBlocking {
                var document = Firebase.firestore.collection("Users").document(userId).get().await()
                enabled = document.get("notifEnabled") as Boolean??: false
            }

            return enabled
        }

        fun register(data: User) {
            val ref = Firebase.firestore.collection("Users").document(data.userId)

            runBlocking {
                ref.set(data).await()
            }
        }
    }

    // get UserId from Email
    fun getUserIdFromEmail(email: String): String {
        var result = ""
        runBlocking {
            var documentSnapshot = users.whereEqualTo("email", email).get().await()
            for (document in documentSnapshot.documents) {
                result = document.id
                break
            }
        }
        return result
    }

    // Temporary API - get userInfo from email (we'll just use userID)
    fun getUserInfoFromEmail(email: String): User {
        var result = User()
        runBlocking {
            var documentSnapshot = users.whereEqualTo("email", email).get().await()
            for (document in documentSnapshot.documents) {
                result = User(
                    userId = document.id,
                    firstName = document.get("firstName").toString(),
                    lastName = document.get("lastName").toString(),
                    email = document.get("email").toString(),
                    phoneNumber = document.get("phoneNumber").toString(),
                    friends = parseFriendsList(document.get("friends"))
                )
//                break
            }
        }
        return result
    }

    // temporary API
    fun getUserIdsFromEmails(emails: MutableList<String>) : MutableList<String> {
        var result = HashSet<String>()
        for (email in emails) {
            var userId = getUserIdFromEmail(email)
            if(userId != "") {
                result.add(userId)
            }
        }
        return result.toMutableList()
    }

    // get Groups for a given userId
    fun getGroupsForUserId(userId: String): MutableList<String> {
        var result = ArrayList<String>()
        runBlocking {
            var document  = users.document(userId).get().await()
            if (document.exists()) {
                try {
                    var groups = document.get("groups") as MutableList<String>
                    result.addAll(groups)
                } catch(e: Throwable) {
                    Log.w("Can't find group", e)
                }
            }
        }

        return result.toMutableList()
    }

    fun getUserInfo(userId: String): User {
        var userInfo = User()
        runBlocking {
            var result = users.document(userId).get().await()
            if (result.exists()) {
                userInfo = User(
                    userId = userId,
                    firstName = result.get("firstName").toString(),
                    lastName = result.get("lastName").toString(),
                    email = result.get("email").toString(),
                    phoneNumber = result.get("phoneNumber").toString(),
                    friends = parseFriendsList(result.get("friends"))
                )
            }
        }
        return userInfo
    }
    private fun parseFriendsList(friendsList: Any?): MutableList<Friend> {
        val parsedFriends: MutableList<Friend> = mutableListOf()
        if (friendsList is List<*>) {
            for (friend in friendsList) {
                if (friend is Map<*, *>) {
                    val userId = friend["userId"].toString()
                    val statusStr = friend["status"].toString()
                    val status = TSFriendStatus.fromString(statusStr)
                    val name = friend["name"].toString()
                    val email = friend["email"].toString()
                    parsedFriends.add(Friend(userId = userId, status = status, name = name, email = email))
                }
            }
        }
        return parsedFriends
    }

    fun getActivitiesForUserId(userId: String): MutableList<String> {
        var document: DocumentSnapshot? = null

        runBlocking {
            try {
                document = users.document(userId).get().await()
            } catch (e: Throwable) {
                Log.w("Error getting documents", e)
            }
        }

        if (document == null) {
            return mutableListOf()
        }

        var activities = document!!.get("activities") as MutableList<String>?

        if (activities != null) {
            return activities
        }

        return mutableListOf()
    }

    fun addActivity(userId: String, activityId: String) {
        runBlocking {
            try {
                users.document(userId).update("activities", FieldValue.arrayUnion(activityId)).await()
            } catch (e: Throwable) {
                Log.w(TAG, "Error adding activity to user.", e)
            }
        }
    }

    fun addGroupToUserId(userId: String, groupId: String) {
        runBlocking {
            users.document(userId).update("groups", FieldValue.arrayUnion(groupId))
                .await()
        }
    }

    fun removeGroupForUserId(userId: String, groupId: String) {
        runBlocking {
            users.document(userId).update("groups", FieldValue.arrayRemove(groupId))
                .await()
        }
    }

    fun removeActivity(userId: String, activityId: String) {
        users.document(userId).update("activities", FieldValue.arrayRemove(activityId))
            .addOnFailureListener { exception: Throwable ->
                Log.w(TAG, "Error removing activity from user.", exception)
            }
    }

    fun addFriend(userId: String, friend : Friend) {
        runBlocking {
            users.document(userId)
                .update("friends", FieldValue.arrayUnion(friend))
        }
    }
    fun removeFriend(userId: String, friendId : String) {
        runBlocking {
            var friendInfo = getFriendInfo(userId, friendId);
            val friendUpdate = hashMapOf<String, Any>(
                "friends" to FieldValue.arrayRemove(hashMapOf(
                    "userId" to friendInfo.userId,
                    "name" to friendInfo.name,
                    "email" to friendInfo.email,
                    "status" to friendInfo.status.toString(),
                ))
            )

            try {
                users.document(userId).update(friendUpdate).await()
            } catch (e: Throwable) {
                Log.w(TAG, "Error removing friend.", e)
            }
        }
    }

    fun updateFriendshipStatus(userId: String, friendId: String, friend: Friend) {
        removeFriend(userId,friendId)
        addFriend(userId, friend)
    }

    private fun getFriendInfo(userId: String, friendId: String): Friend {
        var user = getUserInfo(userId);

        for (friend in user.friends) {
            if (friend.userId == friendId) {
                return friend;
            }
        }

        return Friend();
    }
}
