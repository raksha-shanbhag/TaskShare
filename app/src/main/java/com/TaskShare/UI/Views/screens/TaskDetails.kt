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
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.TaskDetail
import com.TaskShare.ViewModels.TaskViewModel
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskDetailsScreen(onBack: () -> Unit, viewModel: TaskViewModel) {
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
                        .padding(15.dp, 10.dp),

                ) {

                    Text(
                        text = viewModel.detailTaskState.value.taskDetail.taskName,
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
                    var name_wdith = 0.35f
                    var value_width = 0.65f
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Assigned to", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium,modifier = Modifier.fillMaxWidth(name_wdith))

                        Row(modifier = Modifier
                            .fillMaxWidth(value_width)
                            ){
                            RenderPills(taskDetail.assignee, R.color.icon_blue)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Assigned by", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))

                        Row(modifier = Modifier.fillMaxWidth(value_width)){
                            RenderPills(taskDetail.assigner, R.color.icon_blue)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Group", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        Row(modifier = Modifier.fillMaxWidth(value_width)){
                            RenderPills(taskDetail.groupName, R.color.pink)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Status", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        RenderStatus(taskDetail.status)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Deadline", fontSize = small_font_size.sp,fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        RenderPills(simpleDateFormat.format(taskDetail.deadline), R.color.banner_blue)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Cycle", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        RenderPills(taskDetail.cycle, R.color.banner_blue)
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp)
                        .border(
                            BorderStroke(
                                2.dp,
                                SolidColor(colorResource(id = R.color.banner_blue))
                            ), RoundedCornerShape(4.dp)
                        )
                        .padding(2.dp, 5.dp)
                        .clickable(
                            onClick = {
//                                showTransfer = !showTransfer
                            }
                        ),
                        horizontalArrangement = Arrangement.Center

                    ){

                        Text (text = "Transfer Task",
                            fontSize = small_font_size.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(name_wdith),
                            color = colorResource(id = R.color.banner_blue)


                        )
                    }
                    ExposedDropdownMenuBox(
                        expanded = expandedTransfer,
                        onExpandedChange = {
                            expandedTransfer = !expandedTransfer
                        }
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
                        )





                        ExposedDropdownMenu(
                            expanded = expandedTransfer,
                            onDismissRequest = {
                                expandedTransfer = false
                            }
                        ){
                            taskDetail.assignees.forEach { item ->
//                                if (item!=myself) TODO
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        transferState = item
                                        expandedTransfer = false
                                        showConfirmTransfer = true
                                    }
                                )
                            }
                        }
                    }
                    if(showConfirmTransfer){
                        Text(text = "Do you want to transfer this task to ${transferState}?")
                        Button(onClick = {
                            showConfirmTransfer = false
                            viewModel.updateAssignee(transferState)
                            transferState = ""
                        },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.primary_blue),
                                contentColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
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
                                contentColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                Text("Cancel")
                            }

                        }
                    }


                }
            }
        }
    })
}
