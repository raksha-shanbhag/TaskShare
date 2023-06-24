package com.example.greetingcard.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R

@Composable
fun ProfileScreen(
    //userData: UserData
    onEditClick: () -> Unit,
    onAccountPrivClick: () -> Unit,
    onLogOut: () -> Unit,
) {
    val primaryBlue = colorResource(id = R.color.primary_blue)
    val backgroundBlue = colorResource(id = R.color.background_blue)
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBlue)
            ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(385.dp)
                .background(Color.White)
                //.shadow(elevation = 2.dp, clip = true)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(230.dp)
                .background(primaryBlue)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(modifier = Modifier.padding(20.dp))

            Spacer(modifier = Modifier.height(70.dp))
            ProfileSection()

            Spacer(modifier = Modifier.height(50.dp))
            SettingButton("Edit Profile",
                "Edit Profile",
                Icons.Default.Edit,
                onEditClick)

            Spacer(modifier = Modifier.height(25.dp))
            SettingButton("Account Privacy",
                "Account Privacy",
                Icons.Default.Lock,
                onAccountPrivClick)

            Spacer(modifier = Modifier.height(25.dp))
            SettingButton("Log Out",
                "Log Out",
                Icons.Default.ExitToApp,
                onLogOut)
        }
    }
}

// Creates the bar at the top of the screen to place the back button
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        /*Icon(imageVector = Icons.Default.Settings,//ArrowBack
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(24.dp),
        )*/
    }
}


// Creates the section to place the profile picture and write the user's
// name, email, and optional biography.
@Composable
fun ProfileSection(modifier: Modifier = Modifier) {

    // Profile picture placeholder
    val imagePfp = painterResource(R.drawable.ic_account_circle)

    Column(modifier = modifier
        .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ProfileImage(image = imagePfp,
            modifier = Modifier
                .size(180.dp)
                .weight(5f)     // Pfp will take up 50% of row's width
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        ProfileDescription(name = "Firstname Lastname",
            email = "example123@gmail.com",
            bio = "Short optional biography about the user.")
    }
}

@Composable
fun SettingButton(textValue: String, contentDesc: String, buttonIcon: ImageVector, onClick: () -> Unit) {
    Button(onClick = { onClick },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentPadding = PaddingValues(10.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.grey_button)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = buttonIcon,
                contentDescription = contentDesc,
                modifier = Modifier.padding(8.dp),
                tint = Color.DarkGray
            )
            Text(
                text = textValue,
                fontSize = 18.sp,
                color = Color.DarkGray
            )
        }
    }
}

// Creates a circle profile picture.
@Composable
fun ProfileImage(image: Painter, modifier: Modifier) {
    val primaryBlue = colorResource(id = R.color.primary_blue)
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 4.dp,
                color = Color.White,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)      // Makes profile picture round
            .background(primaryBlue)
    )
}

// Write the user's name, email, and biography.
@Composable
fun ProfileDescription(name: String, email: String, bio: String,
                      modifier: Modifier = Modifier) {

    val letterSpacing = 0.5.sp
    val lineHeight = 20.sp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            fontSize = 30.sp
        )
        Text(
            text = email,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = bio,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(
        onEditClick = { },
        onAccountPrivClick = { },
        onLogOut = { },
    )
}