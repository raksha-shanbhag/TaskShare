package com.TaskShare.Models

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import com.TaskShare.ViewModels.TaskViewState

class TSSubTask(taskRef: TSTask, subTaskId: String) {
    private val TAG = "SubTask"
    private val task = taskRef
    private val id = subTaskId
    private val dataRef = task.getSubTaskCollection().document(id)

    var assignee: String = "None"
    var taskStatus: TSTaskStatus = TSTaskStatus.NULL
    var startDate: Timestamp = Timestamp(0, 0)
    var endDate: Timestamp = Timestamp(0, 0)
    var comments: MutableList<String?> = mutableListOf()

    fun getId(): String {
        return id
    }

    fun isAssignedTo(userId: String): Boolean {
        return userId == assignee
    }

    fun getState(): TaskViewState {
        return TaskViewState(
            task.taskName,
            task.assigner,
            taskStatus.displayString,
            assignee,
            task.getGroup().name,
            endDate.toString(),
            task.cycle.toString() + " Days"
        )
    }

    fun read() {
        dataRef.get()
            .addOnSuccessListener{result ->
                readCallback(result)
            }
            .addOnFailureListener{exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    suspend fun syncRead() {
        var result: DocumentSnapshot? = null
        try {
            result = dataRef.get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null) {
            readCallback(result)
        }
    }

    fun write(overwrite: Boolean = true) {
        dataRef.get()
            .addOnSuccessListener { document ->
                if (document.exists() && !overwrite) {
                    Log.w(TAG, "SubTask already exists.")
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

    private fun readCallback(result: DocumentSnapshot) {
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