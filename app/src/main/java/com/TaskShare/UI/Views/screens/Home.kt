package com.example.greetingcard.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.TaskShare.Models.Utilities.TSTaskStatus
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R


@Composable
fun RenderStatusMsg(incomplete: Int, total: Int) {
    var textCol = R.color.completed_tasks
    var statusMessage = "All tasks complete!"

    if(incomplete != 0) {
        textCol = R.color.incomplete_tasks
        statusMessage = "$incomplete/$total tasks left"
    }

    Text(text = statusMessage,
        color = colorResource(id = textCol),
        fontSize = small_font_size.sp,
        modifier = Modifier
            .padding(2.dp, 0.dp, 0.dp, 5.dp)
    )
}

@Composable
fun RenderTasksList(incompleteTasks: List<TaskViewState>) {
    var collapse = incompleteTasks.size > 2
    if(collapse) {
        var leftOver = incompleteTasks.size - 2
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (i in 1..2) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .size(6.dp)
                            .background(Color.Black, shape = CircleShape),
                    )

                    Text(incompleteTasks[i].taskName)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(6.dp)
                        .background(Color.Black, shape = CircleShape),
                )

                Text("$leftOver more tasks")
            }
        }

    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            incompleteTasks.forEach() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .size(4.dp)
                            .background(Color.Black, shape = CircleShape),
                    )

                    Text("${it.taskName}")
                }
            }
        }
    }
}

@Composable
fun RenderGroupCard(group_name: String, incompleteTasksNum: Int, tasksNum: Int, incompleteTasks: List<TaskViewState>, showDetail: () -> Unit, viewModel: GroupViewModel, id: String){
    var bg_col = R.color.pink
    if(incompleteTasksNum == 0) {
        bg_col = R.color.group_progress
    }

    Row(
        modifier = Modifier
            .absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            .background(
                colorResource(id = bg_col),
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    viewModel.setGroupId(id);
                    showDetail();
                }

            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .padding(10.dp, 0.dp)
            .widthIn(10.dp, 140.dp))
        {
            Icon(
                painterResource(id = R.drawable.baseline_groups_24),
                contentDescription = "Default Group Icon",
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        colorResource(id = R.color.white),
                        RoundedCornerShape(10.dp)
                    )
            )
        }
        Column(modifier = Modifier.widthIn(10.dp, 300.dp))
        {
            Text(text = group_name,
                fontSize = small_font_size.sp,
                modifier = Modifier
                    .padding(2.dp, 0.dp, 0.dp, 5.dp)
            )

            RenderStatusMsg(incomplete = incompleteTasksNum, total = tasksNum)
            RenderTasksList(incompleteTasks = incompleteTasks)

        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,

            ) {
            Icon(
                painterResource(id = R.drawable.ic_arrow),
                contentDescription = "group detail"
            )
        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    showDetail: () -> Unit,
    showCreate: () -> Unit,
    viewModel: GroupViewModel
) {

    //getting data
    val state by viewModel.state
    val groupState by viewModel.groupsState
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel) {
        viewModel.getAllGroupsForUser()
    }

    androidx.compose.material.Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "My Groups", color = Color.White, fontSize = 30.sp) },
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
                Button(onClick = showCreate,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.banner_blue),
                        contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
                ) {
                    Row( horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Create New Group", fontSize = MaterialTheme.typography.h6.fontSize)
                    }

                }
                Spacer(modifier = Modifier.height(height = 10.dp))
                //using data
                groupState.groups.forEach{
                    val incompleteTasks = it.tasks.filter { task -> task.status != "Complete" }

                    RenderGroupCard(
                        group_name = it.groupName,
                        incompleteTasksNum = incompleteTasks.size,
                        tasksNum = it.tasks.size,
                        incompleteTasks = incompleteTasks,
                        showDetail = showDetail,
                        viewModel = viewModel,
                        id = it.id
                    )

                    Spacer(modifier = Modifier.height(height = 10.dp))
                }
                Spacer(modifier = Modifier.height(height = 30.dp))
            }


        }
    })

}


