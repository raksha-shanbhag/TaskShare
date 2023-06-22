package com.TaskShare.Models

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class TSSubTask(taskRef: TSTask, subTaskId: String) {
    private val TAG = "SubTask"
    private val task = taskRef
    private val id = subTaskId
    private val dataRef = task.getSubTaskCollection().document(id)

    var assignee: String = "None"
    var taskStatus: TSTaskStatus = TSTaskStatus.NULL
    var startDate: Timestamp = Timestamp(0, 0)
    var endDate: Timestamp = Timestamp(0, 0)
    var comments: HashSet<String?> = hashSetOf()

    fun getId(): String {
        return id
    }

    fun create(overwrite: Boolean = false) {
        dataRef.get()
            .addOnSuccessListener { document ->
                if (document.exists() && !overwrite) {
                    Log.w(TAG, "Group already exists.")
                } else {
                    val data = hashMapOf(
                        "Assignee" to assignee,
                        "Task Status" to taskStatus.displayString,
                        "Start Date" to startDate,
                        "End Date" to endDate,
                        "Comments" to comments.toList()
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

    fun delete() {
        dataRef.delete()
    }

    fun isAssignedTo(userId: String): Boolean {
        return userId == assignee
    }

    fun get() {
        dataRef.get()
            .addOnSuccessListener{result ->
                set(result)
            }
            .addOnFailureListener{exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun synchronisedGet() {
        var result: DocumentSnapshot? = null
        try {
            result = dataRef.get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null) {
            set(result)
        }
    }

    private fun set(result: DocumentSnapshot) {
        comments.clear()
        assignee = result.get("Assignee") as String
        taskStatus = TSTaskStatus.fromString(result.get("Task Status") as String)
        startDate = result.get("Start Date") as Timestamp
        endDate = result.get("End Date") as Timestamp
        comments.addAll(result.get("Comments") as List<String?>)
    }

    enum class TSTaskStatus(str: String) {
        NULL("Error"),
        TODO("To Do"),
        IN_PROGRESS("In Progress"),
        PENDING_APPROVAL("Pending Approval"),
        COMPLETE("Complete"),
        OVERDUE("Overdue"),
        DECLINED("Declined");

        var displayString: String = str

        companion object {
            @JvmStatic
            fun fromString(name: String): TSTaskStatus {
                return when (name)
                {
                    "To Do" -> TODO
                    "In Progress" -> IN_PROGRESS
                    "Pending Approval" -> PENDING_APPROVAL
                    "Complete" -> COMPLETE
                    "Overdue" -> OVERDUE
                    "Declined" ->  DECLINED
                    else -> NULL
                }
            }
        }
    }
}