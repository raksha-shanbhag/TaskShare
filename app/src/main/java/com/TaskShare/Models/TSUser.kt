package com.TaskShare.Models

import android.util.Log
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
    private val TAG = "User"
    private val id = userId
    private val firstName: HashSet<String> = hashSetOf()
    private val lastName: HashSet<String> = hashSetOf()
    private val email: HashSet<String> = hashSetOf()
    private val password : HashSet<String> = hashSetOf()
    private val phoneNumber: HashSet<String> = hashSetOf()
    private var groups: HashSet<TSGroup> = hashSetOf()
    private var tasks: HashSet<TSSubTask>? = null

    fun getId(): String {
        return id
    }

    fun create() {
        val db = Firebase.firestore
        val docRef = db.collection("Users").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "User already exists.")
                } else {
                    var groupIds: HashSet<String> = hashSetOf()

                    for (group in groups) {
                        groupIds.add(group.getId())
                    }

                    val data = hashMapOf(
                        "First_Name" to firstName.toList(),
                        "Last_Name" to lastName.toList(),
                        "Email" to email.toList(),
                        "Password" to password.toList(),
                        "Phone_Number" to phoneNumber.toList(),
                        "Groups" to groups.toList()
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

    fun getGroups(): Set<TSGroup> {
        return groups;
    }
    fun setEmail(value: HashSet<String>) {
        email.clear()
        email.addAll(value)
    }

    fun setPassword(value: HashSet<String>) {
        password.clear()
        password.addAll(value)
    }

    fun setFirstName(value: HashSet<String>) {
        firstName.clear()
        firstName.addAll(value)
    }

    fun setLastName(value: HashSet<String>) {
        lastName.clear()
        lastName.addAll(value)
    }


    fun getTasks(): Set<TSSubTask> {
        if (tasks != null) {
            return tasks as HashSet<TSSubTask>
        }

        var set: HashSet<TSSubTask> = hashSetOf()

        for (group in groups) {
            set.addAll(group.getSubTasksAssignedTo(id))
        }

        tasks = set
        return set
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

    fun get() {
        val db = Firebase.firestore
        db.collection("Users").document(id)
            .get()
            .addOnSuccessListener { result ->
                set(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun synchronisedGet() {
        var result: DocumentSnapshot? = null
        try {
            result = Firebase.firestore.collection("Users").document(id).get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null) {
            set(result)
        }
    }

    private fun set(result: DocumentSnapshot) {
        var groupIds: HashSet<String> = hashSetOf()
        groupIds.addAll(result.get("Groups") as List<String>)
        Log.w(TAG, groupIds.toString() + "TESTING")

        var set: HashSet<TSGroup> = hashSetOf()

        for (groupId in groupIds) {
            var flag = false

            for (group in groups) {
                if (group.getId() == groupId) {
                    set.add(group)
                    flag = true
                    break
                }
            }

            if (!flag) {
                set.add(TSGroup(groupId))
            }
        }

        groups = set
    }
}
