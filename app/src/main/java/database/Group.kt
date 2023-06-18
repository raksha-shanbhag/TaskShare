package database

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Group(groupId: String) {
    private var TAG = "MyActivity"
    private var id = groupId
    private var users: HashSet<String> = hashSetOf()
    private var tasks: HashSet<String> = hashSetOf()

    fun getId(): String {
        return id
    }

    fun createGroup() {
        val db = Firebase.firestore
        val docRef = db.collection("Groups").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "Group already exists.")
                } else {
                    val data = hashMapOf(
                        "Users" to users.toList(),
                        "Tasks" to tasks.toList()
                    )
                    docRef.set(data)
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error creating group.", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun getUsers(): HashSet<String> {
        if (users.isEmpty()) {
            update()
        }
        return users
    }

    fun updateUser(userId: String, add: Boolean = true): Boolean {
        val db = Firebase.firestore
        val dbRef = db.collection("Groups").document(id)

        var success = true
        val failureListener = { exception: Throwable ->
            Log.w(TAG, "Error updating users in group.", exception)
            success = false
        }

        if (add) {
            dbRef.update("Users", FieldValue.arrayUnion(userId))
                .addOnFailureListener(failureListener)
        } else {
            dbRef.update("Users", FieldValue.arrayRemove(userId))
                .addOnFailureListener(failureListener)
        }

        return success
    }

    private fun update() {
        val db = Firebase.firestore
        users.clear()
        tasks.clear()

        db.collection("Groups").document(id)
            .get()
            .addOnSuccessListener { result ->
                users.plus(result.get("Users"))
                tasks.plus(result.get("Tasks"))
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
