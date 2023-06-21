package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User(userId: String) {
    private val TAG = "User"
    private val id = userId
    private var groups: HashSet<Group> = hashSetOf()
    private var tasks: HashSet<SubTask> = hashSetOf()

    fun getId(): String {
        return id
    }

    fun create() {
        val db = Firebase.firestore
        val docRef = db.collection("Groups").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "User already exists.")
                } else {
                    var groupIds: HashSet<String> = hashSetOf()

                    for (group in groups) {
                        groupIds.plus(group.getId())
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

    fun getGroups(refresh: Boolean = false): Set<Group> {
        if (refresh) {
            get()
        }

        return groups;
    }

    fun getTasks(refresh: Boolean = false): Set<SubTask> {
        if (refresh) {
            get()
        }

        return tasks;
    }

    fun updateGroup(group: Group, add: Boolean = true): Boolean {
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

        if (!group.updateUser(id, add)) {
            dbRef.update("Groups", FieldValue.arrayRemove(group.getId()))
                .addOnFailureListener(failureListener)
        }

        return success
    }

    private fun get() {
        val db = Firebase.firestore
        groups.clear()

        db.collection("Users").document(id)
            .get()
            .addOnSuccessListener { result ->
                var groupIds: HashSet<String> = hashSetOf()
                groupIds.plus(result.get("Groups"))

                for (groupId in groupIds) {
                    groups.plus(Group(groupId))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
