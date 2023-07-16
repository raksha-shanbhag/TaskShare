package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class TSUsersRepository() {
    private val TAG = "TSUserApi"
    val db = Firebase.firestore
    val users = db.collection("Users")

    companion object {
        var globalUserId: String = ""
    }

    // temporary API
    fun getUserIdFromName(username: String) : String{
        var result = ""

        users.whereEqualTo("name", username)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    result = document.id
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        return result
    }

    // temporary API
    fun getUserIdsFromNames(usernames: MutableList<String>) : MutableList<String> {
        var result = HashSet<String>()
        for (name in usernames) {
            result.add(getUserIdFromName(name))
        }
        return result.toMutableList()
    }

    // Temporary API - get user from email (we'll just use userID)
    fun getUserIdFromEmail(email: String): String {
        var result = ""
        users.whereEqualTo("email", email).get()
            .addOnSuccessListener{documents ->
                for(document in documents) {
                    result = document.id
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
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
            Log.i("Debug Raksha TS User get group", document.toString())
            var groups = document.get("groups") as MutableList<String>
            result.addAll(groups)
        }

        Log.i("Debug Raksha TS user group state", result.toString())
        return result.toMutableList()
    }

    fun getUserInfo(userId: String): User {
        var userInfo = User()
        runBlocking {
            var result = users.document(userId).get().await()
            userInfo = User(
                firstName = result.get("firstName").toString(),
                lastName = result.get("lastName").toString(),
                email = result.get("email").toString(),
                phoneNumber = result.get("phoneNumber").toString()
            )
        }
        return userInfo
    }

    fun getActivitiesForUserId(userId: String): MutableList<String> {
        var document: DocumentSnapshot? = null

        runBlocking {
                document = users.document(userId).get().await()
            try {

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
                .addOnFailureListener { exception: Throwable ->
                    Log.w(TAG, "Error adding groupId to user.", exception)
                }
        }
    }

    fun removeActivity(userId: String, activityId: String) {
        users.document(userId).update("activities", FieldValue.arrayRemove(activityId))
            .addOnFailureListener { exception: Throwable ->
                Log.w(TAG, "Error removing activity from user.", exception)
            }
    }
}

data class TSUserData (
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val groups: MutableList<String> = mutableListOf()
)

class TSUser() {
//    private var groups: MutableList<TSGroup> = mutableListOf()

    var id = ""
    var userData = TSUserData()

    companion object {
        private val TAG = "User"
        var globalUser: TSUser = TSUser()

        fun getFromId(id: String, recurse: Boolean = true): TSUser {
            var user = TSUser()
            user.id = id

            runBlocking {
                user.read(recurse)
            }

            return user
        }

        fun getIdFromEmail(email: String): String {
            var documents: QuerySnapshot? = null
            var result = ""

            try {
                runBlocking {
                    documents = Firebase.firestore.collection("Users").whereEqualTo("email", email).get().await()
                }
            } catch (exception: Throwable) {
                Log.w(TAG, "Error finding user.", exception)
            }

            if (documents != null) {
                for (document in documents as QuerySnapshot) {
                    result = document.id
                    break
                }
            }

            return result
        }

        fun register(id: String, data: TSUserData) {
            val ref = Firebase.firestore.collection("Users").document(id)

            runBlocking {
                ref.set(data).await()
            }
        }

        fun updateGroup(userId: String, groupId: String, add: Boolean = true) {
            val ref = Firebase.firestore.collection("Users").document(userId)

            if (add) {
                ref.update("groups", FieldValue.arrayUnion(groupId))
                    .addOnFailureListener { exception: Throwable ->
                        Log.w(TAG, "Error adding group to user.", exception)
                    }
            } else {
                ref.update("Groups", FieldValue.arrayRemove(groupId))
                    .addOnFailureListener { exception: Throwable ->
                        Log.w(TAG, "Error removing group from user.", exception)
                    }
            }
        }
    }

//    fun getGroups(): MutableList<TSGroup> {
//        return groups;
//    }

    fun updateInfo(value: TSUserData) {
        val dbRef = Firebase.firestore.collection("Users").document(id)

        dbRef.update("firstName", userData.firstName)
        dbRef.update("lastName", userData.lastName)
        dbRef.update("email", userData.email)
        dbRef.update("phoneNumber", userData.phoneNumber)
    }

//    fun getTasks(): List<TSSubTask> {
//        var list: MutableList<TSSubTask> = mutableListOf()
//
//        for (group in groups) {
//            list.addAll(group.getSubTasksAssignedTo(id))
//        }
//
//        return list
//    }

    fun updateGroup(groupId: String, add: Boolean = true) {
        Companion.updateGroup(id, groupId, add)
    }

//    fun getTaskStates(): List<TaskViewState> {
//        var list: MutableList<TaskViewState> = mutableListOf()
//        var tasks = getTasks()
//
//        for (task in tasks) {
//            list.add(task.getState())
//        }
//
//        return list
//    }
//
//    fun createTask(taskState: AddTaskState) {
//        // empty
//    }

    suspend fun read(recurse: Boolean = true) {
        var document: DocumentSnapshot? = null

        try {
            document = Firebase.firestore.collection("Users").document(id).get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (document != null && document.exists()) {
            userData = TSUserData(
                firstName = document.get("firstName") as String,
                lastName = document.get("lastName") as String,
                email = document.get("email") as String,
                phoneNumber = document.get("phoneNumber") as String,
                groups = document.get("groups") as MutableList<String>,
            )

//            groups.clear()
//            if (recurse) {
//                for (groupId in userData.groups) {
//                    var group = TSGroup.getFromId(groupId)
//                    groups.add(group)
//                }
//            }
        }
    }
}