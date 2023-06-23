package com.example.greetingcard.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.GroupViewState
import com.TaskShare.ViewModels.TaskViewState
import com.example.greetingcard.R

var min_width_pill_g = 100
var max_width_pill_g = 400
var mid_font_size_g = 20
var small_font_size_g = 18


@Composable
fun RenderPillsG(text: String, bg_color: Int){
    var text_color = R.color.white

    if (bg_color == R.color.icon_blue || bg_color == R.color.pink){
        text_color = R.color.black
    }
    Text(
        text = text,
        color = colorResource(id = text_color),
        fontSize = MaterialTheme.typography.caption.fontSize,

        modifier = Modifier
            .widthIn(min_width_pill.dp, max_width_pill.dp)
            .padding(4.dp, 0.dp)
            .background(colorResource(id = bg_color), RoundedCornerShape(3.dp))
            .padding(10.dp, 2.dp)
    )

}
@Composable
fun RenderStatusG(status: String){
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
    RenderPillsG(text_print, bg_color)
}

@Composable
fun RenderTaskCardG(task_name: String, assigner: String, status: String, end_date: String, showDetail: () -> Unit){
//    Button(onClick = showDetail){
    Row(
        modifier = Modifier
            .background(
                colorResource(id = R.color.icon_blue),
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .clickable(
                onClick = showDetail
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

        Column(modifier = Modifier
            .padding(10.dp, 0.dp)
            .widthIn(10.dp, 140.dp))
        {
            Text(text = task_name,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                modifier = Modifier
                    .padding(2.dp, 0.dp, 0.dp, 5.dp)
            )
            Text(text = "Assigned by: $assigner",
                fontSize = MaterialTheme.typography.caption.fontSize,
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
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,

            ) {
            RenderStatusG(status)
            Spacer(modifier = Modifier.height(height = 5.dp))
            Text(text = end_date,
                fontSize = MaterialTheme.typography.caption.fontSize,
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
//    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ViewGroupTasksScreen(onBack: () -> Unit,showEdit: () -> Unit) {
    //getting data
    val viewModel = viewModel(GroupViewModel::class.java)
    val state by viewModel.state
    val groupState by viewModel.groupsState
    val scrollState = rememberScrollState()

    viewModel.addGroupAndTasks(
        GroupViewState("Roommates", "test description","Lamia", mutableListOf("Lamia", "Jaishree"), mutableListOf(
            TaskViewState("Trash", "Lamia", "Jaishree", "10/10/2023", "done")
        ), mutableListOf())
    )
    viewModel.addGroupAndTasks(
        GroupViewState("Home", "test description", "Lamia",mutableListOf("Lamia", "Jaishree", "Cheng"), mutableListOf(
            TaskViewState("Trash", "Lamia", "Jaishree", "10/10/2023", "done"),
            TaskViewState("Dishes", "Lamia", "Jaishree", "10/10/2023", "inprogress"),
            TaskViewState("Laundry", "Jaishree", "Lamia", "10/10/2023", "inprogress")
        ), mutableListOf(TaskViewState("Dishes", "Lamia", "Jaishree", "10/10/2023", "in_progress")))
    )


    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Group Details", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
            navigationIcon = {
                IconButton(
                    onClick = onBack ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "", tint = Color.White)
                }},
            actions = {
                IconButton(onClick = showEdit) {
                    Icon(painterResource(id = R.drawable.ic_settings), modifier = Modifier.size(35.dp),contentDescription = "", tint = Color.White)
                }
            }
        )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(state = scrollState),
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
                .absolutePadding(20.dp, 20.dp, 20.dp, 20.dp)) {


                Row(Modifier.padding(10.dp)){
                    Text(text = "Sort By: ",
                        fontSize = mid_font_size.sp)
                    Text(text = "Assignee",
                        fontSize = mid_font_size.sp)
                }

                //using data
                var assigneeTasks = viewModel.getAssigneeTasks( 1)
                assigneeTasks.forEach{
                    Text(text = "Assigned to ${it.key}",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold)
                    it.value.forEach{
                        RenderTaskCardG(it.taskName, it.assigner, it.status, it.dueDate, showEdit)
                        Spacer(modifier = Modifier.height(height = 10.dp))
                    }
                }



            }

        }
    })


}


