package com.TaskShare.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.TaskShare.Models.DataObjects.Activity
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.Models.Services.ActivityManagementService

class ActivityViewModel: ViewModel() {
    val state = mutableStateOf(Activity())
    val activitiesState = mutableStateOf(ActivitiesViewState())

    fun getAllActivitiesForUser() {
        activitiesState.value.activities.clear()
        var allActivities = mutableListOf<Activity>()
        allActivities.addAll(ActivityManagementService.getActivitiesForUser(TSUsersRepository.globalUserId))

        for (activity in allActivities) {
            appendNewActivity(activity)
        }
    }

    fun appendNewActivity(activity: Activity) {
        val currentList = activitiesState.value.activities
        currentList.add(activity)
        activitiesState.value = activitiesState.value.copy(activities = currentList)
    }

    fun setNotificationEnabled(enable: Boolean) {
        ActivityManagementService.setActivityNotify(enable);
    }

    fun getNotificationEnabled(): Boolean {
        return ActivityManagementService.getActivityNotify()
    }
}


data class ActivitiesViewState (
    val activities: MutableList<Activity> = mutableListOf()
)
