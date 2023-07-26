package com.example.greetingcard.screens

import android.annotation.SuppressLint
import android.util.Log
import com.TaskShare.UI.Views.screens.LoginBackend
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    //state: SignInState,
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    //onForgotClick: () -> Unit
) {

    // Variables to hold the user's email and password
    val email = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.log_in),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))
            RenderTextFields(stringResource(R.string.email), Icons.Default.Email) { newValue ->
                email.value = newValue
            }
            Spacer(modifier = Modifier.height(5.dp))
            RenderPasswordTextField(stringResource(R.string.password), Icons.Default.Lock) { newValue ->
                password.value = newValue
            }

            Spacer(modifier = Modifier.height(50.dp))

            val loginBackend = LoginBackend()
            Button(onClick = {
                val emailValue = email.value
                val passwordValue = password.value
                if (loginBackend.validateCredentials(emailValue, passwordValue)) {
                    val loginTask = loginBackend.performLogin(emailValue, passwordValue)
                    loginTask.addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result == true) {
                            onLogInClick()
                        } else {
                            // Display "Login failed. Please try signing up.
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Login failed. Please sign up.")
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
                        text = "Log In",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            //ClickableForgotPasswordText()

            Spacer(modifier = Modifier.height(5.dp))
            ClickableSignUpText(onSignUpClick)

        }

    }
}

@Composable
fun ClickableSignUpText(onSignUpClick: () -> Unit) {
    val firstSentenceText = "Don't have an account? "
    val signUpText = "Sign up!"

    val annotatedString = buildAnnotatedString {
        append(firstSentenceText)
        withStyle(SpanStyle(color = colorResource(id = R.color.primary_blue))){
            pushStringAnnotation(
                tag = signUpText,
                annotation = signUpText
            )
            append(signUpText)
        }
    }

    // Add string into clickable text component
    // Find out which part of the text the user has clicked.
    ClickableText(
        text = annotatedString,
        onClick = {onSignUpClick()})
}

@Composable
fun ClickableForgotPasswordText() {
    val forgotPassText = "Forgot your password? "

    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = colorResource(id = R.color.primary_blue))){
            pushStringAnnotation(
                tag = forgotPassText,
                annotation = forgotPassText
            )
            append(forgotPassText)
        }
    }

    // Add string into clickable text component
    // Find out which part of the text the user has clicked.
    ClickableText(
        text = annotatedString,
        onClick = {/*onForgotClick()*/}
    )
}

@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen(
        onLogInClick = { },
        onSignUpClick = { },
    )
}