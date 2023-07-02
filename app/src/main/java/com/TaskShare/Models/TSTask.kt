package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
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

data class TSTaskData (
    val groupId: String = "",
    val taskName: String = "",
    val assigner: String = "",
    val assignees: MutableList<String> = mutableListOf(),
    val subTasks: MutableList<String> = mutableListOf(),
    val cycle: String = ""
)

class TSTask() {
    private var subTasks = mutableListOf<TSSubTask>()
    private var group = TSGroup()

    var id = ""
    var taskData = TSTaskData()

    companion object {
        private val TAG = "Task"

        fun getFromId(id: String, recurse: Boolean = true): TSTask {
            var task = TSTask()
            task.id = id

            runBlocking {
                task.read(recurse)
            }

            return task
        }

        fun createTask(data: TSTaskData): String {
            val ref = Firebase.firestore.collection("Tasks")
            var documentId = ""

            runBlocking {
                try {
                    var document = ref.add(data).await()
                    Log.d(TAG, document.id)
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    documentId = document.id
                } catch (exception: Throwable) {
                    Log.w(TAG, "Error writing document", exception)
                }
            }

            return documentId
        }
    }

    fun getGroup(): TSGroup {
        return group
    }

    fun getSubTasks(): List<TSSubTask> {
        return subTasks
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
        return taskData.assignees.contains(userId)
    }

    fun updateSubTask(subTaskId: String, add: Boolean = true) {
        val dbRef = Firebase.firestore.collection("Tasks").document(id)

        if (add) {
            dbRef.update("tasks", FieldValue.arrayUnion(subTaskId))
                .addOnFailureListener{ exception: Throwable ->
                    Log.w(TAG, "Error adding subtask to task.", exception)
                }
        } else {
            dbRef.update("tasks", FieldValue.arrayRemove(subTaskId))
                .addOnFailureListener{ exception: Throwable ->
                    Log.w(TAG, "Error removing subtask from task.", exception)
                }
        }
    }

    suspend fun read(recurse: Boolean = true) {
        var dataRef = Firebase.firestore.collection("Tasks").document(id)
        var document: DocumentSnapshot? = null

        try {
            document = dataRef.get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (document != null && document.exists()) {
            taskData = TSTaskData(
                groupId = document.get("groupId") as String,
                taskName = document.get("taskName") as String,
                assigner = document.get("assigner") as String,
                assignees = document.get("assignees") as MutableList<String>,
                subTasks = document.get("subTasks") as MutableList<String>,
                cycle = document.get("cycle") as String
            )

            group = TSGroup.getFromId(taskData.groupId, false)

            subTasks.clear()
            if (recurse) {
                for (subTaskId in taskData.subTasks) {
                    var subTask = TSSubTask.getFromId(subTaskId)
                    subTasks.add(subTask)
                }
            }


        }
    }
}