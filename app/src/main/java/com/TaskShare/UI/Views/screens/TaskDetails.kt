package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.TaskShare.ViewModels.TaskViewModel
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskDetailsScreen(onBack: () -> Unit, viewModel: TaskViewModel, editTask: () -> Unit) {
    var taskDetail by remember {
        mutableStateOf(TaskViewState())
    }

    var expandedTransfer by remember {
        mutableStateOf(false)
    }
    var showConfirmTransfer by remember {
        mutableStateOf(false)
    }
    var transferState by remember {
        mutableStateOf("")
    }
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")


    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Task Details", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
            navigationIcon = {
                IconButton(
                    onClick = onBack ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "", tint = Color.White)
                }
            }
        )
    }, content = {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                taskDetail = viewModel.getDetailTaskInfo()
                // Task Name
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(0.dp, 15.dp)
                        .background(
                            colorResource(id = R.color.banner_blue),
                            RoundedCornerShape(4.dp)
                        )
                        .fillMaxWidth()
                        .padding(15.dp, 10.dp)
                        .clickable(onClick = {
                            editTask()
                        })

                ) {

                    Text(
                        text = taskDetail.taskName,
                        color = colorResource(id = R.color.white),
                        fontSize = mid_font_size.sp,
                    )
                    Icon(
                        painterResource(id = R.drawable.edit_icon),
                        contentDescription = "edit task icon",
                        modifier = Modifier.size(size = 24.dp),
                        tint = Color.White
                    )
                }
                // Task Details
                Column(modifier = Modifier
                    .fillMaxSize()
                ) {
                    var nameWidth = 0.35f
                    var valueWidth = 0.65f
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Assigned to", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium,modifier = Modifier.fillMaxWidth(nameWidth))

                        Row(modifier = Modifier
                            .fillMaxWidth(valueWidth)
                            ){
                            RenderPills(taskDetail.assignee, R.color.icon_blue)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Assigned by", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(nameWidth))

                        Row(modifier = Modifier.fillMaxWidth(valueWidth)){
                            RenderPills(taskDetail.assigner, R.color.icon_blue)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Group", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(nameWidth))
                        Row(modifier = Modifier.fillMaxWidth(valueWidth)){
                            RenderPills(taskDetail.groupName, R.color.pink)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Status", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(nameWidth))
                        RenderStatus(taskDetail.status)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Deadline", fontSize = small_font_size.sp,fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(nameWidth))
                        RenderPills(simpleDateFormat.format(taskDetail.deadline), R.color.banner_blue)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Cycle", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(nameWidth))
                        RenderPills(taskDetail.cycle, R.color.banner_blue)
                    }
                    Spacer(modifier = Modifier.height(height = 10.dp))


//                  Transfer Task
                    ExposedDropdownMenuBox(
                        expanded = expandedTransfer,
                        onExpandedChange = {
                            expandedTransfer = !expandedTransfer
                        },
                        modifier = Modifier
                            .border(
                            BorderStroke(
                                    2.dp,
                                    SolidColor(colorResource(id = R.color.banner_blue))
                            )
                        )
                    ) {
                        TextField(
                            readOnly = true,
                            value = transferState,
                            onValueChange = { showConfirmTransfer = true},
                            label = { Text("Transfer Task") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedTransfer
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                            )





                        ExposedDropdownMenu(
                            expanded = expandedTransfer,
                            onDismissRequest = {
                                expandedTransfer = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                ,
                        ){
                            taskDetail.assignees.forEach { item ->
//                                if (item!=myself) TODO
                                DropdownMenuItem(
                                    text = { Text(text = item.memberName) },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        transferState = item.memberName
                                        expandedTransfer = false
                                        showConfirmTransfer = true
                                    }
                                )
                            }
                        }
                    }
                    if(showConfirmTransfer) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                           ) {


                            Text(text = "Do you want to transfer this task to ${transferState}?",
                                 fontSize = small_font_size.sp)
                            Button(
                                onClick = {
                                    showConfirmTransfer = false
                                    viewModel.updateAssignee(transferState)
                                    transferState = "Transfer Task"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.primary_blue),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Confirm")
                                }

                            }

                            Button(
                                onClick = {
                                    transferState = ""
                                    showConfirmTransfer = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.progress_red),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Cancel")
                                }

                            }
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp)
                        .border(
                            BorderStroke(
                                2.dp,
                                SolidColor(colorResource(id = R.color.progress_red))
                            ), RoundedCornerShape(4.dp)
                        )
                        .background(color = colorResource(id = R.color.progress_red))
                        .padding(2.dp, 5.dp)
                        .clickable(
                            onClick = {
//                                // delete task endpoint: TODO backend
                                viewModel.deleteTask(taskDetail.id)
                            }
                        ),
                        horizontalArrangement = Arrangement.Center

                    ){

                        Text (text = "Delete Task",
                            fontSize = small_font_size.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(nameWidth),
                            color = colorResource(id = R.color.white)


                        )
                    }


                }
            }
        }
    })
}
