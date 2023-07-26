package com.TaskShare.Models.Services

import com.TaskShare.Models.DataObjects.Activity
import com.TaskShare.Models.Utilities.ActivityType
import com.TaskShare.Models.Repositories.TSAcivityRepository
import com.TaskShare.Models.Repositories.TSGroupsRepository
import com.TaskShare.Models.Repositories.TSSubTasksRepository
import com.TaskShare.Models.Repositories.TSTasksRepository
import com.TaskShare.Models.Repositories.TSUsersRepository

class ActivityManagementService {
    companion object {
        private val groupsRepository = TSGroupsRepository()
        private val taskRepository = TSTasksRepository()
        private val subTaskRepository = TSSubTasksRepository()
        private val usersRepository = TSUsersRepository()
        private val activityRepository = TSAcivityRepository()

        fun addActivity(activity: Activity) {
            var activityId = activityRepository.create(activity)

            if (activityId != null) {
                for (userId in activity.affectedUsers) {
                    usersRepository.addActivity(userId, activityId)
                }
            }
        }

        fun getActivitiesForUser(userId: String): MutableList<Activity> {
            var activityIds = usersRepository.getActivitiesForUserId(userId)
            var activities: MutableList<Activity> = mutableListOf()

            for (activityId in activityIds) {
                var activity = activityRepository.getActivityFromId(activityId)

                if (activity != null) {
                    if (System.currentTimeMillis() - activity.time.time > 259200000) {
                        for (userId in activity.affectedUsers) {
                            usersRepository.removeActivity(userId, activityId)
                        }

                        activityRepository.delete(activityId)
                    } else {
                        activities.add(activity);
                    }
                }
            }

            activities.sortByDescending{ it.time }
            return activities
        }

        fun setActivityNotify(notify: Boolean) {
            TSUsersRepository.setNotifEnabled(TSUsersRepository.globalUserId, notify)
        }
    }
}