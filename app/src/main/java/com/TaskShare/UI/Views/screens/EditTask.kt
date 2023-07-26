package com.example.greetingcard.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
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
import com.TaskShare.ViewModels.TaskViewModel
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R
import java.util.Calendar
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")


@Composable
fun EditTaskScreen(context: Context, redirectToMyTasks: ()-> Unit, viewModel: TaskViewModel) {
    val state by viewModel.state

    // integrate with backend
    val repeatList = arrayOf("No Cycle", "Daily", "Weekly", "Monthly")
    val statusList = arrayOf("In Progress", "Complete", "Declined")

    // state of the menus
    var expandedGroup by remember {
        mutableStateOf(false)
    }
    var expandedCycle by remember {
        mutableStateOf(false)
    }
    var expandedStatus by remember {
        mutableStateOf(false)
    }
    var expandedAssignTo by remember {
        mutableStateOf(false)
    }
    var taskDetail by remember {
        mutableStateOf(TaskViewState())
    }
    var groupMembers by remember {
        mutableStateOf(mutableListOf<GroupMember>())
    }

    var taskName by remember {
        mutableStateOf("")
    }
    var multipleChecked by remember { mutableStateOf(emptySet<Int>())}

    LaunchedEffect(viewModel) {
        // fetch data from viewmodel
        groupMembers = viewModel.getGroupMembers()

        taskDetail = viewModel.getDetailTaskInfo()
        taskName = taskDetail.taskName

        groupMembers.forEachIndexed() { index, member ->

            if (taskDetail.assignees.any{it.memberId == member.memberId}){

                multipleChecked += index
                groupMembers[index].selected = true

            }
        }
    }




    // date picker setup
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    calendar.time = taskDetail.deadline

    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH) + 1
    day = calendar.get(Calendar.DAY_OF_MONTH)

    val date = remember { mutableStateOf("$day/$month/$year") }
    val datePickerDialog = DatePickerDialog(
        context,

        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            var actualmonth = month + 1
            date.value = "$dayOfMonth/$actualmonth/$year"
        }, year, month, day
    )




    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Edit Task", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
            navigationIcon = {
                IconButton(
                    onClick = redirectToMyTasks ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "", tint = Color.White)
                }
            }
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

                TextField(
                    value = taskName,
                    onValueChange = {
                            text ->
                        taskName = text
                                    },
                    label = {Text("Task Name")},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White, textColor = Color.Black,
                        focusedIndicatorColor = Color.Black, cursorColor = Color.Black,
                        focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black))

                // render the right date
//                var dateDisplayed = taskDetail.deadline.day
                

                TextField(
                    readOnly = true,
                    value = date.value,
                    onValueChange = {},
                    label = { Text("End Date") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = false
                        )
                    },
                    enabled = false,
                    modifier = Modifier
                        .clickable {
                            datePickerDialog.show()
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        disabledPlaceholderColor = Color.Black,
                        disabledTextColor = Color.Black,
                        disabledLabelColor = Color.Black
                    )
                )

                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = {
                        expandedStatus = !expandedStatus
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = taskDetail.status,
                        onValueChange = { },
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedStatus
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedStatus,
                        onDismissRequest = {
                            expandedStatus = false
                        }
                    ){
                        statusList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    taskDetail.status = item
                                    expandedStatus = false
                                }
                            )
                        }
                    }
                }


                ExposedDropdownMenuBox(
                    expanded = expandedCycle,
                    onExpandedChange = {
                        expandedCycle = !expandedCycle
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = taskDetail.cycle,
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
                                    taskDetail.cycle = item
                                    expandedCycle = false
                                }
                            )
                        }
                    }
                }

                // updating assignees
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)) {
                    Text(text = "Assign To")
                    groupMembers.forEachIndexed {index, item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(

                                checked = multipleChecked.contains(index),
                                onCheckedChange = {
                                    item.toggle()
                                    multipleChecked = if(it){
                                        multipleChecked + index
                                    } else{
                                        multipleChecked - index
                                    }

                                }
                            )
                            Text(
                                text = item.memberName
                            )

                        }
                    }
                }

                Button(onClick = {
                    viewModel.updateTask(groupMembers, taskName, date.value)
                    state.taskName = ""
                    state.groupName = ""
                    state.cycle = ""
                    date.value = ""
                    state.assignees = mutableListOf()
                    groupMembers = mutableListOf()
                    redirectToMyTasks()
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.primary_blue),
                        contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("Edit Task")
                    }

                }



            }

        }
    })
}

