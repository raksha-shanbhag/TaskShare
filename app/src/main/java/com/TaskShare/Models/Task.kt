package com.TaskShare.Models

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Task(groupRef: Group, taskId: String, ref: CollectionReference) {
    private val TAG = "Task"
    private val group = groupRef
    private val id = taskId
    private val dataRef = ref.document(id)
    private var assignees: HashSet<String> = hashSetOf()
    private var subTasks: HashSet<SubTask> = hashSetOf()

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
                    subTasks.plus(SubTask(this, document.id, dataRef.collection("SubTasks")))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}