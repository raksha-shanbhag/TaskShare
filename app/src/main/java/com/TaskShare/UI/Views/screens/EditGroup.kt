package com.TaskShare.UI.Views.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.TaskShare.ViewModels.GroupViewModel
import com.TaskShare.ViewModels.GroupViewState
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditGroupScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: GroupViewModel) {
    val scrollState = rememberScrollState()
    //getting data
    val state by viewModel.state
    var group = viewModel.groupsState.value.groups.find{ temp ->  temp.id == state.id}
    if(group == null) {
        group = GroupViewState()
    }
    var friends by remember {
        mutableStateOf(viewModel.getFriendsNotInGroup(group.groupMembers))
    }
    var groupName by remember {
        mutableStateOf(group.groupName)
    }
    var groupDescription by remember {
        mutableStateOf(group.groupDescription)
    }


    var groupMembers by remember {
        mutableStateOf(mutableListOf<String>())
    }
    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Edit Group", color = Color.White, fontSize = 30.sp) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.primary_blue)),
            navigationIcon = {
                IconButton(
                    onClick = onBack ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "", tint = Color.White)
                }}
        )
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),

        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Column() {
                    Text(
                        text = "Group Name",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = Color.Black,
                        modifier = Modifier.absolutePadding(20.dp, 10.dp,10.dp,0.dp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.absolutePadding(20.dp, 0.dp,10.dp,10.dp))

                }
                TextField(value = groupName, onValueChange = {text -> groupName = text},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White, textColor = Color.Black,
                        focusedIndicatorColor = Color.Black, cursorColor = Color.Black,
                        focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black),
                    modifier = Modifier.absolutePadding(20.dp, 0.dp,0.dp,0.dp)
                )

                Column() {
                    Text(
                        text = "Group Description",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = Color.Black,
                        modifier = Modifier.absolutePadding(20.dp, 10.dp,10.dp,0.dp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.absolutePadding(20.dp, 0.dp,20.dp,10.dp))

                }

                TextField(value = groupDescription, onValueChange = {text -> groupDescription = text},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White, textColor = Color.Black,
                        focusedIndicatorColor = Color.Black, cursorColor = Color.Black,
                        focusedLabelColor = Color.Black, disabledPlaceholderColor = Color.Black),
                    modifier = Modifier.absolutePadding(20.dp, 0.dp,0.dp,0.dp)
                )

                Column() {
                    Text(
                        text = "Members",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = Color.Black,
                        modifier = Modifier.absolutePadding(20.dp, 10.dp,10.dp,0.dp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.absolutePadding(20.dp, 0.dp,10.dp,10.dp))

                }

                LazyColumn(horizontalAlignment = Alignment.Start, modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(20.dp, 10.dp, 10.dp, 0.dp)
                    .background(
                        colorResource(id = R.color.icon_blue),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)) {

//using data
                    items(group.groupMembers) {  currentMem ->
                        var email = TSUsersRepository().getUserInfo(currentMem).email
                        Text(text = " $email", fontSize = MaterialTheme.typography.subtitle1.fontSize,)
                    }

                }
                Column(modifier = Modifier
                    .fillMaxWidth()) {

                    Text(
                        text = "Choose more members to add",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = Color.Black,
                        modifier = Modifier.absolutePadding(20.dp, 10.dp,10.dp,0.dp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.absolutePadding(20.dp, 0.dp,10.dp,10.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(friends.size) { i ->
                            Row(modifier = Modifier
                                .absolutePadding(60.dp, 0.dp, 0.dp, 0.dp)
                                .fillMaxWidth()
                                .clickable { friends = friends.mapIndexed { j, item ->
                                    if(i == j) {
                                        item.copy(isSelected = !item.isSelected)
                                    } else item
                                } }) {
                                Text(text = friends[i].name)
                                if(friends[i].isSelected) {
                                    Icon(imageVector = Icons.Default.Check,
                                        contentDescription = "selected" )
                                } else {
                                    Icon(painterResource(id =  R.drawable.ic_sqaure), "square")
                                }
                            }

                        }
                    }
                    Button(
                        onClick = {
                            var selected = friends.filter{it.isSelected}
                            group.groupMembers.forEach { groupMembers = (groupMembers + TSUsersRepository().getUserInfo(it).email) as  MutableList<String>}
                            selected.forEach{
                                groupMembers = (groupMembers + it.name) as MutableList<String>
                            }
                            viewModel.updateGroup(groupName, groupDescription, groupMembers);
                            onDone()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.progress_red),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save Changes")
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.removeMember()
                            onDone()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.progress_red),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(60.dp, 0.dp, 60.dp, 0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Leave Group")
                        }
                    }
                }
            }
        }
    })


}


