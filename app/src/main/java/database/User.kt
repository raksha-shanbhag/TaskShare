package database

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User(userId: String) {
    private var TAG = "MyActivity"
    private var id = userId
    private var groups: HashSet<String> = hashSetOf();

    fun createUser() {
        val db = Firebase.firestore
        val docRef = db.collection("Groups").document(id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "User already exists.")
                } else {
                    val data = hashMapOf(
                        "Groups" to groups.toList(),
                    )
                    docRef
                        .set(data)
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error creating user.", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun getGroups(): Set<String> {
        if (groups.isEmpty()) {
            update()
        }

        return groups;
    }

    fun updateGroup(group: Group, add: Boolean = true): Boolean {
        val db = Firebase.firestore
        val dbRef = db.collection("Users").document(id)

        var success = true
        val failureListener = { exception: Throwable ->
            Log.w(TAG, "Error updating groups in user.", exception)
            success = false
        }

        if (add) {
            dbRef.update("Groups", FieldValue.arrayUnion(group.getId()))
                .addOnFailureListener(failureListener)
        } else {
            dbRef.update("Groups", FieldValue.arrayRemove(group.getId()))
                .addOnFailureListener(failureListener)
        }

        Log.w(TAG, "Test1")
        if (!group.updateUser(id, add)) {
            dbRef.update("Groups", FieldValue.arrayRemove(group.getId()))
                .addOnFailureListener(failureListener)
        }

        return success
    }

    private fun update() {
        val db = Firebase.firestore
        groups.clear()

        db.collection("Users").document(id)
            .get()
            .addOnSuccessListener { result ->
                groups.plus(result.get("Groups"))
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
