package com.TaskShare.Models.Utilities

enum class TSFriendStatus(str: String) {
    INCOMING("Incoming"),
    OUTGOING("Outgoing"),
    FRIEND("Friend"),
    BLOCKED("Blocked"),
    NULL("Error");

    var displayString: String = str

    companion object {
        @JvmStatic
        fun fromString(name: String): TSFriendStatus {
            return when (name)
            {
                "INCOMING" -> INCOMING
                "OUTGOING" -> OUTGOING
                "FRIEND" -> FRIEND
                "BLOCKED" -> BLOCKED
                else -> NULL
            }
        }

        // to check if sender is of either status in receiver's list.
        // If not, Friend request goes through
        fun senderFriendRequestErrorFromReceiverFriendStatus(status: TSFriendStatus): String {
            return when (status) {
                INCOMING -> "Already sent request"
                OUTGOING -> "Received friend request from user. Accept it to become friends"
                FRIEND -> "Already Friend"
                BLOCKED -> "Previously Blocked by Friend"
                NULL -> "Issues adding Friend"
            }
        }
    }
}