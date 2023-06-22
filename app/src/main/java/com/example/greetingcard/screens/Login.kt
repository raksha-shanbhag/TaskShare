package com.example.greetingcard.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greetingcard.R

@Composable
fun LoginScreen() {
    val primaryBlue = colorResource(id = R.color.primary_blue)
    val backgroundBlue = colorResource(id = R.color.background_blue)
    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}


@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen()
}