package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.SubTask
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
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

    // API to get all subtasks from a taskId
    fun getAllSubTasksFromTaskId(taskId: String): MutableList<SubTask> {
        var result = ArrayList<SubTask>()

        runBlocking {

        }
        return result.toMutableList()
    }

    // API to get all subtasks for a userId
    fun getAllSubtasksForUserId(userId : String): MutableList<SubTask> {
        var result = ArrayList<SubTask>()

        runBlocking {
            Log.i("Debug Rak user", userId)
            var documentSnapshot = subTasks.whereEqualTo("assigneeId", userId).get().await()
            Log.i("Debug Raksha check this -- ", documentSnapshot.toString())
            Log.i("Debug Raksha check this -size- ", documentSnapshot.size().toString())

            for (document in documentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("lastDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()

                var element = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = TSTaskStatus.fromString(document.data?.get("taskStatus").toString()),
                    startDate = startDate,
                    endDate = endDate,
                )

                Log.i("Debug Raksha check this", element.toString())

                result.add(element)
            }
            Log.i("Debug Raksha check this", documentSnapshot.documents.toString())
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
                val endDateTimestamp = document.data?.get("lastDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()

                result = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeID").toString(),
                    taskStatus = TSTaskStatus.fromString(document.data?.get("assigneeID").toString()),
                    startDate = startDate,
                    endDate = endDate,
                )
            } else {
                Log.w(TAG, "Error getting documents.")
            }
        }

        return result
    }
}