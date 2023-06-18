package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateGroupScreen(onBack: () -> Unit) {
    var groupName by remember {
        mutableStateOf("")
    }
    var groupDescription by remember {
        mutableStateOf("")
    }
    var member by remember {
        mutableStateOf("")
    }
    var groupMembers by remember {
        mutableStateOf(listOf<String>())
    }

    Scaffold( topBar = {
        CenterAlignedTopAppBar(
        title = { Text(text = "Create Group", color = Color.White, fontSize = 30.sp) },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
            navigationIcon = {
                IconButton(
                    onClick = onBack ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "", tint = Color.White)
                }}
    )}, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column (verticalArrangement = Arrangement.spacedBy(10.dp)) {

                TextField(value = groupName, onValueChange = {text -> groupName = text},  label = {Text("Group Name")})

                TextField(value = groupDescription, onValueChange = {text -> groupDescription = text}, label = {Text("Group Description")})

                TextField(value = member, onValueChange = {text -> member = text}, label = {Text("Add Members")})

                LazyColumn() {
                    items(groupMembers) { currentGroup ->
                        Text(text = currentGroup)
                    }
                }

                Button(onClick = {
                    groupMembers = groupMembers + member
                    member = ""
                }) {
                    Text("add group member")
                }

            }

        }
    })

}

