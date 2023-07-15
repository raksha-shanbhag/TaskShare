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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.greetingcard.R
import com.example.greetingcard.screens.ProfileImage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onEditPicture: () -> Unit,
    onSaveEdit: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Variables to hold the user's credentials
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val bio = remember { mutableStateOf("") }
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
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_left),
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(90.dp))
                        Text(
                            text = "Edit Profile",
                            color = Color.White,
                            fontSize = 26.sp
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
            Spacer(modifier = Modifier.height(20.dp))
            EditProfilePictureSection(onClick = onEditPicture)
            Spacer(modifier = Modifier.height(20.dp))

            RenderEditTextField(labelValue = "First name"){ newValue ->
                firstName.value = newValue
            }
            RenderEditTextField(labelValue = "Last name"){ newValue ->
                lastName.value = newValue
            }
            RenderEditTextField(labelValue = "Bio"){ newValue ->
                bio.value = newValue
            }
            RenderEditTextField(labelValue = "Email"){ newValue ->
                email.value = newValue
            }

            Spacer(modifier = Modifier.height(20.dp))
            RenderSaveButton("Save",
                "Save changes to profile", onSaveEdit)
        }
    }
}

@Composable
fun RenderEditTextField(modifier: Modifier = Modifier, labelValue: String,
                        onValueChange: (String) -> Unit) {
    val textValue = remember {
        mutableStateOf("")
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onValueChange(it)},
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color.Black,
            focusedIndicatorColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            disabledPlaceholderColor = Color.Black
        )
    )
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun RenderSaveButton(textValue: String, contentDesc: String,
                     onClick: () -> Unit) {
    Button(
        onClick = { onClick },
        modifier = Modifier
            .width(200.dp)
            .height(45.dp),
        contentPadding = PaddingValues(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, colorResource(id = R.color.primary_blue)),
        colors = ButtonDefaults.buttonColors(Color.White),
    ){
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
fun EditProfilePictureSection(modifier: Modifier = Modifier, onClick: () -> Unit) {

    // Profile picture placeholder
    val imagePfp = painterResource(R.drawable.ic_account_circle)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            ProfileImage(image = imagePfp,
                modifier = Modifier
                    .size(180.dp)
                    .weight(5f)     // Pfp will take up 50% of row's width
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onClick/*TODO: Select photo from camera to upload as profile picture*/ },
            modifier = Modifier
                .width(150.dp)
                .height(45.dp),
            contentPadding = PaddingValues(10.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.background_blue)),
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Edit Picture",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
@Preview
fun EditProfileScreenPreview() {
    EditProfileScreen(
        onBack = {},
        onEditPicture = {},
        onSaveEdit = {}
    )
}
