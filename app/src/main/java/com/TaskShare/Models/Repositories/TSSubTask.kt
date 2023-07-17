package com.TaskShare.Models.Repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.TaskShare.Models.DataObjects.TSTaskStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Date

// data Class
data class RequestSubTask(
    var assigneeId: String = "",
    var taskStatus: TSTaskStatus = TSTaskStatus.NULL,
    // var updatedBy
    var startDate: Timestamp = Timestamp(0, 0),
    var endDate: Timestamp = Timestamp(0, 0),
    var comments: HashSet<String?> = hashSetOf()
)

data class ResponseSubTask(
    var assigneeId: String = "",
    var taskStatus: TSTaskStatus = TSTaskStatus.NULL,
    // var updatedBy
    var startDate: Timestamp = Timestamp(0, 0),
    var endDate: Timestamp = Timestamp(0, 0),
    var comments: HashSet<String?> = hashSetOf(),
    var id: String = ""
)

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
    fun getAllSubTasksFromTaskId(taskId: String): MutableList<ResponseSubTask> {
        var result = ArrayList<ResponseSubTask>()

        runBlocking {

        }
        return result.toMutableList()
    }

    // API to get all subtasks for a userId
    fun getAllSubtasksForMemberId(): MutableList<ResponseSubTask> {
        var result = ArrayList<ResponseSubTask>()

        runBlocking {

        }
        return result.toMutableList()
    }

    // @toDo -  API to update subtask status from taskID

    // API to get subtask info for a given subtaskId
    fun getSubTaskInfoFromId(subtaskId: String) : ResponseSubTask {
        var result = ResponseSubTask()
        subTasks.document(subtaskId).get()
            .addOnSuccessListener{document ->
                if(document.exists()) {
                    var subTaskComments = ArrayList<String>()
                    subTaskComments.addAll(document.get("comments") as List<String>)

                    result = ResponseSubTask(
                        id = document.id,
                        assigneeId = document.data?.get("assigneeID").toString(),
                        taskStatus = TSTaskStatus.fromString(document.data?.get("assigneeID").toString()),
                        comments = subTaskComments.toHashSet()
                    )
                }
            }
            .addOnFailureListener{exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return result
    }
}