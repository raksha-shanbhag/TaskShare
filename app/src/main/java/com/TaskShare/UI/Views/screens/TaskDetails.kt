package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskDetailsScreen(onBack: () -> Unit) {
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
                        .background(
                            colorResource(id = R.color.banner_blue),
                            RoundedCornerShape(4.dp)
                        )
                        .fillMaxWidth()
                        .padding(15.dp, 10.dp),

                ) {
                    Text(
                        text = "Take Out Trash ",
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
                Column(modifier = Modifier.fillMaxSize()) {

                    
                }
            }
        }
    })
}
