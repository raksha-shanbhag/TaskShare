package com.example.greetingcard.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import com.TaskShare.UI.Views.screens.SignUpBackend
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R
import kotlinx.coroutines.launch

/*
    Followed this tutorial to set up the password text field:
    https://www.youtube.com/watch?v=PeUERQJnHdI
 */

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onLogInClick: () -> Unit,
) {
    // Variables to hold the user's name, email and password
    val firstName = remember { mutableStateOf("")}
    val lastName = remember { mutableStateOf("")}
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("")}

    val headingSize = 30.sp

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.sign_up),
                fontSize = headingSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Join a group and start organizing your tasks!",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))
            RenderTextFields(stringResource(R.string.first_name), Icons.Default.Person) { newValue ->
                firstName.value = newValue
            }
            Spacer(modifier = Modifier.height(5.dp))
            RenderTextFields(stringResource(R.string.last_name), Icons.Default.Person) { newValue ->
                lastName.value = newValue
            }
            Spacer(modifier = Modifier.height(5.dp))
            RenderTextFields(stringResource(R.string.email), Icons.Default.Email) { newValue ->
                email.value = newValue
            }
            Spacer(modifier = Modifier.height(5.dp))
            RenderPasswordTextField(stringResource(R.string.password), Icons.Default.Lock) { newValue ->
                password.value = newValue
            }

            Spacer(modifier = Modifier.height(50.dp))
            val signUpBackend = SignUpBackend()
            Button(onClick = {
                val emailValue = email.value
                val passwordValue = password.value
                val firstNameValue = firstName.value
                val lastNameValue = lastName.value
                if (signUpBackend.validateCredentials(emailValue, passwordValue, firstNameValue,lastNameValue)) {
                    val loginTask = signUpBackend.performSignUp(emailValue, passwordValue, firstNameValue,lastNameValue)
                    loginTask.addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result == true) {
                            // Successful login
                            onSignUpClick()
                        } else {
                            // Unsuccessful login
                            // Message would be field empty or password too short
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Empty field or password is too short.")
                            }
                        }
                    }
                } else {
                    //display "invalid credentials try again"
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Invalid credentials. Please try again.")
                    }
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentPadding = PaddingValues(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_blue)),
                shape = RoundedCornerShape(50.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Sign Up",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            ClickableLoginText(onLogInClick)
        }

    }
}

@Composable
fun ClickableLoginText(onLogInClick: () -> Unit) {
    val firstSentenceText = "Already have an account? "
    val loginText = "Log in."

    val annotatedString = buildAnnotatedString {
        append(firstSentenceText)
        withStyle(SpanStyle(color = colorResource(id = R.color.primary_blue))){
            pushStringAnnotation(
                tag = loginText,
                annotation = loginText
            )
            append(loginText)
        }
    }

    // Add string into clickable text component
    // Find out which part of the text the user has clicked.
    ClickableText(
        text = annotatedString,
        onClick = {onLogInClick() })
}
@Composable
fun RenderPasswordTextField(labelValue: String, icon: ImageVector, onValueChange: (String) -> Unit) {
    val password = remember {
        mutableStateOf("")
    }

    // Hide the password as the default visibility
    val passwordVisibility = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = labelValue) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },

        trailingIcon = {
            val trailingIcon = if(passwordVisibility.value) {
                painterResource(id = R.drawable.ic_visibility)
            } else {
                painterResource(id = R.drawable.ic_visibility_off)
            }
            var description = if(passwordVisibility.value) {
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            }

            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(
                    painter = trailingIcon,
                    contentDescription = description,
                    modifier = Modifier.size(25.dp)
                )
            }
        },
        visualTransformation = if(passwordVisibility.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        value = password.value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = colorResource(id = R.color.background_blue),
            focusedBorderColor = colorResource(id = R.color.primary_blue),
            focusedLabelColor = colorResource(id = R.color.primary_blue),
            cursorColor = colorResource(id = R.color.primary_blue)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = {
            password.value = it
            onValueChange(it)       // Update with new value
        }
    )
}

@Composable
fun RenderTextFields(labelValue: String, icon: ImageVector, onValueChange: (String) -> Unit) {
    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = labelValue) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
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
            onValueChange(it) // Updated with new value
        }
    )
}
@Composable
@Preview
fun SignUpScreenPreview() {
    SignUpScreen(
        onSignUpClick = { },
        onLogInClick = { },
    )
}