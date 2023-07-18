package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.SubTask
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date

//// data Class
//data class ResponseSubTask(
//    var assigneeId: String = "",
//    var taskStatus: TSTaskStatus = TSTaskStatus.NULL,
//    // var updatedBy
//    var startDate: Timestamp = Timestamp(0, 0),
//    var endDate: Timestamp = Timestamp(0, 0),
//    var comments: HashSet<String?> = hashSetOf(),
//    var id: String = ""
//)

// APIs
class TSSubTasksRepository {
    private val TAG = "TSSubTasksRepository"
    private val db = Firebase.firestore
    private val subTasks = db.collection("SubTasks")
    private val pattern = "dd-MM-yyyy"
    private val dateFormat = SimpleDateFormat(pattern)

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
            var documents = subTasks.whereEqualTo("assigneeId", userId).get().await()
            for (document in documents) {
                var element = SubTask(
                    subTaskId = document.id,
                    taskId = document.data?.get("taskId").toString(),
                    assigneeId = document.data?.get("assigneeId").toString(),
                    taskStatus = TSTaskStatus.fromString(document.data?.get("taskStatus").toString()),
                    startDate = dateFormat.parse(document.data?.get("startDate").toString()),
                    endDate = dateFormat.parse(document.data?.get("endDate").toString()),
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
        subTasks.document(subtaskId).get()
            .addOnSuccessListener{document ->
                if(document.exists()) {
                    var subTaskComments = ArrayList<String>()
                    subTaskComments.addAll(document.get("comments") as List<String>)

                    result = SubTask(
                        subTaskId = document.id,
                        taskId = document.data?.get("subTaskId").toString(),
                        assigneeId = document.data?.get("assigneeID").toString(),
                        taskStatus = TSTaskStatus.fromString(document.data?.get("assigneeID").toString()),
                        startDate = dateFormat.parse(document.data?.get("startDate").toString()),
                        endDate = dateFormat.parse(document.data?.get("endDate").toString()),
                    )
                }
            }
            .addOnFailureListener{exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return result
    }
}