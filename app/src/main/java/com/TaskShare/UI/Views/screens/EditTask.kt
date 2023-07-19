package com.example.greetingcard.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import com.TaskShare.Models.DataObjects.Group
import com.TaskShare.ViewModels.AddTaskViewModel
import com.TaskShare.ViewModels.GroupData
import com.TaskShare.ViewModels.GroupMember
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.GroupViewState
import com.example.greetingcard.R
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")


@Composable
fun EditTaskScreen(context: Context, redirectToMyTasks: ()-> Unit) {
    val viewModel = viewModel(AddTaskViewModel::class.java)
    val state by viewModel.state

    // integrate with backend
    val repeatList = arrayOf("No Cycle", "Daily", "Weekly", "Every 2 weeks")
    // state of the menus
    var expandedGroup by remember {
        mutableStateOf(false)
    }
    var expandedCycle by remember {
        mutableStateOf(false)
    }
    var expandedAssignTo by remember {
        mutableStateOf(false)
    }
    // date picker setup
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,

//        R.style.ThemeOverlay_MyApp_Dialog,
        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/$month/$year"
        }, year, month, day
    )

    // variables
    var groups by remember {
        mutableStateOf(mutableListOf<GroupData>())
    }
    var groupMembers by remember {
        mutableStateOf(mutableListOf<GroupMember>())
    }

    // fetch data from viewmodel
    groups = viewModel.getAllGroupsForUser()
    groupMembers = viewModel.getGroupMembers()


    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Edit Task", color = Color.White, fontSize = 30.sp) },
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
            Text(text = "Edit Task")
            
//            Column (verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//
//                TextField(value = state.taskName, onValueChange = {text -> viewModel.updateTaskName(text)},  label = {Text("Task Name")},
//                    colors = TextFieldDefaults.textFieldColors(
//                        backgroundColor = Color.White, textColor = Color.Black,
//                        focusedIndicatorColor = Color.Black, cursorColor = Color.Black,
//                        focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black))
//
//                ExposedDropdownMenuBox(
//                    expanded = expandedGroup,
//                    onExpandedChange = {
//                        expandedGroup = !expandedGroup
//                    }
//                ) {
//                    TextField(
//                        readOnly = true,
//                        value = state.groupName,
//                        onValueChange = { },
//                        label = { Text("Group Name") },
//                        trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(
//                                expanded = expandedGroup
//                            )
//                        },
//                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
//                    )
//                    ExposedDropdownMenu(
//                        expanded = expandedGroup,
//                        onDismissRequest = {
//                            groupMembers = viewModel.getGroupMembers()
//                            expandedGroup = false
//                        }
//                    ){
//                        groups.forEach { item ->
//                            DropdownMenuItem(
//                                text = { Text(text = item.groupName) },
//                                onClick = {
//                                    viewModel.updateTaskGroup(item)
//                                    expandedGroup = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//
//                TextField(
//                    readOnly = true,
//                    value = date.value,
//                    onValueChange = { },
//                    label = { Text("End Date") },
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(
//                            expanded = false
//                        )
//                    },
//                    enabled = false,
//                    modifier = Modifier
//                        .clickable {
//                            datePickerDialog.show()
//                        },
//                    colors = TextFieldDefaults.textFieldColors(
//                        backgroundColor = Color.White,
//                        textColor = Color.Black,
//                        focusedIndicatorColor = Color.Black,
//                        cursorColor = Color.Black,
//                        focusedLabelColor = Color.Black,
//                        disabledPlaceholderColor = Color.Black,
//                        disabledTextColor = Color.Black,
//                        disabledLabelColor = Color.Black
//                    )
//                )
//
//
//                ExposedDropdownMenuBox(
//                    expanded = expandedCycle,
//                    onExpandedChange = {
//                        expandedCycle = !expandedCycle
//                    }
//                ) {
//                    TextField(
//                        readOnly = true,
//                        value = state.cycle,
//                        onValueChange = { },
//                        label = { Text("Cycle") },
//                        trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(
//                                expanded = expandedCycle
//                            )
//                        },
//                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
//                    )
//                    ExposedDropdownMenu(
//                        expanded = expandedCycle,
//                        onDismissRequest = {
//                            expandedCycle = false
//                        }
//                    ){
//                        repeatList.forEach { item ->
//                            DropdownMenuItem(
//                                text = { Text(text = item) },
//                                onClick = {
//                                    viewModel.updateCycle(item)
//                                    expandedCycle = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//
//                ExposedDropdownMenuBox(
//                    expanded = expandedAssignTo,
//                    onExpandedChange = {
//                        expandedAssignTo = !expandedAssignTo
//                    }
//                ) {
//                    TextField(
//                        readOnly = true,
//                        value = "Select Assignees",
//                        onValueChange = { },
//                        label = { Text("Assignees") },
//                        trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(
//                                expanded = expandedAssignTo
//                            )
//                        },
//                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
//                    )
//                    ExposedDropdownMenu(
//                        expanded = expandedAssignTo,
//                        onDismissRequest = {
//                            expandedAssignTo = false
//                        }
//                    ){
//                        groupMembers.forEach { item ->
//                            DropdownMenuItem(
//                                text = {
//                                    Row() {
//                                        Checkbox(
//                                            checked = item.selected,
//                                            onCheckedChange = {
//                                                item.toggle()
//                                            }
//                                        )
//
//                                        Text(text = item.memberName)
//                                    }
//                                },
//                                onClick = {
////                                    viewModel.updateAssignee(groupMembers.indexOf(item))
////                                    updateSelectedGroupMember()
////                                    item.selected = !item.selected
//                                    expandedAssignTo = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//
//                FlowRow(modifier = Modifier
//                    .fillMaxWidth()
//                    .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp),
//                    horizontalArrangement = Arrangement.Center
//
//                )
//                {
//                    groupMembers.forEach { item ->
//                        if (item.selected) {
//                            RenderPills(item.memberName, R.color.icon_blue)
//                            Spacer(modifier = Modifier.padding(5.dp, 20.dp))
//                        }
//                    }
//                }
//
//
//                Button(onClick = {
//                    viewModel.createTask()
//                    state.taskName = ""
//                    state.groupName = ""
//                    state.cycle = ""
//                    date.value = ""
//                    state.assignees = mutableListOf()
//                    groupMembers = mutableListOf()
//                    redirectToMyTasks()
//                },
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = colorResource(id = R.color.primary_blue),
//                        contentColor = Color.White),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
//                ) {
//                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
//                        Text("Edit Task")
//                    }
//
//                }
//
//
//
//            }

        }
    })
}

