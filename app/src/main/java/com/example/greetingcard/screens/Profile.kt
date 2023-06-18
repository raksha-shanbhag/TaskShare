package com.example.greetingcard.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.height(40.dp))
        ProfileSection()
    }
}

// Creates the bar at the top of the screen to place the back button
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.size(24.dp),
        )
    }
}


// Creates the section to place the profile picture and write the user's
// name, email, and optional biography.
@Composable
fun ProfileSection(modifier: Modifier = Modifier) {

    // Profile picture placeholder
    val image_pfp = painterResource(R.drawable.ic_account_circle)
    val color = colorResource(id = R.color.primary_blue)

    Column(modifier = modifier
        .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ProfileImage(image = image_pfp,
            modifier = Modifier
                .size(150.dp)
                .weight(5f)     // Pfp will take up 50% of row's width
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        ProfileDescription(name = "Firstname Lastname",
            email = "example123@gmail.com",
            bio = "Short optional biography about the user.")
    }
}

// Creates a circle profile picture.
@Composable
fun ProfileImage(image: Painter, modifier: Modifier) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)      // Makes profile picture round
    )
}

// Write the user's name, email, and biography.
@Composable
fun ProfileDescription(name: String, email: String, bio: String,
                      modifier: Modifier = Modifier) {

    val letterSpacing = 0.5.sp
    val lineHeight = 20.sp
    val userNameSize = 25.sp

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
            fontSize = userNameSize
        )
        Text(
            text = email,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
        Text(
            text = bio,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen()
}