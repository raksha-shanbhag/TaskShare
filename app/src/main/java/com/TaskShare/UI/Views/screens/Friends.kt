package com.TaskShare.UI.Views.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
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
fun FriendScreen(
    onBack: () -> Unit,
    onAddFriends: () -> Unit,
    onRemoveFriend: () -> Unit,
    onFriends: () -> Unit,
    onIncoming: () -> Unit,
    onOutgoing: () -> Unit,
    onBlock: () -> Unit
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
                        horizontalArrangement = Arrangement.SpaceBetween,
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
                        Text(
                            text = "Friends",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(
                            onClick = onAddFriends,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Add a friend",
                                tint = Color.White
                            )
                        }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                RenderFriendCard(
                    "User1",
                    painterResource(R.drawable.ic_account_circle),
                    onClickRemove = onRemoveFriend,
                    onClickBlock = onBlock
                )

                Spacer(modifier = Modifier.height(20.dp))
                RenderFriendCard(
                    "User2",
                    painterResource(R.drawable.ic_account_circle),
                    onClickRemove = onRemoveFriend,
                    onClickBlock = onBlock
                )

            }
        }
    }
}

@Composable
fun RenderTopButtonBar(onClickFriends: () -> Unit, onClickIncoming: () -> Unit,
                       onClickOutgoing: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.background_blue)),
    ) {
        RenderTopButton(label = "Friends", onClick = onClickFriends)
        RenderTopButton(label = "Incoming", onClick = onClickIncoming)
        RenderTopButton(label = "Outgoing", onClick = onClickOutgoing)
    }
}

@Composable
fun RenderTopButton(modifier: Modifier = Modifier, label: String,
                    onClick: () -> Unit) {
    TextButton(onClick = { onClick() },
        modifier = Modifier
            .width(130.dp)
            .height(45.dp),
        contentPadding = PaddingValues(10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun RenderFriendCard(username: String, profilePic: Painter,
                     modifier: Modifier = Modifier, onClickRemove: () -> Unit,
                     onClickBlock: () -> Unit) {
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
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = username,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Column (modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ){

                // Remove button
                RenderRectButton(onClick = onClickRemove, buttonLabel = "Remove",
                    buttonColor = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_blue)))

                Spacer(modifier = Modifier.height(10.dp))

                // Block button
                RenderRectButton(onClick = onClickBlock, buttonLabel = "Block",
                    buttonColor = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_pink)))
            }
        }
    }
}

@Composable
fun RenderRectButton(modifier: Modifier = Modifier, buttonColor: ButtonColors,
                     buttonLabel: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(110.dp)
            .height(43.dp),
        contentPadding = PaddingValues(10.dp),
        colors = buttonColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = buttonLabel,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}


@Composable
@Preview
fun FriendScreenPreview() {
    FriendScreen(
        onBack = {},
        onAddFriends = {},
        onRemoveFriend = {},
        onFriends = {},
        onIncoming = {},
        onOutgoing = {},
        onBlock = {}
    )
}