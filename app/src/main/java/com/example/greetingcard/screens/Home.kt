package com.example.greetingcard.screens

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    showDetail: () -> Unit,
    showCreate: () -> Unit
) {
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
}

//@Composable
//@Preview
//fun HomeScreenPreview() {
//    HomeScreen()
//}

//fun HomeScreen(
//    showDetail: () -> Unit
//) {
//    Scaffold {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Text(
//                modifier = Modifier.align(Alignment.Center),
//                text = "Home Screen", style = MaterialTheme.typography.headlineMedium
//            )
//            FilledButton(
//                modifier = Modifier
//                    .padding(top = 100.dp)
//                    .align(Alignment.Center),
//                text = "Goto home detail",
//                onClick = {
//                    showDetail()
//                }
//            )
//        }
//    }
//}