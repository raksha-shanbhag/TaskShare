package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TSSubTask(taskRef: TSTask, subTaskId: String) {
    private val TAG = "SubTask"
    private val task = taskRef
    private val id = subTaskId
    private val dataRef = task.getSubTaskCollection().document(id)
    private var assignee: String = "None"

    fun create() {
        val db = Firebase.firestore

        dataRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "Group already exists.")
                } else {
                    val data = hashMapOf(
                        "Assignee" to assignee
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

    private fun get() {
        dataRef.get()
            .addOnSuccessListener { result ->
                assignee = result.get("Assignees").toString()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}