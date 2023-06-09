package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.GroupViewState
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateGroupScreen(onBack: () -> Unit, viewModel: GroupViewModel) {
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
        mutableStateOf(mutableListOf<String>())
    }

    val state by viewModel.state

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
                .absolutePadding(0.dp, 20.dp, 0.dp, 0.dp)
                .background(Color.White)
                .padding(0.dp, 50.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column ( verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(value = groupName, onValueChange = {text -> groupName = text},  label = {Text("Group Name")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black))

                TextField(value = groupDescription, onValueChange = {text -> groupDescription = text}, label = {Text("Group Description")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black))

                TextField(value = member, onValueChange = {text -> member = text}, label = {Text("Group Member Email")}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, focusedIndicatorColor = Color.Black, cursorColor = Color.Black, focusedLabelColor = Color.Black))

                LazyColumn() {
                    itemsIndexed(groupMembers) { ind, currentGroup ->
                        Text(text = "Member " +  (ind+1) + ": $currentGroup")
                    }
                }
                Button(onClick = {
                    groupMembers = (groupMembers + member) as MutableList<String>
                    member = ""
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.banner_blue),
                        contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Add Group Member")
                    }

                }

                Button(onClick = {
                    viewModel.createGroup(groupName, groupDescription, groupMembers)
                    groupMembers = (mutableListOf<String>())
                    groupName = ""
                    groupDescription = ""
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.banner_blue),
                        contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("Save and Create Group")
                    }

                }


            }

        }
    })

}


