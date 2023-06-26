package com.TaskShare.Models

import android.util.Log
import com.TaskShare.ViewModels.AddTaskState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class TSUserApi() {
    private val TAG = "TSUserApi"
    val db = Firebase.firestore
    val users = db.collection("Users")

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

}

class TSUser(userId: String) {
    companion object var globalUser: TSUser? = null
    private val TAG = "User"
    private val id = userId
    private var groups: MutableList<TSGroup> = mutableListOf()

    var name: String = "None"

    fun getId(): String {
        return id
    }

    fun getGroups(): List<TSGroup> {
        return groups;
    }

    fun getTasks(): List<TSSubTask> {
        var list: MutableList<TSSubTask> = mutableListOf()

        for (group in groups) {
            list.addAll(group.getSubTasksAssignedTo(id))
        }

        return list
    }

    fun createTask(taskState: AddTaskState) {
        // empty
    }

    fun updateGroup(group: TSGroup, add: Boolean = true): Boolean {
        val db = Firebase.firestore
        val dbRef = db.collection("Users").document(id)

        var success = true
        val failureListener = { exception: Throwable ->
            Log.w(TAG, "Error updating groups in user.", exception)
            success = false
        }

        if (add) {
            dbRef.update("Groups", FieldValue.arrayUnion(group.getId()))
                .addOnFailureListener(failureListener)
        } else {
            dbRef.update("Groups", FieldValue.arrayRemove(group.getId()))
                .addOnFailureListener(failureListener)
        }

        if (!group.updateUser(this, add)) {
            dbRef.update("Groups", FieldValue.arrayRemove(group.getId()))
                .addOnFailureListener(failureListener)
        }

        if (success) {
            groups.add(group)
        }

        return success
    }

    fun read() {
        val db = Firebase.firestore
        db.collection("Users").document(id)
            .get()
            .addOnSuccessListener { result ->
                readCallback(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun syncRead() {
        var result: DocumentSnapshot? = null
        try {
            result = Firebase.firestore.collection("Users").document(id).get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null) {
            readCallback(result)
        }
    }

    fun write() {
        val db = Firebase.firestore
        val docRef = db.collection("Users").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "User already exists.")
                } else {
                    var groupIds: MutableList<String> = mutableListOf()

                    for (group in groups) {
                        groupIds.add(group.getId())
                    }

                    val data = hashMapOf(
                        "Groups" to groups.toList(),
                    )
                    docRef
                        .set(data)
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error creating user.", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun readCallback(result: DocumentSnapshot) {
        var groupIds: MutableList<String> = mutableListOf()
        groupIds.addAll(result.get("Groups") as List<String>)
        Log.w(TAG, groupIds.toString() + "TESTING")

        var list: MutableList<TSGroup> = mutableListOf()

        for (groupId in groupIds) {
            var flag = false

            for (group in groups) {
                if (group.getId() == groupId) {
                    list.add(group)
                    flag = true
                    break
                }
            }

            if (!flag) {
                list.add(TSGroup(groupId))
            }
        }

        groups = list
    }
}
