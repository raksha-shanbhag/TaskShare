package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TSTask(groupRef: TSGroup, taskId: String) {
    private val TAG = "Task"
    private val group = groupRef
    private val id = taskId
    private val dataRef = group.getTaskCollection().document(id)
    private var assignees: HashSet<String> = hashSetOf()
    private var subTasks: HashSet<TSSubTask> = hashSetOf()

    fun create() {
        val db = Firebase.firestore

        dataRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "Group already exists.")
                } else {
                    val data = hashMapOf(
                        "Assignees" to assignees.toList()
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

    fun getSubTaskCollection(): CollectionReference {
        return dataRef.collection("SubTasks")
    }

    private fun get() {
        dataRef.get()
            .addOnSuccessListener { result ->
                assignees.plus(result.get("Assignees"))
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        dataRef.collection("SubTasks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    subTasks.plus(TSSubTask(this, document.id))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}