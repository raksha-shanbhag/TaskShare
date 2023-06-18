package com.example.greetingcard.screens

import android.annotation.SuppressLint
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    showDetail: () -> Unit,
    showCreate: () -> Unit
) {
    androidx.compose.material.Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "My Groups", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue))
            )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column() {
                Text(
                    text = "HOME",
                    fontSize = MaterialTheme.typography.h3.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Button(onClick = showDetail) {
                    Text("view group")
                }
                Button(onClick = showCreate) {
                    Text("create group")
                }
            }

        }
    })

}

