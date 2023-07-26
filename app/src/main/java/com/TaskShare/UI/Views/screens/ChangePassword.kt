package com.TaskShare.UI.Views.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R
import com.example.greetingcard.screens.ClickableForgotPasswordText

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Variables to hold the user's password
    val oldpassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmedPassword = remember { mutableStateOf("") }

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
                        Spacer(modifier = Modifier.width(45.dp))
                        Text(
                            text = "Change Password",
                            color = Color.White,
                            fontSize = 26.sp,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Change your password at any time",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)
            Text(text = "Password must be at least 6 characters long",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp)
            Spacer(modifier = Modifier.height(30.dp))

            RenderTextFields("Current Password") { newValue ->
                oldpassword.value = newValue
            }
            Spacer(modifier = Modifier.height(15.dp))
            RenderTextFields("New Password") { newValue ->
                newPassword.value = newValue
            }
            Spacer(modifier = Modifier.height(15.dp))
            RenderTextFields("Confirm Password") { newValue ->
                confirmedPassword.value = newValue
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row (modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                //ClickableForgotPasswordText()
            }

            Spacer(modifier = Modifier.height(50.dp))
//            RenderSaveButton("Save",
//                "Save changes to profile",
//                onSavePassword(oldpassword, newPassword, confirmedPassword)
//            )
        }

    }
}

@Composable
fun RenderTextFields(labelValue: String, onValueChange: (String) -> Unit) {
    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        label = { androidx.compose.material.Text(text = labelValue) },
        singleLine = true,
        value = textValue.value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = colorResource(id = R.color.background_blue),
            focusedBorderColor = colorResource(id = R.color.primary_blue),
            focusedLabelColor = colorResource(id = R.color.primary_blue),
            cursorColor = colorResource(id = R.color.primary_blue)
        ),
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = {
            textValue.value = it
            onValueChange(it)
        }
    )
}

@Composable
@Preview
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(
        onBack = { /*TODO*/ },
    )
}
