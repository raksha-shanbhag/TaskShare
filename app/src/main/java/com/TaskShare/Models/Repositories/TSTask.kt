package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.TaskShare.ViewModels.TaskViewState
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

// Repository
class TSTasksRepository {
    private val TAG = "TSTasksRepository"
    private val db = Firebase.firestore
    private val tasks = db.collection("Tasks")

    // API Service for creating a Task
    fun createTask(
        assignerId: String,
        taskName: String,
        groupId: String,
        cycle: String,
        lastDate: Date
    ) : String {
        var documentId: String = ""
        var startDate: Date = Date()

        var data = hashMapOf(
            "taskName" to taskName,
            "cycle" to cycle,
            "lastDate" to lastDate,
            "groupId" to groupId,
            "assignerId" to assignerId,
            "startDate" to startDate
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

    // API Service for getting a task
    fun getTask(taskId: String) : Task {
        var result = Task()

        runBlocking {
            tasks.document(taskId).get()
                .addOnSuccessListener{ document ->
                    if (document.exists()) {
                        result = Task(
                            taskId = document.id,
                            taskName = document.data?.get("taskName").toString(),
                            groupId = document.data?.get("groupId").toString(),
                            cycle = document.data?.get("cycle").toString(),
                            assignerId = document.data?.get("assignerId").toString(),
                            startDate = Date(), //Date(document.data?.get("startDate").toString()),
                            lastDate = Date()//Date(document.data?.get("lastDate").toString())
                        )
                    }

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
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
//                    startDate = document.data?.get("startDate").toString()
                )

                result.add(task)
            }
        }

        return result
    }
}