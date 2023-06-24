package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.TaskShare.ViewModels.TaskViewModel
import com.example.greetingcard.R



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskDetailsScreen(onBack: () -> Unit) {
    val viewModel = viewModel(TaskViewModel::class.java)

    // REMOVE line below when integration with backend done, this is used to generate dummy data
    viewModel.initTask("Cry Myself to Sleep", "Waterloo", "inprogress", "Jaishree", "ECE", "01/04/2024", "Daily")

    // update getTaskByID to take in id so this page can be dynamically rendered
    var task = viewModel.getTaskByID()
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
                        text = task.taskName,
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
                            .wrapContentWidth()){
                            RenderPills(task.assignee, R.color.icon_blue)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Assigned by", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        Row(modifier = Modifier.fillMaxWidth(value_width)){
                            RenderPills(task.assigner, R.color.icon_blue)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Group", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        Row(modifier = Modifier.fillMaxWidth(value_width)){
                            RenderPills(task.groupName, R.color.pink)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Status", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        RenderStatus(task.status)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Deadline", fontSize = small_font_size.sp,fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        RenderPills(task.deadline, R.color.banner_blue)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 3.dp)) {
                        Text(text = "Cycle", fontSize = small_font_size.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth(name_wdith))
                        RenderPills(task.cycle, R.color.banner_blue)
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
                        .padding(2.dp, 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ){

                        Text (text = "Transfer Task",
                            fontSize = small_font_size.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(name_wdith),
                            color = colorResource(id = R.color.banner_blue)
                        )
                    }
                }
            }
        }
    })
}
