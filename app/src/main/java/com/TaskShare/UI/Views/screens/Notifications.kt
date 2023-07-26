package com.example.greetingcard.screens

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.Models.DataObjects.Activity
import com.TaskShare.Models.Utilities.ActivityType
import com.TaskShare.ViewModels.ActivityViewModel
import com.TaskShare.ViewModels.TaskViewModel
import com.example.greetingcard.R
import java.text.DateFormat

@Composable
fun RenderActivityIcon(activity: Activity){

    var id = R.drawable.ic_friend_request

    if(activity.type == ActivityType.GROUP_REQUEST) {
        id = R.drawable.ic_added_to_group
    } else if (activity.type == ActivityType.TASK_ASSIGNED) {
        id = R.drawable.ic_new_task
    } else if (activity.type == ActivityType.TASK_CHANGED) {
        id = R.drawable.ic_edited
    } else if (activity.type == ActivityType.TASK_DUE) {
        id = R.drawable.ic_duedate
    }

    Icon(
        painterResource(id = id),
        contentDescription = "Activity Icon",
        modifier = Modifier
            .size(50.dp)
    )


}
@Composable
fun RenderActivityCard(activity: Activity, taskViewModel: TaskViewModel, goToDetail: () -> Unit){
    Row(
        modifier = Modifier
            .absolutePadding(10.dp, 0.dp, 10.dp, 0.dp)
            .padding(10.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    if ((activity.type == ActivityType.TASK_TRANSFER ||
                        activity.type == ActivityType.TASK_CHANGED ||
                        activity.type == ActivityType.TASK_DUE)
                    ) {
                        taskViewModel.setDetailTaskInfo(activity.taskId)
                        goToDetail()
                    }
                    if(activity.type == ActivityType.TASK_ASSIGNED && !activity.details.contains("You are invited")) {
                        taskViewModel.setDetailTaskInfo(activity.taskId)
                        goToDetail()
                    }
                }

            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .padding(10.dp, 0.dp)
            .widthIn(10.dp, 140.dp))
        {
            RenderActivityIcon(activity = activity)
        }
        Column(modifier = Modifier.widthIn(10.dp, 300.dp))
        {
            Text(text = activity.details,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(2.dp, 0.dp, 0.dp, 5.dp)
            )
            var df = DateFormat.getDateInstance(DateFormat.LONG);

            Text(text = df.format(activity.time),
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(2.dp, 0.dp, 0.dp, 5.dp)
            )
        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationScreen(taskViewModel: TaskViewModel, goToDetail: () -> Unit) {
    val viewModel = viewModel(ActivityViewModel::class.java)
    //getting data
    val state by viewModel.state
    val scrollState = rememberScrollState()
    viewModel.getAllActivitiesForUser()

    androidx.compose.material.Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Activity", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue))
        )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .absolutePadding(0.dp, 20.dp, 0.dp, 0.dp)
                .background(Color.White)
                .verticalScroll(state = scrollState),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                val checkedState = remember { mutableStateOf(true) }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(text = "Notifications Enabled",
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .absolutePadding(20.dp, 0.dp, 0.dp, 0.dp))
                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = it },
                        colors  = SwitchDefaults.colors(
                            checkedTrackColor = colorResource(id = R.color.grey),
                            checkedThumbColor = colorResource(id = R.color.banner_blue),
                            uncheckedThumbColor = colorResource(id = R.color.banner_blue)

                        ),
                    )
                }
                Spacer(modifier = Modifier.height(height = 10.dp))
                if(viewModel.activitiesState.value.activities.isNotEmpty() && DateUtils.isToday(viewModel.activitiesState.value.activities[0].time.time)) {
                    Text(text = "Today",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .absolutePadding(20.dp, 0.dp, 0.dp, 0.dp))
                    viewModel.activitiesState.value.activities.forEach{
                        if(DateUtils.isToday(it.time.time)) {
                            RenderActivityCard(activity = it, taskViewModel, goToDetail)
                            Spacer(modifier = Modifier.height(height = 10.dp))
                        }
                    }
                }

                if(viewModel.activitiesState.value.activities.find{ temp -> !DateUtils.isToday(temp.time.time) } != null) {
                    Text(text = "Earlier",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .absolutePadding(20.dp, 0.dp, 0.dp, 0.dp))
                }

                viewModel.activitiesState.value.activities.forEach{
                    if(!DateUtils.isToday(it.time.time)) {
                        RenderActivityCard(activity = it, taskViewModel, goToDetail)
                        Spacer(modifier = Modifier.height(height = 10.dp))
                    }
                }

            }

        }
    })

}


