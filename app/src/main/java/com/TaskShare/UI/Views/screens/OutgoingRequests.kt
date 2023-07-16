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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
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
import com.example.greetingcard.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable

fun OutgoingRequestsScreen(
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onFriends: () -> Unit,
    onIncoming: () -> Unit,
    onOutgoing: () -> Unit
) {
    val scrollState = rememberScrollState()

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
                            text = "Outgoing Requests",
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
        ) {
            RenderTopButtonBar(onFriends, onIncoming, onOutgoing)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                RenderOutgoingRequestCard("User1",
                    painterResource(R.drawable.ic_account_circle),
                    onClickCancel = onCancel)

                Spacer(modifier = Modifier.height(20.dp))
                RenderOutgoingRequestCard("User2",
                    painterResource(R.drawable.ic_account_circle),
                    onClickCancel = onCancel)

            }
        }
    }
}

@Composable
fun RenderOutgoingRequestCard(username: String, profilePic: Painter,
                              onClickCancel: () -> Unit,
                              modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .background(
                colorResource(id = R.color.background_blue),
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.ic_account_circle),
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
                text = username,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                RenderRectButton(onClick = onClickCancel, buttonLabel = "Cancel",
                    buttonColor = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_pink)))
            }
        }
    }
}

@Composable
fun RenderCancelButton(){

}

@Composable
@Preview
fun OutgoingRequestsScreenPreview() {
    OutgoingRequestsScreen(
        onOutgoing = {},
        onCancel = {},
        onIncoming = {},
        onFriends = {},
        onBack = {}
    )
}