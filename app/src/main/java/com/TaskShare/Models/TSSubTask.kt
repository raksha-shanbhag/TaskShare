package com.TaskShare.Models

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import com.TaskShare.ViewModels.TaskViewState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

data class TSSubTaskData (
    var taskId: String = "",
    var assignee: String = "",
    var taskStatus: String = "",
    var startDate: Timestamp = Timestamp(0, 0),
    var endDate: Timestamp = Timestamp(0, 0),
    var comments: MutableList<String> = mutableListOf()
)

class TSSubTask() {
    private var task = TSTask()

    var id = ""
    var subTaskData = TSSubTaskData()
    var taskStatus = TSTaskStatus.NULL

    companion object {
        private val TAG = "SubTask"

        fun getFromId(id: String): TSSubTask {
            var subTask = TSSubTask()
            subTask.id = id

            runBlocking {
                subTask.read()
            }

            return subTask
        }

        fun createSubTask(data: TSSubTaskData): String {
            val ref = Firebase.firestore.collection("SubTasks")
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

    fun isAssignedTo(userId: String): Boolean {
        return userId == subTaskData.assignee
    }

    fun getState(): TaskViewState {
        return TaskViewState(
            task.taskData.taskName,
            task.taskData.assigner,
            subTaskData.taskStatus,
            subTaskData.assignee,
            task.getGroup().groupData.groupName,
            subTaskData.endDate.toString(),
            task.taskData.cycle.toString() + " Days"
        )
    }

    suspend fun read() {
        var dataRef = Firebase.firestore.collection("SubTasks").document(id)
        var document: DocumentSnapshot? = null

        try {
            document = dataRef.get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (document != null && document.exists()) {
            subTaskData = TSSubTaskData(
                taskId = document.get("taskId") as String,
                assignee = document.get("assignee") as String,
                taskStatus = document.get("taskStatus") as String,
                startDate = document.get("startDate") as Timestamp,
                endDate = document.get("endDate") as Timestamp,
                comments = document.get("comments") as MutableList<String>
            )

            taskStatus = TSTaskStatus.fromString(subTaskData.taskStatus)
            task = TSTask.getFromId(subTaskData.taskId)
            task.read(false)
        }
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