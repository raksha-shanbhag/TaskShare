package com.TaskShare.UI.Views.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.FriendViewModel
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddFriendScreen(
    onBack: () -> Unit

) {
    // Getting friends data
    val viewModel = viewModel(FriendViewModel::class.java)
    val state by viewModel.state

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Variables to hold the user's name, email and password
    val email = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_left),
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(90.dp))
                        Text(
                            text = "Add Friend",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.primary_blue)
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Add your friends on SplitChore",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)
            Text(text = "You can add friends with their email",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp)
            Spacer(modifier = Modifier.height(30.dp))
            RenderTextField("ADD FRIEND","Enter an email") { newValue ->
                email.value = newValue
            }
            Spacer(modifier = Modifier.height(20.dp))
//            AddFriendButton("Send Friend Request",
//                "Send a friend request"
//            ) { viewModel.sendFriendRequest(email.value) }
            AddFriendButton("Send Friend Request",
                "Send a friend request",
                onClick = { viewModel.sendFriendRequest(email.value) }
            )
        }
    }
}

@Composable
fun RenderTextField(titleValue: String, labelValue: String, onValueChange: (String) -> Unit) {
    val textValue = remember {
        mutableStateOf("")
    }

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = titleValue,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            label = { androidx.compose.material.Text(text = labelValue) },
            singleLine = true,
            value = textValue.value,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = colorResource(id = R.color.background_blue),
                focusedBorderColor = colorResource(id = R.color.primary_blue),
                focusedLabelColor = colorResource(id = R.color.primary_blue),
                cursorColor = colorResource(id = R.color.primary_blue)
            ),
            keyboardOptions = KeyboardOptions.Default,
            onValueChange = {
                textValue.value = it
                onValueChange(it)
            }
        )
    }
}

@Composable
fun AddFriendButton(textValue: String, contentDesc: String,
                    onClick: () -> Unit) {
    Button(onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentPadding = PaddingValues(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, colorResource(id = R.color.primary_blue)),
        colors = ButtonDefaults.buttonColors(Color.White),
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = textValue,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.primary_blue)
            )
        }
    }
}

@Composable
@Preview
fun AddFriendScreenPreview() {
    AddFriendScreen(
        onBack = {}
    )
}
