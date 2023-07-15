package com.TaskShare.Models.Repositories

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.TaskShare.Models.DataObjects.Group
import kotlinx.coroutines.runBlocking

// APIs
class TSGroupsRepository {
    private val TAG = "TSGroupsRepository"
    private val db = Firebase.firestore
    private val groups = db.collection("Groups")

    // API method to create a group
    fun createGroup(
        creatorId: String,
        groupName: String,
        groupDescription: String,
        groupMembersIds: MutableList<String>
    ): String {
        var documentId: String = ""
        val newRequestGroup = hashMapOf(
            "groupName" to groupName,
            "groupDescription" to groupDescription,
            "groupMembers" to groupMembersIds,
            "createdBy" to creatorId
        )

        Log.i("Raksha Debug", groupName)
        runBlocking {
            groups.add(newRequestGroup)
                .addOnSuccessListener { document ->
                    documentId = document.id
                    Log.d(TAG, document.id)
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        return documentId
    }

    // API method to get all the groups for a user
    suspend fun getAllGroups(): MutableList<Group> {
        var result = ArrayList<Group>()

        var documents = groups.get().await()
        for (document in documents) {
            var groupMembers = ArrayList<String>()
            groupMembers.addAll(document.get("groupMembers") as List<String>)

            var group = Group(
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
        runBlocking {
            groups.document(groupId)
                .update("groupMembers", FieldValue.arrayUnion(memberId))
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    // API method to add member
    fun addMemberToGroup(groupId: String, newMemberUserId: String) {
        runBlocking {
            groups.document(groupId)
                .update("groupMembers", FieldValue.arrayUnion(newMemberUserId))
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    // API to get group from Id
    fun getGroupFromId(groupId: String): Group {
        if (groupId.isEmpty()) {
            return Group()
        }
        var result = Group()
        runBlocking {
            var document = groups.document(groupId)
                .get()
                .await()

            if(document != null) {
                var groupMembers = ArrayList<String>()
                groupMembers.addAll(document.get("groupMembers") as List<String>)

                result = Group(
                    id = document.id,
                    groupName = document.get("groupName").toString(),
                    groupDescription = document.get("groupDescription").toString(),
                    groupMembers = groupMembers
                )
            }
        }
        return result
    }

    // get Group Members for a given group Id
    fun getGroupMembersFromGroupId(groupId: String): MutableList<String> {
        var result = ArrayList<String>()
        runBlocking {
            groups.document(groupId)
                .get()
                .addOnSuccessListener { document ->
                    result.addAll(document.get("groupMembers") as List<String>)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }

        return result.toMutableList()
    }
}