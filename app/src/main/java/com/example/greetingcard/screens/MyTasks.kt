package com.example.greetingcard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greetingcard.R

var min_width_pill = 100
var max_width_pill = 400
@Composable
fun RenderStatus(status: String){

    var text_status = ""
    var text_color = R.color.white

    if(status == "todo"){
        text_status = "To Do"
        text_color = R.color.progress_red
    }
    if(status == "inprogress"){
        text_status = "In Progress"
        text_color = R.color.progress_yellow
    }
    if(status == "done"){
        text_status = "Done"
        text_color = R.color.progress_green
    }

    Text(text = text_status,
        color = colorResource(id = R.color.white),

        modifier = Modifier
            .widthIn(min_width_pill.dp, max_width_pill.dp)
            .background(colorResource(id = text_color), RoundedCornerShape(3.dp))
            .padding(10.dp, 2.dp)
    )
}

@Composable
fun MyTasksScreen(showDetail: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Row(modifier) {
                Text(
                    text = "Tasks Remaining: "
                )
                Text(
                    text = "0"
                )
            }
            Row(){
                Text(text = "Sort By: ")
                Text(text = "Groups")
            }
            Row(
            modifier = Modifier
                .background(colorResource(id = R.color.icon_blue), RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
                ) {


                Icon(
                    painterResource(id = R.drawable.baseline_groups_24),
                    contentDescription = "Default Group Icon",
                    modifier = Modifier
                        .background(colorResource(id = R.color.white), RoundedCornerShape(10.dp))
                        .padding(14.dp)
                )

                Column(modifier = Modifier
                    .padding(10.dp, 0.dp))
                {
                    Text(text = "Clean counters",
                        modifier = Modifier
                            .padding(2.dp, 0.dp, 0.dp, 5.dp))
                    Text(text = "Roommates",
                        color = colorResource(id = R.color.banner_blue),
                        modifier = Modifier
                            .background(colorResource(id = R.color.white), RoundedCornerShape(3.dp))
                            .padding(10.dp, 2.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,

                ) {
                    RenderStatus("todo")
                    RenderStatus("done")
                    RenderStatus("inprogress")
                    Spacer(modifier = Modifier.height(height = 5.dp))
                    Text(text = "10/05/2023",
                    modifier = Modifier
                        .widthIn(min_width_pill.dp, max_width_pill.dp)
                        .background(colorResource(id = R.color.white), RoundedCornerShape(3.dp))
                        .padding(10.dp, 2.dp)
                    )
                }


//            Text(
//                text = "My Tasks",
//                fontSize = MaterialTheme.typography.h3.fontSize,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )

//            Button(onClick = showDetail) {
//                Text("my x task")
//            }

        }

    }
}}

