package com.TaskShare.Models.Utilities

enum class ActivityType(str: String) {
    NULL("Error"),
    FRIEND_REQUEST("Friend Request"),
    GROUP_REQUEST("Group Request"),
    TASK_ASSIGNED("Task Assigned"),
    TASK_CHANGED("Task Changed"),
    TASK_DUE("Task Due");

    var displayString: String = str

    companion object {
        @JvmStatic
        fun fromString(name: String): ActivityType {
            return when (name)
            {
                "Friend Request" -> FRIEND_REQUEST
                "Group Request" -> GROUP_REQUEST
                "Task Assigned" -> TASK_ASSIGNED
                "Task Changed" -> TASK_CHANGED
                "Task Due" -> TASK_DUE
                else -> NULL
            }
        }
    }
}