package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.SubTask
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Date

// APIs
class TSSubTasksRepository {
    private val TAG = "TSSubTasksRepository"
    private val db = Firebase.firestore
    private val subTasks = db.collection("SubTasks")

    // API to create Sub Tasks
    fun createSubTask(taskId: String, assigneeId: String, startDate: Date, endDate: Date): String {
        var data = hashMapOf(
            "startDate" to startDate,
            "endDate" to endDate,
            "taskId" to taskId,
            "assigneeId" to assigneeId,
            "taskStatus" to TSTaskStatus.TODO
        )

        var documentId = ""
        runBlocking {
            var document = subTasks.add(data).await()
            if (document != null) {
                documentId = document.id
                Log.d(TAG, document.id)
                Log.d(TAG, "DocumentSnapshot successfully written!")
            } else {
                Log.w(TAG, "Error writing document")
            }
        }

        return documentId
    }

    // API to get all subtasks for a userId
    fun getAllSubtasksForUserId(userId : String): MutableList<SubTask> {
        var result = ArrayList<SubTask>()

        runBlocking {
            // assigned Tasks
            var documentSnapshot = subTasks.whereEqualTo("assigneeId", userId).get().await()

            for (document in documentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("endDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()
                val taskStatus = TSTaskStatus.valueOf(document.data?.get("taskStatus").toString())

                var element = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = taskStatus,
                    startDate = startDate,
                    endDate = endDate,
                )

                result.add(element)
            }

            // transferred tasks
            var transferredDocumentSnapshot = subTasks.whereEqualTo("taskTransferAssignee", userId).get().await()

            for (document in transferredDocumentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("endDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()
                val taskStatus = TSTaskStatus.valueOf(document.data?.get("taskStatus").toString())

                var element = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = taskStatus,
                    startDate = startDate,
                    endDate = endDate,
                    taskTransferAssignee = userId
                )

                result.add(element)
            }
        }
        return result.toMutableList()
    }

    // @toDo -  API to update subtask status from subtasktaskID

    // API to get subtask info for a given subtaskId
    fun getSubTaskInfoForId(subtaskId: String) : SubTask {
        var result = SubTask()

        runBlocking {
            var document = subTasks.document(subtaskId).get().await()
            if(document.exists()) {
//                    var subTaskComments = ArrayList<String>()
//                    subTaskComments.addAll(document.get("comments") as List<String>)

                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("endDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()
                val taskStatus = TSTaskStatus.valueOf(document.data?.get("taskStatus").toString())


                result = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = taskStatus,
                    startDate = startDate,
                    endDate = endDate,
                )
            } else {
                Log.w(TAG, "Error getting documents.")
            }
        }

        return result
    }

    // API to get all subtasks for a taskId
    fun getAllSubtasksForTaskId(taskId : String): MutableList<SubTask> {
        var result = ArrayList<SubTask>()

        runBlocking {
            var documentSnapshot = subTasks.whereEqualTo("taskId", taskId).get().await()
            for (document in documentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("endDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()
                val taskStatus = TSTaskStatus.valueOf(document.data?.get("taskStatus").toString())

                var element = SubTask(
                    subTaskId = document.id,
                    taskId = taskId,
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = taskStatus,
                    startDate = startDate,
                    endDate = endDate,
                )

                result.add(element)
            }
        }
        return result.toMutableList()
    }

    fun updateSubTaskStatus(subtaskId: String, newStatus: TSTaskStatus) {
        runBlocking {
            subTasks.document(subtaskId).update("taskStatus", newStatus).await()
        }
    }

    // API to transfer task
    fun updateSubtaskTransfer(subtaskId: String, status: TSTaskStatus, taskTransferAssignee: String?, assigneeId: String) {
        runBlocking {
            var data = hashMapOf(
                "taskStatus" to status,
                "taskTransferAssignee" to taskTransferAssignee,
                "assigneeId" to assigneeId
            )
            subTasks.document(subtaskId).update(data.toMap()).await()
        }
    }

    fun getSubtasksForListOfTaskIds(taskIds : MutableList<String>) : MutableList<SubTask> {
        var result = ArrayList<SubTask>()
        runBlocking {
            var documentSnapshot = subTasks.whereIn("taskId", listOf(taskIds)).get().await()
            for (document in documentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("endDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()
                val taskStatus = TSTaskStatus.valueOf(document.data?.get("taskStatus").toString())

                var element = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = taskStatus,
                    startDate = startDate,
                    endDate = endDate,
                )

                result.add(element)
            }
        }

        return result.toMutableList()
    }

    fun getActiveTasksNumber(userId: String) : Int {
        var result = 0
        runBlocking {
            var documentSnapshot = subTasks
                .whereEqualTo("assigneeId", userId)
                .get().await()


            for (document in documentSnapshot.documents) {
                val taskStatus = TSTaskStatus.valueOf(document.data?.get("taskStatus").toString())
                if (taskStatus != TSTaskStatus.COMPLETE && taskStatus != TSTaskStatus.DECLINED) {
                    result += 1
                }
            }
        }
        return result
    }
}