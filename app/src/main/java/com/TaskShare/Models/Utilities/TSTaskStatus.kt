package com.TaskShare.Models.Utilities

// Task Status Enumerator
enum class TSTaskStatus(str: String) {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete"),
    OVERDUE("Overdue"),
    DECLINED("Declined"),
    TRANSFER("Transfer Requested");

    var displayString: String = str

    companion object {
        @JvmStatic
        fun fromString(name: String): TSTaskStatus {
            return when (name)
            {
                "In Progress" -> IN_PROGRESS
                "Complete" -> COMPLETE
                "Overdue" -> OVERDUE
                "Declined" ->  DECLINED
                "Transfer Requested" -> TRANSFER
                else -> TODO
            }
        }

        fun toDisplay(status: TSTaskStatus): String {
            return when (status)
            {
                TODO -> "To Do"
                IN_PROGRESS -> "In Progress"
                COMPLETE -> "Complete"
                OVERDUE -> "Overdue"
                DECLINED -> "Declined"
                TRANSFER -> "Transfer Requested"
            }
        }
    }
}