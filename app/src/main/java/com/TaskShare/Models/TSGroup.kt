package com.TaskShare.Models

import android.util.Log
import com.TaskShare.ViewModels.TaskViewState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class RequestGroup(
    val groupName: String ="",
    val groupDescription: String = "",
    val member: String = "",
    val groupMembers: MutableList<String> = mutableListOf(),
) {}

data class ResponseGroup(
    val id: String = "",
    val groupName: String ="",
    val groupDescription: String = "",
    val member: String = "",
    val groupMembers: MutableList<String> = mutableListOf(),
    val tasks: MutableList<TaskViewState> = mutableListOf(),
    val incompleteTasks: MutableList<TaskViewState> = mutableListOf()
)
class TSGroupsAPI {
    private val TAG = "TSGroupAPI"
    private val db = Firebase.firestore
    private val groups = db.collection("Groups")
    private val userApi = TSUserApi()

    // API method to create a group
    fun createGroup(
        groupName: String,
        groupDescription: String,
        groupMembers: MutableList<String>
    ) : String {
        var documentId: String = ""
        val newRequestGroup = RequestGroup(
            groupName = groupName,
            groupDescription = groupDescription,
            groupMembers = userApi.getUserIdsFromNames(groupMembers)
        )

        Log.i("Raksha Debug", groupName)
        groups.add(newRequestGroup)
            .addOnSuccessListener { document ->
                documentId = document.id
                Log.d(TAG, document.id)
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        addTasksToGroup(documentId, "Testing", "33/33/33", "None")
        return documentId
    }

    // API method to get all the groups
    suspend fun getAllGroups(): MutableList<ResponseGroup> {
        var result = ArrayList<ResponseGroup>()

        groups.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var groupMembers = ArrayList<String>()
                    groupMembers.addAll(document.get("groupMembers") as List<String>)

                    var group = ResponseGroup(
                        id = document.id,
                        groupName = document.data["groupName"].toString(),
                        groupDescription = document.data["groupDescription"].toString(),
                        groupMembers = groupMembers
                    )

                    result.add(group)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
            .await()

        return result.toMutableList()
    }

    // API method to remove member
    fun removeUserFromGroup(groupId: String, memberId: String) {
        groups.document(groupId)
            .update("groupMembers", FieldValue.arrayUnion(memberId))
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    // API method to add member
    fun addMemberToGroup(groupId: String, memberEmail: String) {
        val newUserId = userApi.getUserIdFromEmail(memberEmail)
        groups.document(groupId)
            .update("groupMembers", FieldValue.arrayUnion(newUserId))
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    // API to get group from Id
    fun getGroupFromId(groupId: String): ResponseGroup {
        var result = ResponseGroup()
        groups.document(groupId)
            .get()
            .addOnSuccessListener{ document ->
                if(document.exists()) {
                    var groupMembers = ArrayList<String>()
                    groupMembers.addAll(document.get("groupMembers") as List<String>)

                    result = ResponseGroup(
                        id = document.id,
                        groupName = document.data?.get("groupName").toString(),
                        groupDescription = document.data?.get("groupDescription").toString(),
                        groupMembers = groupMembers
                    )
                }
            }
        return result
    }

    // API to add Tasks
    fun addTasksToGroup(groupId: String, taskName: String, deadline: String, cycle: String) {
        var data = hashMapOf(
            "name" to taskName,
            "deadline" to deadline,
            "cycle" to cycle,
        )
        groups.document(groupId)
            .collection("Tasks")
            .document("testing")
            .set(data)
    }

}

class TSGroup(groupId: String) {
    private val TAG = "Group"
    private val id = groupId
    private var users: HashSet<TSUser> = hashSetOf()
    private var tasks: HashSet<TSTask> = hashSetOf()
    private var name = groupId

    fun getId(): String {
        return id
    }

    fun create(groupDescription: String) {
        Log.i(TAG, id)
        val db = Firebase.firestore
        val docRef = db.collection("Groups").document(id)
        var doesExist = false

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.w(TAG, "Group already exists.")
                    doesExist = true
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        if (!doesExist) {
            var userIds: HashSet<String> = hashSetOf()
            for (user in users) {
                userIds.add(user.getId())
            }

            val data = hashMapOf(
                "Users" to users.toList(),
                "Name" to name,
                "Description" to groupDescription
            )

            docRef.set(data)
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error creating group.", exception)
                }
        }

    }

    fun getTaskCollection(): CollectionReference {
        return Firebase.firestore.collection("Groups").document(id).collection("Tasks")
    }

    fun getSubTasksAssignedTo(userId: String): Set<TSSubTask> {
        var set: HashSet<TSSubTask> = hashSetOf()

        for (task in tasks) {
            if (task.isAssignedTo(userId)) {
                set.addAll(task.getSubTasksAssignedTo(userId))
            }
        }

        return set
    }

    fun updateUser(user: TSUser, add: Boolean = true): Boolean {
        val db = Firebase.firestore
        val dbRef = db.collection("Groups").document(id)

        var success = true
        val failureListener = { exception: Throwable ->
            Log.w(TAG, "Error updating users in group.", exception)
            success = false
        }

        if (add) {
            dbRef.update("Users", FieldValue.arrayUnion(user.getId()))
                .addOnFailureListener(failureListener)
        } else {
            dbRef.update("Users", FieldValue.arrayRemove(user.getId()))
                .addOnFailureListener(failureListener)
        }

        if (success) {
            users.add(user)
        }

        return success
    }

    fun get() {
        val ref = Firebase.firestore.collection("Groups").document(id)

        ref.get()
            .addOnSuccessListener { result ->
                set(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        ref.collection("Tasks")
            .get()
            .addOnSuccessListener { result ->
                setTasks(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    suspend fun synchronisedGet() {
        val ref = Firebase.firestore.collection("Groups").document(id)
        var result: DocumentSnapshot? = null
        var result2: QuerySnapshot? = null

        try {
            result = ref.get().await()
            result2 = ref.collection("Tasks").get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (result != null && result2 != null) {
            set(result)
            setTasks(result2)
        }
    }

    private fun set(result: DocumentSnapshot) {
        var userIds: HashSet<String> = hashSetOf()
        userIds.addAll(result.get("Users") as List<String>)

        var set: HashSet<TSUser> = hashSetOf()

        for (userId in userIds) {
            var flag = false
            for (user in users) {
                if (user.getId() == userId) {
                    set.add(user)
                    flag = true
                    break
                }
            }

            if (!flag) {
                set.add(TSUser(userId))
            }
        }

        users = set
    }

    private fun setTasks(result: QuerySnapshot) {
        var set: HashSet<TSTask> = hashSetOf()

        for (document in result) {
            var flag = false

            for (task in tasks) {
                if (task.getId() == document.id) {
                    set.add(task)
                    flag = true
                    break
                }
            }

            if (!flag) {
                set.add(TSTask(this, document.id))
            }
        }

        tasks = set
    }
}
