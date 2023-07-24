package com.TaskShare.Models.Utilities

import com.TaskShare.Models.DataObjects.UpdateLog
import com.TaskShare.Models.Repositories.TSUsersRepository

class UpdateLogger {
    private var userRepository = TSUsersRepository()

    fun createUpdateLogArray(assignerId : String) : MutableList<UpdateLog> {
        var assignerInfo = userRepository.getUserInfo(assignerId)
        var updateLog = mutableListOf<UpdateLog>()
        updateLog.add(
            UpdateLog(
                name = assignerInfo.firstName,
                userId = assignerInfo.userId,
                update = "Created Task"
            )
        )
        return updateLog
    }
}