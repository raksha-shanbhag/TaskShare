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
    }

    fun getActivityString(activity: Activity): String {
        if (activity.type == ActivityType.FRIEND_REQUEST) {
            var userInfo = usersRepository.getUserInfo(activity.sourceUser)
            return "${userInfo.firstName} sent you a friends request!"
        } else if (activity.type == ActivityType.GROUP_REQUEST) {
            var userInfo = usersRepository.getUserInfo(activity.sourceUser)
            var groupInfo = groupsRepository.getGroupFromId(activity.groupId)
            return "${userInfo.firstName} added you to the group ${groupInfo.groupName}!"
        } else if (activity.type == ActivityType.TASK_ASSIGNED) {
            return "A new task has been assigned to you!"
        } else if (activity.type == ActivityType.TASK_CHANGED) {
            var taskInfo = taskRepository.getTask(activity.taskId)
            return "${taskInfo.taskName} has been updated!"
        } else if (activity.type == ActivityType.TASK_DUE) {
            var taskInfo = taskRepository.getTask(activity.taskId)
            return "${taskInfo.taskName} is due soon!"

        }

        return "ERROR"
    }
}