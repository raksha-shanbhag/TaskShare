package com.TaskShare.UI.Views.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.TaskShare.ViewModels.FriendViewModel
import com.TaskShare.ViewModels.UserViewModel
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun IncomingRequestsScreen(
    onBack: () -> Unit,
    onAcceptRequest: () -> Unit,
    onDenyRequest: () -> Unit,
    onBlockUser: () -> Unit,
    onFriends: () -> Unit,
    onIncoming: () -> Unit,
    onOutgoing: () -> Unit
) {
    // Get info for incoming requests
    val viewModel = viewModel(FriendViewModel::class.java)
    val incomingRequests = viewModel.getIncomingRequests()

    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .scrollable(state = scrollState, orientation = Orientation.Vertical),
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
                        Spacer(modifier = Modifier.width(60.dp))
                        Text(
                            text = "Incoming Requests",
                            color = Color.White,
                            fontSize = 24.sp,
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
        Column (modifier = Modifier.fillMaxWidth()
        ){
            RenderTopButtonBar(onFriends, onIncoming, onOutgoing)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                state = scrollState
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items (incomingRequests.count()) { index ->
                    RenderIncomingRequestCard(name = incomingRequests[index].friendName,
                        painterResource(R.drawable.ic_account_circle),
                        onClickAccept = onAcceptRequest,
                        onClickDeny = onDenyRequest,
                        onClickBlock = onBlockUser
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item{
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun RenderIncomingRequestCard(name: String,
                              profilePic: Painter = painterResource(id = R.drawable.ic_account_circle),
                              onClickAccept: () -> Unit, onClickDeny: () -> Unit,
                              onClickBlock: () -> Unit,
                              modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .background(
                colorResource(id = R.color.icon_blue),
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            profilePic,
            contentDescription = "Default User Icon",
            modifier = Modifier
                .size(60.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = name,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                // Accept button
                RenderCircleButton(
                    onClick = onClickAccept,
                    buttonColor = colorResource(id = R.color.progress_green),
                    buttonIcon = painterResource(id = R.drawable.ic_check),
                    contentDesc = "Accept friend request"
                )
                Spacer(modifier = Modifier.width(10.dp))

                // Reject button
                RenderCircleButton(
                    onClick = onClickDeny,
                    buttonColor = colorResource(id = R.color.primary_blue),
                    buttonIcon = painterResource(id = R.drawable.ic_close),
                    contentDesc = "Reject friend request"
                )
                Spacer(modifier = Modifier.width(10.dp))

                // Block button
                RenderCircleButton(
                    onClick = onClickBlock,
                    buttonColor = colorResource(id = R.color.progress_red),
                    buttonIcon = painterResource(id = R.drawable.ic_block),
                    contentDesc = "Block the account"
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun RenderCircleButton(modifier: Modifier = Modifier,
                       onClick: () -> Unit, buttonColor: Color,
                       buttonIcon: Painter, contentDesc: String) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            painter = buttonIcon,
            contentDescription = contentDesc,
            tint = buttonColor
        )
    }
}


@Composable
@Preview
fun IncomingRequestsScreenPreview() {
    IncomingRequestsScreen(
        onBack = {},
        onIncoming = {},
        onOutgoing = {},
        onFriends = {},
        onAcceptRequest = {},
        onDenyRequest = {},
        onBlockUser = {}
    )
}