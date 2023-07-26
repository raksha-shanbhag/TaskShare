package com.example.greetingcard.screens

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun ViewGroupScreen(onBack: () -> Unit, showEdit: () -> Unit, viewModel: GroupViewModel) {
    //getting data
    val state by viewModel.state
    var group = viewModel.groupsState.value.groups.find{ temp ->  temp.id == state.id}
    if(group == null) {
        group = GroupViewState()
    }

    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "View Group", color = Color.White, fontSize = 30.sp) },
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
                .background(Color.White)

        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(20.dp, 20.dp, 20.dp, 0.dp)
                        .background(
                            colorResource(id = R.color.banner_blue),
                            RoundedCornerShape(4.dp)
                        )

                        .padding(15.dp, 10.dp)
                        .clickable(onClick = {
                            showEdit()
                        })

                ) {

                    Text(
                        text = group.groupName,
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

                Column() {
                    Text(
                        text = "Group Description",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = Color.Black,
                        modifier = Modifier.absolutePadding(20.dp, 10.dp,20.dp,0.dp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.absolutePadding(20.dp, 0.dp,20.dp,10.dp))

                }

                Text(
                    text = group.groupDescription,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(20.dp, 10.dp, 20.dp, 0.dp)
                        .background(
                            colorResource(id = R.color.icon_blue),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)
                )

                Column() {
                    Text(
                        text = "Members",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = Color.Black,
                        modifier = Modifier.absolutePadding(20.dp, 10.dp,20.dp,0.dp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.absolutePadding(20.dp, 0.dp,20.dp,10.dp))

                }

                LazyColumn(horizontalAlignment = Alignment.Start, modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(20.dp, 10.dp, 20.dp, 0.dp)
                    .background(
                        colorResource(id = R.color.icon_blue),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)) {
                        items(group.groupMembers) {  currentMem ->
                            var email = TSUsersRepository().getUserInfo(currentMem).email
                            Text(text = " $email", fontSize = MaterialTheme.typography.subtitle1.fontSize,)
                        }

                }
            }
        }
    })


}


