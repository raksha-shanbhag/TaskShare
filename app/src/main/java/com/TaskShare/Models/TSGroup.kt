package com.TaskShare.Models

import android.util.Log
import com.TaskShare.ViewModels.AddTaskState
import com.TaskShare.ViewModels.GroupViewState
import com.TaskShare.ViewModels.TaskViewState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
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

        var documents: QuerySnapshot? = null

        try {
            documents = groups.get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents: ", exception)
        }

        if (documents == null) {
            Log.w(TAG, "Error getting documents.")
            return mutableListOf()
        }

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

data class TSGroupData (
    val groupName: String ="",
    val groupDescription: String = "",
    val groupMembers: MutableList<String> = mutableListOf(),
    val tasks: MutableList<String> = mutableListOf()
)

class TSGroup() {
    private var tasks: MutableList<TSTask> = mutableListOf()
    private var members: MutableList<TSUser> = mutableListOf()

    var id = ""
    var groupData = TSGroupData()

    companion object {
        private val TAG = "Group"

        fun getFromId(id: String, recurse: Boolean = true): TSGroup {
            var group = TSGroup()
            group.id = id

            runBlocking {
                group.read(recurse)
            }

            return group
        }

        fun createGroup(data: TSGroupData) : String {
            val ref = Firebase.firestore.collection("Groups")
            var documentId = ""

            runBlocking {
                try {
                    var document = ref.add(data).await()
                    Log.d(TAG, document.id)
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    documentId = document.id
                } catch (exception: Throwable) {
                    Log.w(TAG, "Error writing document", exception)
                }
            }

            return documentId
        }
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

    fun getState(): GroupViewState {
        var userEmails: MutableList<String> = mutableListOf()
        var taskStates: MutableList<TaskViewState> = mutableListOf()
        var incompleteTasks: MutableList<TaskViewState> = mutableListOf()

        for (user in members) {
            userEmails.add(user.userData.email)
        }

        for (task in tasks) {
            for (subTask in task.getSubTasks()) {
                var taskState = subTask.getState()
                taskStates.add(taskState)

                if (subTask.taskStatus == TSSubTask.TSTaskStatus.TODO) {
                    incompleteTasks.add(taskState)
                }
            }
        }

        return GroupViewState(
            groupData.groupName,
            groupData.groupDescription,
            userEmails,
            taskStates,
            incompleteTasks,
            id
        )
    }

    fun updateInfo() {
        if (id.isEmpty()) {
            return
        }

        val dbRef = Firebase.firestore.collection("Groups").document(id)

        dbRef.update("groupName", groupData.groupName)
        dbRef.update("groupDescription", groupData.groupDescription)
    }

    fun updateMember(userId: String, add: Boolean = true) {
        val dbRef = Firebase.firestore.collection("Groups").document(id)

        if (add) {
            dbRef.update("groupMembers", FieldValue.arrayUnion(userId))
                .addOnFailureListener{ exception: Throwable ->
                    Log.w(TAG, "Error adding user to group.", exception)
                }
        } else {
            dbRef.update("groupMembers", FieldValue.arrayRemove(userId))
                .addOnFailureListener{ exception: Throwable ->
                    Log.w(TAG, "Error removing user from group.", exception)
                }
        }
    }

    fun updateTask(taskId: String, add: Boolean = true) {
        val dbRef = Firebase.firestore.collection("Groups").document(id)

        if (add) {
            dbRef.update("tasks", FieldValue.arrayUnion(taskId))
                .addOnFailureListener{ exception: Throwable ->
                    Log.w(TAG, "Error adding user to group.", exception)
                }
        } else {
            dbRef.update("tasks", FieldValue.arrayRemove(taskId))
                .addOnFailureListener{ exception: Throwable ->
                    Log.w(TAG, "Error removing user from group.", exception)
                }
        }
    }

    fun createTask(state: AddTaskState) {
    }

    suspend fun read(recurse: Boolean = true) {
        val ref = Firebase.firestore.collection("Groups").document(id)
        var document: DocumentSnapshot? = null

        try {
            document = ref.get().await()
        } catch (exception: Throwable) {
            Log.w(TAG, "Error getting documents.", exception)
            return
        }

        if (document != null && document.exists()) {
            groupData = TSGroupData(
                groupName = document.get("groupName") as String,
                groupDescription = document.get("groupDescription") as String,
                groupMembers = document.get("groupMembers") as MutableList<String>,
                tasks = document.get("tasks") as MutableList<String>
            )

            members.clear()
            for (userId in groupData.groupMembers) {
                members.add(TSUser.getFromId(userId, false))
            }

            tasks.clear()
            if (recurse) {
                for (taskId in groupData.tasks) {
                    var task = TSTask.getFromId(taskId)
                    tasks.add(task)
                }
            }
        }
    }
}
