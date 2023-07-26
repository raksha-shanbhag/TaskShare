package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.TaskShare.ViewModels.TaskViewModel
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R
import java.text.SimpleDateFormat

var min_width_pill = 100
var max_width_pill = 400
var mid_font_size = 20
var small_font_size = 18


@Composable
fun RenderPills(text: String, bg_color: Int){
    var text_color = R.color.white

    if (bg_color == R.color.icon_blue || bg_color == R.color.pink){
        text_color = R.color.black
    }
    Text(
        text = text,
        color = colorResource(id = text_color),
        fontSize = small_font_size.sp,

        modifier = Modifier
            .widthIn(min_width_pill.dp, max_width_pill.dp)
            .padding(4.dp, 0.dp)
            .background(colorResource(id = bg_color), RoundedCornerShape(3.dp))
            .padding(10.dp, 2.dp)
    )

}
@Composable
fun RenderStatus(status: String){
    var bg_color = R.color.white
    var text_print = ""

    if (status == "To Do") {
        text_print = "To Do"
        bg_color = R.color.progress_red
    }
    else if (status == "Overdue") {
        text_print = "Overdue"
        bg_color = R.color.progress_red
    }
    else if (status == "Declined") {
        text_print = "Declined"
        bg_color = R.color.progress_red
    }
    else if (status == "In Progress") {
        text_print = "In Progress"
        bg_color = R.color.progress_yellow
    }
    else if (status == "Pending Approval") {
        text_print = "Pending Approval"
        bg_color = R.color.progress_yellow
    }
    else if (status == "Complete") {
        text_print = "Complete"
        bg_color = R.color.progress_green
    }
    else{
        text_print = status
        bg_color = R.color.progress_yellow
    }
    RenderPills(text_print, bg_color)
}

@Composable
fun RenderTaskCard(taskInfo: TaskViewState,  viewModel: TaskViewModel, showDetail: () -> Unit){
//    Button(onClick = showDetail){
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    Row(
        modifier = Modifier
            .background(
                colorResource(id = R.color.icon_blue),
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .clickable(

                onClick = {
                    viewModel.setDetailTaskInfo(taskInfo.id)
                    showDetail()
                }
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.baseline_groups_24),
            contentDescription = "Default Group Icon",
            modifier = Modifier
                .background(
                    colorResource(id = R.color.white),
                    RoundedCornerShape(10.dp)
                )
                .padding(14.dp)
        )
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .widthIn(10.dp, 140.dp)
                )
                {
                    Text(
                        text = taskInfo.taskName,
                        fontSize = small_font_size.sp,
                        modifier = Modifier
                            .padding(2.dp, 0.dp, 0.dp, 5.dp)
                    )
                    Text(
                        text = taskInfo.groupName,
                        fontSize = small_font_size.sp,
                        color = colorResource(id = R.color.banner_blue),
                        modifier = Modifier
                            .background(
                                colorResource(id = R.color.white),
                                RoundedCornerShape(3.dp)
                            )
                            .padding(10.dp, 2.dp)
                    )

                    Text(
                        text = "Assignees",
                        fontSize = small_font_size.sp,
                        modifier = Modifier
                            .padding(2.dp, 0.dp, 0.dp, 5.dp)
                    )
                }

                Column(

                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,

                    ) {
                    RenderStatus(taskInfo.status)
                    Spacer(modifier = Modifier.height(height = 5.dp))

                    Text(
                        text = simpleDateFormat.format(taskInfo.deadline),
                        fontSize = small_font_size.sp,
                        modifier = Modifier
                            .widthIn(min_width_pill.dp, max_width_pill.dp)
                            .background(
                                colorResource(id = R.color.white),
                                RoundedCornerShape(3.dp)
                            )
                            .padding(10.dp, 2.dp)
                    )

                    RenderPills(taskInfo.assignee.memberName, R.color.banner_blue)
                }
            }

        }

    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyTasksScreen(showDetail: () -> Unit, viewModel: TaskViewModel) {
    //getting data
    val state by viewModel.state
    val scrollState = rememberScrollState()




    Scaffold(
        topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "My Tasks", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue))

        )}, content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),

                contentAlignment = Alignment.Center
            )
            {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)) {
                    // pull task data

                    var userTasks = viewModel.getTasksForUser()

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                colorResource(id = R.color.banner_blue),
                                RoundedCornerShape(4.dp)
                            )
                            .fillMaxWidth()
                            .padding(15.dp, 10.dp)

                        ) {
                        Text(
                            text = "Tasks Remaining: ",
                            color = colorResource(id = R.color.white),
                            fontSize = mid_font_size.sp,
                            )
                        Text(
                            color = colorResource(id = R.color.banner_blue),
                            modifier = Modifier
                                .padding(10.dp, 0.dp)
                                .drawBehind {
                                    drawCircle(
                                        color = Color.White,
                                        radius = 14.dp.toPx()

                                    )
                                },
                            text = viewModel.getActiveTasksLen().toString(), // TODO backend
                            fontSize = mid_font_size.sp,
                        )
                    }

                    userTasks.forEach{
                        RenderTaskCard(it, viewModel, showDetail)
                        Spacer(modifier = Modifier.height(height = 10.dp))
                    }
                    Spacer(modifier = Modifier.height(height = 30.dp))
                }
            }
        }
    )
}

