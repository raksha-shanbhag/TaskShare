package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

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
                        userIds.add(user.getId())
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

    fun getUsers(): HashSet<TSUser> {
        return users
    }

    fun getTasks(): HashSet<TSTask> {
        return tasks
    }

    fun getTaskCollection(): CollectionReference {
        return Firebase.firestore.collection("Groups").document(id).collection("Tasks")
    }

    fun getSubTasksAssignedTo(userId: String): Set<TSSubTask> {
        var set: HashSet<TSSubTask> = hashSetOf()

        for (task in tasks) {
            if (task.isAssignedTo(userId)) {
                set.addAll(task.getSubTasksAssignedTo(userId))
            }
        }

        return set
    }

    fun updateUser(user: TSUser, add: Boolean = true): Boolean {
        val db = Firebase.firestore
        val dbRef = db.collection("Groups").document(id)

        var success = true
        val failureListener = { exception: Throwable ->
            Log.w(TAG, "Error updating users in group.", exception)
            success = false
        }

        if (add) {
            dbRef.update("Users", FieldValue.arrayUnion(user.getId()))
                .addOnFailureListener(failureListener)
        } else {
            dbRef.update("Users", FieldValue.arrayRemove(user.getId()))
                .addOnFailureListener(failureListener)
        }

        if (success) {
            users.add(user)
        }

        return success
    }

    fun get() {
        val ref = Firebase.firestore.collection("Groups").document(id)

        ref.get()
            .addOnSuccessListener { result ->
                set(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        ref.collection("Tasks")
            .get()
            .addOnSuccessListener { result ->
                setTasks(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun synchronisedGet() {
        val ref = Firebase.firestore.collection("Groups").document(id)
        var result: DocumentSnapshot? = null
        var result2: QuerySnapshot? = null

        try {
            result = ref.get().await()
            result2 = ref.collection("Tasks").get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null && result2 != null) {
            set(result)
            setTasks(result2)
        }
    }

    private fun set(result: DocumentSnapshot) {
        var userIds: HashSet<String> = hashSetOf()
        userIds.addAll(result.get("Users") as List<String>)

        var set: HashSet<TSUser> = hashSetOf()

        for (userId in userIds) {
            var flag = false
            for (user in users) {
                if (user.getId() == userId) {
                    set.add(user)
                    flag = true
                    break
                }
            }

            if (!flag) {
                set.add(TSUser(userId))
            }
        }

        users = set
    }

    private fun setTasks(result: QuerySnapshot) {
        var set: HashSet<TSTask> = hashSetOf()

        for (document in result) {
            var flag = false

            for (task in tasks) {
                if (task.getId() == document.id) {
                    set.add(task)
                    flag = true
                    break
                }
            }

            if (!flag) {
                set.add(TSTask(this, document.id))
            }
        }

        tasks = set
    }
}
