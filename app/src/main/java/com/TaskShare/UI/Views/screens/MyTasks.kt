package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.TaskViewModel
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

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

    if (status == "todo") {
        text_print = "To Do"
        bg_color = R.color.progress_red
    }
    if (status == "inprogress") {
        text_print = "In Progress"
        bg_color = R.color.progress_yellow
    }
    if (status == "done") {
        text_print = "Done"
        bg_color = R.color.progress_green
    }
    RenderPills(text_print, bg_color)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RenderTaskCard(taskInfo: TaskViewState,  viewModel: TaskViewModel, showDetail: () -> Unit){
//    Button(onClick = showDetail){
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
//                    .background(
//                        colorResource(id = R.color.progress_green),
//                    )
//                    ,
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
                }

                Column(
//                    modifier = Modifier.background(
//                        colorResource(id = R.color.progress_yellow),
//                    ),
//                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,

                    ) {
                    RenderStatus(taskInfo.status)
                    Spacer(modifier = Modifier.height(height = 5.dp))

                    Text(
                        text = taskInfo.deadline,
                        fontSize = small_font_size.sp,
                        modifier = Modifier
                            .widthIn(min_width_pill.dp, max_width_pill.dp)
                            .background(
                                colorResource(id = R.color.white),
                                RoundedCornerShape(3.dp)
                            )
                            .padding(10.dp, 2.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)

            ) {
//                Row(
//                    modifier = Modifier
//                    .background(
//                        colorResource(id = R.color.progress_red),
//                    )
//
//                ){
//
//
//                }


                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Assignees: ",
                        fontSize = small_font_size.sp,
                        modifier = Modifier
                        .padding(0.dp, 0.dp, 10.dp, 10.dp)
//                        .padding(10.dp, 2.dp)
                    )

                    taskInfo.assignees.forEach {
                        Text(
                            text = it,
                            fontSize = small_font_size.sp,
                            color = colorResource(id = R.color.banner_blue),
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 10.dp, 10.dp)
                                .background(
                                    colorResource(id = R.color.white),
                                    RoundedCornerShape(3.dp)
                                )
                                .padding(10.dp, 2.dp)
                        )
                    }

                }
            }
        }

    }
//    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyTasksScreen(showDetail: () -> Unit, viewModel: TaskViewModel) {
    //getting data
//    val viewModel = viewModel(TaskViewModel::class.java)
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
                Column(modifier = Modifier.fillMaxSize()) {
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
                            text = "0",
                            fontSize = mid_font_size.sp,
                        )
                    }
                    Row(Modifier.padding(10.dp)){
                        Text(text = "Sort By: ",
                            fontSize = mid_font_size.sp)
                        Text(text = "Groups",
                            fontSize = mid_font_size.sp)
                    }

                    // pull task data

                    var userTasks = viewModel.getTasks()
                    userTasks.forEach{
                        RenderTaskCard(it, viewModel, showDetail)
                        Spacer(modifier = Modifier.height(height = 10.dp))
                    }

                }
            }
        }
    )
}

