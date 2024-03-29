package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.Task
import com.TaskShare.Models.DataObjects.UpdateLog
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
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

    // API Service for creating a Task
    fun createTask(
        assignerId: String,
        taskName: String,
        groupId: String,
        cycle: String,
        lastDate: Date,
        assignees: MutableList<String>,
        startDate: Date,
        updateLog: MutableList<UpdateLog>
    ) : String {
        var documentId: String = ""

        var data = hashMapOf(
            "taskName" to taskName,
            "cycle" to cycle,
            "lastDate" to lastDate,
            "groupId" to groupId,
            "assignerId" to assignerId,
            "startDate" to startDate,
            "assignees" to assignees,
            "creationDate" to Date(),
            "updateLog" to updateLog,
            "currentIndex" to 0
        )


        runBlocking {
            var document = tasks.add(data).await()
                if(document != null){
                    documentId = document.id
                    Log.d(TAG, document.id)
                    Log.d(TAG, "DocumentSnapshot successfully written!")
//                    ActivityManagementService().addActivity(Activity(Date(), documentId, groupId, assignees, assignerId, ActivityType.GROUP_REQUEST))

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
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("lastDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()

                val index: Int = document.data?.get("currentIndex") as? Int ?: 0

                result = Task(
                    taskId = document.id,
                    taskName = document.data?.get("taskName").toString(),
                    groupId = document.data?.get("groupId").toString(),
                    cycle = document.data?.get("cycle").toString(),
                    assignerId = document.data?.get("assignerId").toString(),
                    startDate = startDate,
                    lastDate = endDate,
                    assignees = assignees,
                    currentIndex = index
                )
            } else {
                Log.w(TAG, "Error writing document")
            }
        }

        return result
    }

    // API service to get tasks for a group Id
    fun getTasksForGroupId(groupId: String): MutableList<Task> {
        var result = mutableListOf<Task>()

        runBlocking {
            var documentSnapshot = tasks.whereEqualTo("groupId", groupId).get().await()
            for (document in documentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("lastDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()

                val index: Int = document.data?.get("currentIndex") as? Int ?: 0

                var task = Task(
                    taskId = document.id,
                    taskName = document.data?.get("taskName").toString(),
                    groupId = groupId,
                    cycle = document.data?.get("cycle").toString(),
                    assignerId = document.data?.get("assignerId").toString(),
                    startDate = startDate,
                    lastDate = endDate,
                    currentIndex = index
                )

                result.add(task)
            }
        }

        return result
    }

    // APIs to get group Tasks for Assignee Id
    fun getGroupTasksForAssignee(groupId: String, assigneeId: String): MutableList<Task> {
        var result = mutableListOf<Task>()

        runBlocking {
            var documentSnapshot = tasks
                .whereEqualTo("groupId", groupId)
                .whereArrayContains("assignees", assigneeId)
                .get().await()

            for (document in documentSnapshot.documents) {
                val startDateTimestamp = document.data?.get("startDate") as? Timestamp
                val endDateTimestamp = document.data?.get("lastDate") as? Timestamp

                val startDate = startDateTimestamp?.toDate()?: Date()
                val endDate = endDateTimestamp?.toDate()?: Date()

                val index: Int = document.data?.get("currentIndex") as? Int ?: 0

                var task = Task(
                    taskId = document.id,
                    taskName = document.data?.get("taskName").toString(),
                    groupId = groupId,
                    cycle = document.data?.get("cycle").toString(),
                    assignerId = document.data?.get("assignerId").toString(),
                    startDate = startDate,
                    lastDate = endDate,
                    currentIndex = index
                )

                result.add(task)
            }
        }

        return result
    }

    // API to get taskIds
    fun getTaskIdsForGroupId(groupId: String): MutableList<String> {
        var result = mutableListOf<String>()

        runBlocking {
            var documentSnapshot = tasks.whereEqualTo("groupId", groupId).get().await()
            for (document in documentSnapshot.documents) {
                result.add(document.id)
            }

        }

        return result
    }

    // API Service for removing a member from a Task
    fun removeAssigneeFromTask(taskId: String, assigneeId: String) {
        runBlocking {
            tasks.document(taskId)
                .update("assignees", FieldValue.arrayRemove(assigneeId))
                .await()
        }
    }

    // API Service for updating Task information
    fun updateTaskInfo(taskId: String, taskName: String, cycle: String, endDate: Date, updateIndex: Int, assignees: MutableList<String>) {
        var data = hashMapOf(
            "taskName" to taskName,
            "cycle" to cycle,
            "lastDate" to endDate,
            "currentIndex" to updateIndex,
            "assignees" to assignees
        )

        runBlocking {
            tasks.document(taskId).update(data.toMap()).await()
        }
    }
}