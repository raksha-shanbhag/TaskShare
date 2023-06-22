package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.AddTaskViewModel
import com.TaskShare.ViewModels.GroupViewModel
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddTaskScreen() {
    val viewModel = viewModel(AddTaskViewModel::class.java)
    val state by viewModel.state

    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Add Task", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
        )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White).padding(0.dp, 50.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column (verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(value = state.taskName, onValueChange = {text -> viewModel.updateTaskName(text)},  label = {Text("Task Name")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black))

                TextField(value = state.groupName, onValueChange = {text -> viewModel.updateGroupName(text)}, label = {Text("Group Name")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black))

                TextField(value = state.deadline, onValueChange = {text -> viewModel.updateDeadline(text)}, label = {Text("Deadline")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black))

                TextField(value = state.cycle, onValueChange = {text -> viewModel.updateCycle(text)}, label = {Text("Repeat")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black))

                TextField(value = state.assignTo, onValueChange = {text -> viewModel.updateAssignTo(text)}, label = {Text("Assign To")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black))

                LazyColumn() {
                    itemsIndexed(state.assignees) { ind, currentGroup ->
                        Text(text = "Assignee " +  (ind+1) + ": $currentGroup")
                    }
                }
                Button(onClick = {
                    viewModel.updateAssignees(state.assignTo)
                    viewModel.updateAssignTo("")
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.banner_blue),
                        contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Add Group Member")
                    }

                }


            }

        }
    })
}

@Composable
@Preview
fun AddTaskScreenPreview() {
    AddTaskScreen()
}