package com.TaskShare.Models.Repositories

import android.util.Log
import com.TaskShare.Models.DataObjects.Activity
import com.TaskShare.Models.DataObjects.ActivityType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class TSAcivityRepository {
    private val TAG = "TSGroupsRepository"
    private val activities = Firebase.firestore.collection("Activities")

    fun create(activity: Activity): String? {
        var documentReference: DocumentReference? = null
        val newActivity = hashMapOf(
            "time" to activity.time,
            "taskId" to activity.taskId,
            "groupId" to activity.groupId,
            "affectedUsers" to activity.affectedUsers,
            "sourceUser" to activity.sourceUser,
            "type" to activity.type.displayString,
        )

        runBlocking {
            try {
                documentReference = activities.add(newActivity).await()
            } catch (e: Throwable) {
                Log.w("Error creating document", e)
            }
        }

        if (documentReference == null) {
            return null
        }

        return documentReference!!.id
    }

    fun delete(activityId: String) {
        activities.document(activityId).delete()
    }

    fun getActivityFromId(activityId: String): Activity? {
        var document: DocumentSnapshot? = null

        runBlocking {
            try {
                document = activities.document(activityId).get().await()
            } catch (e: Throwable) {
                Log.w("Error getting documents", e)
            }
        }

        if (document == null) {
            return null
        }

        var doc = document!!

        return Activity(
            time = (doc.get("time") as Timestamp).toDate(),
            taskId = doc.get("taskId") as String,
            affectedUsers = doc.get("affectedUsers") as MutableList<String>,
            groupId = doc.get("groupId") as String,
            sourceUser = doc.get("sourceUser") as String,
            type = ActivityType.fromString(doc.get("type") as String)
        )
    }
}