package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class TSTask(groupRef: TSGroup, taskId: String) {
    private val TAG = "Task"
    private val group = groupRef
    private val id = taskId
    private val dataRef = group.getTaskCollection().document(id)
    private var assignees: HashSet<String> = hashSetOf()
    private var subTasks: HashSet<TSSubTask> = hashSetOf()

    fun getId(): String {
        return id
    }

    fun create() {
        dataRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "Group already exists.")
                } else {
                    val data = hashMapOf(
                        "Assignees" to assignees.toList()
                    )

                    dataRef.set(data)
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error creating group.", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun getSubTaskCollection(): CollectionReference {
        return dataRef.collection("SubTasks")
    }

    fun getSubTasks(): Set<TSSubTask> {
        return subTasks
    }

    fun getSubTasksAssignedTo(userId: String): Set<TSSubTask> {
        var set: HashSet<TSSubTask> = hashSetOf()

        for (subTask in subTasks) {
            if (subTask.isAssignedTo(userId)) {
                set.add(subTask)
            }
        }

        return set
    }

    fun isAssignedTo(userId: String): Boolean {
        return assignees.contains(userId)
    }

    fun get() {
        dataRef.get()
            .addOnSuccessListener { result ->
                set(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        dataRef.collection("SubTasks")
            .get()
            .addOnSuccessListener { result ->
                setSubTasks(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun synchronisedGet() {
        var result: DocumentSnapshot? = null
        var result2: QuerySnapshot? = null

        try {
            result = dataRef.get().await()
            result2 = dataRef.collection("SubTasks").get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null && result2 != null) {
            set(result)
            setSubTasks(result2)
        }
    }

    private fun set(result: DocumentSnapshot) {
        assignees.addAll(result.get("Assignees") as List<String>)
    }

    private fun setSubTasks(result: QuerySnapshot) {
        var set: HashSet<TSSubTask> = hashSetOf()

        for (document in result) {
            var flag = false

            for (subTask in subTasks) {
                if (subTask.getId() == document.id) {
                    set.add(subTask)
                    flag = true
                    break
                }
            }

            if (!flag) {
                set.add(TSSubTask(this, document.id))
            }
        }

        subTasks = set
    }
}