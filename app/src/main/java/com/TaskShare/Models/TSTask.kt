package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class TSTaskAPI() {
    private val TAG = "TSGroupAPI"
    private val db = Firebase.firestore
    private val tasks = db.collection("Tasks")

    // create a Task
    fun createTask(
        taskName: String,
        groupId: String,
        cycle: String,
        deadline: String
    ) : String {
        var documentId: String = ""

        var data = hashMapOf(
            "taskName" to taskName,
            "cycle" to cycle,
            "deadline" to deadline,
            "groupId" to groupId
        )

        tasks.add(data)
            .addOnSuccessListener { document ->
                documentId = document.id
                Log.d(TAG, document.id)
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        return documentId
    }
}

class TSTask(groupRef: TSGroup, taskId: String) {
    private val TAG = "Task"
    private val group = groupRef
    private val id = taskId
    private val dataRef = group.getTaskCollection().document(id)

    var taskName: String = "None"
    var assigner: String = "None"
    var assignees: MutableList<String> = mutableListOf()
    var subTasks: MutableList<TSSubTask> = mutableListOf()
    var cycle: Int = 0;

    fun getId(): String {
        return id
    }

    fun getGroup(): TSGroup {
        return group
    }

    fun getSubTaskCollection(): CollectionReference {
        return dataRef.collection("SubTasks")
    }

    fun getSubTasksAssignedTo(userId: String): List<TSSubTask> {
        var list: MutableList<TSSubTask> = mutableListOf()

        for (subTask in subTasks) {
            if (subTask.isAssignedTo(userId)) {
                list.add(subTask)
            }
        }

        return list
    }

    fun isAssignedTo(userId: String): Boolean {
        return assignees.contains(userId)
    }

    fun read() {
        dataRef.get()
            .addOnSuccessListener { result ->
                readCallback(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        dataRef.collection("SubTasks")
            .get()
            .addOnSuccessListener { result ->
                readSubTasksCallback(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun syncRead() {
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
            readCallback(result)
            readSubTasksCallback(result2)
        }
    }

    fun write(overwrite: Boolean = true) {
        dataRef.get()
            .addOnSuccessListener { document ->
                if (document.exists() && !overwrite) {
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

    private fun readCallback(result: DocumentSnapshot) {
        assignees.addAll(result.get("Assignees") as List<String>)
    }

    private fun readSubTasksCallback(result: QuerySnapshot) {
        var list: MutableList<TSSubTask> = mutableListOf()

        for (document in result) {
            var flag = false

            for (subTask in subTasks) {
                if (subTask.getId() == document.id) {
                    list.add(subTask)
                    flag = true
                    break
                }
            }

            if (!flag) {
                list.add(TSSubTask(this, document.id))
            }
        }

        subTasks = list
    }
}