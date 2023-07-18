package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date

// Repository
class TSTasksRepository {
    private val TAG = "TSTasksRepository"
    private val db = Firebase.firestore
    private val tasks = db.collection("Tasks")
    private val pattern = "dd-MM-yyyy"
    private val dateFormat = SimpleDateFormat(pattern)

    // API Service for creating a Task
    fun createTask(
        assignerId: String,
        taskName: String,
        groupId: String,
        cycle: String,
        lastDate: Date,
        assignees: MutableList<String>,
        startDate: Date
    ) : String {
        var documentId: String = ""

        var data = hashMapOf(
            "taskName" to taskName,
            "cycle" to cycle,
            "lastDate" to lastDate,
            "groupId" to groupId,
            "assignerId" to assignerId,
            "startDate" to startDate,
            "assignees" to assignees
        )

        runBlocking {
            var document = tasks.add(data).await()
                if(document != null){
                    documentId = document.id
                    Log.d(TAG, document.id)
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                } else {
                    Log.w(TAG, "Error writing document")
                }
        }
        return documentId
    }

    // API Service for getting a task
    fun getTask(taskId: String) : Task {
        var result = Task()

        runBlocking {
            var document = tasks.document(taskId).get().await()
            var assignees = ArrayList<String>()
            assignees.addAll(document.get("assignees") as List<String>)

            if (document != null) {
                result = Task(
                    taskId = document.id,
                    taskName = document.data?.get("taskName").toString(),
                    groupId = document.data?.get("groupId").toString(),
                    cycle = document.data?.get("cycle").toString(),
                    assignerId = document.data?.get("assignerId").toString(),
                    startDate = dateFormat.parse(document.data?.get("startDate").toString()),
                    lastDate = dateFormat.parse(document.data?.get("lastDate").toString()),
                    assignees = assignees
                )
            } else {
                Log.w(TAG, "Error writing document")
            }
        }

        return result
    }

    // API service to get tasks for a group Id
    suspend fun getTasksForGroupId(groupId: String): MutableList<Task> {
        var result = mutableListOf<Task>()

        runBlocking {
            var documents = tasks.whereEqualTo("groupId", groupId).get().await()
            for (document in documents) {
                var task = Task(
                    taskId = document.id,
                    taskName = document.data?.get("taskName").toString(),
                    groupId = groupId,
                    cycle = document.data?.get("cycle").toString(),
                    assignerId = document.data?.get("assignerId").toString(),
                    startDate = dateFormat.parse(document.data?.get("startDate").toString()),
                    lastDate = dateFormat.parse(document.data?.get("lastDate").toString()),
                )

                result.add(task)
            }
        }

        return result
    }
}