package com.example.greetingcard.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.TaskShare.ViewModels.GroupViewState
import com.example.greetingcard.R


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")

//@Composable
//fun multiselectAssignees(assignee: String, assigneesState: MutableList<Boolean>, index: Int) {
//    var isSelected by rememberSaveable { mutableStateOf(assigneesState[index]) }
//    val backgroundColor by animateColorAsState(if (isSelected) Color.Blue else Color.Transparent)
//
//    Row {
//
//        Checkbox(
//            checked = isSelected,
//            onCheckedChange = {
//                isSelected = !isSelected
//                assigneesState[index] = !isSelected
//            },
//            modifier = Modifier
//                .background(color = backgroundColor)
//        )
//        Text(
//            text = assignee,
//            modifier = Modifier
//                .padding(24.dp)
//        )
//    }
//}

@Composable
fun AddTaskScreen() {
    val viewModel = viewModel(AddTaskViewModel::class.java)
    val groupViewModel = viewModel(GroupViewModel::class.java)
    val state by viewModel.state

    val repeatList = arrayOf("No Cycle", "Daily", "Weekly", "Every 2 weeks")
    val assignees = arrayOf("Jaishree", "Lamia", "Cheng")
    // state of the menu
    var expandedGroup by remember {
        mutableStateOf(false)
    }
    var expandedCycle by remember {
        mutableStateOf(false)
    }
//    var assigneesState by remember { mutableListOf<Boolean>() }
    var groupNames by remember {
        mutableStateOf(mutableListOf<String>())
    }

    groupNames = groupViewModel.getAllGroupNames()
    Log.i("Debugging Raksha", groupNames.toString())


    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Add Task", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
        )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(0.dp, 50.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column (verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(value = state.taskName, onValueChange = {text -> viewModel.updateTaskName(text)},  label = {Text("Task Name")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black))

                ExposedDropdownMenuBox(
                    expanded = expandedGroup,
                    onExpandedChange = {
                        expandedGroup = !expandedGroup
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = state.groupName,
                        onValueChange = { },
                        label = { Text("Group Name") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedGroup
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGroup,
                        onDismissRequest = {
                            expandedGroup = false
                        }
                    ){
                        groupNames.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    viewModel.updateGroupName(item)
                                    expandedGroup = false
                                }
                            )
                        }
                    }
                }

                TextField(value = state.deadline,
                    onValueChange = {text -> viewModel.updateDeadline(text)},
                    label = {Text("Deadline")},
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
                    placeholder = { Text(text = "mm/dd/yyyy") }
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCycle,
                    onExpandedChange = {
                        expandedCycle = !expandedCycle
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = state.cycle,
                        onValueChange = { },
                        label = { Text("Cycle") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedCycle
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCycle,
                        onDismissRequest = {
                            expandedCycle = false
                        }
                    ){
                        repeatList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    viewModel.updateCycle(item)
                                    expandedCycle = false
                                }
                            )
                        }
                    }
                }

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
                        Text("Assign Another Member")
                    }
                }

                Button(onClick = {
                    viewModel.createTask()
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.banner_blue),
                        contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("Save and Create Task")
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