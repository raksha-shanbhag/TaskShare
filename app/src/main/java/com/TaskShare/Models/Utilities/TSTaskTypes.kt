package com.TaskShare.Models.Utilities

enum class TSTaskTypes(str: String) {
    NULL("Error"),
    NOCYCLE("No Cycle"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    var displayString: String = str

    companion object {
        @JvmStatic
        fun fromString(name: String): TSTaskTypes {
            return when (name) {
                "No Cycle" -> TSTaskTypes.NOCYCLE
                "Daily" -> TSTaskTypes.DAILY
                "Weekly" -> TSTaskTypes.WEEKLY
                "Monthly" -> TSTaskTypes.MONTHLY
                else -> TSTaskTypes.NULL
            }
        }
    }

    fun toDisplay(status: TSTaskTypes) : String {
        return when (status)
        {
            TSTaskTypes.NOCYCLE -> "No Cycle"
            TSTaskTypes.DAILY -> "Daily"
            TSTaskTypes.WEEKLY -> "Weekly"
            TSTaskTypes.MONTHLY -> "Monthly"
            else -> "Error"
        }
    }
}