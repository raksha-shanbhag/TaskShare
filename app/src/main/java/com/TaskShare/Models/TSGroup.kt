package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TSGroup(groupId: String) {
    private val TAG = "Group"
    private val id = groupId
    private var users: HashSet<TSUser> = hashSetOf()
    private var tasks: HashSet<TSTask> = hashSetOf()

    fun getId(): String {
        return id
    }

    fun create() {
        val db = Firebase.firestore
        val docRef = db.collection("Groups").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "Group already exists.")
                } else {
                    var userIds: HashSet<String> = hashSetOf()

                    for (user in users) {
                        userIds.plus(user.getId())
                    }

                    val data = hashMapOf(
                        "Users" to users.toList()
                    )
                    docRef.set(data)
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error creating group.", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun getUsers(refresh: Boolean = false): HashSet<TSUser> {
        if (refresh) {
            get()
        }

        return users
    }

    fun getTaskCollection(): CollectionReference {
        return Firebase.firestore.collection("Groups").document(id).collection("Tasks")
    }

    fun updateUser(userId: String, add: Boolean = true): Boolean {
        val db = Firebase.firestore
        val dbRef = db.collection("Groups").document(id)

        var success = true
        val failureListener = { exception: Throwable ->
            Log.w(TAG, "Error updating users in group.", exception)
            success = false
        }

        if (add) {
            dbRef.update("Users", FieldValue.arrayUnion(userId))
                .addOnFailureListener(failureListener)
        } else {
            dbRef.update("Users", FieldValue.arrayRemove(userId))
                .addOnFailureListener(failureListener)
        }

        return success
    }

    private fun get() {
        val db = Firebase.firestore
        val ref = db.collection("Groups").document(id)
        users.clear()
        tasks.clear()

        ref.get()
            .addOnSuccessListener { result ->
                var userIds: HashSet<String> = hashSetOf()
                userIds.plus(result.get("Users"))

                for (userId in userIds) {
                    users.plus(TSUser(userId))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        ref.collection("Tasks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tasks.plus(TSTask(this, document.id))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
