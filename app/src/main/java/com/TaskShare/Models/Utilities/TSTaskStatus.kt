package com.TaskShare.Models.Utilities

// Task Status Enumerator
enum class TSTaskStatus(str: String) {
    NULL("Error"),
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    PENDING_APPROVAL("Pending Approval"),
    COMPLETE("Complete"),
    OVERDUE("Overdue"),
    DECLINED("Declined");

    var displayString: String = str

    companion object {
        @JvmStatic
        fun fromString(name: String): TSTaskStatus {
            return when (name)
            {
                "To Do" -> TODO
                "In Progress" -> IN_PROGRESS
                "Pending Approval" -> PENDING_APPROVAL
                "Complete" -> COMPLETE
                "Overdue" -> OVERDUE
                "Declined" ->  DECLINED
                else -> NULL
            }
        }
    }
}