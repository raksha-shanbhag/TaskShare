package com.example.greetingcard.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
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

@Composable
fun LoginScreen(
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    //onForgotClick: () -> Unit
) {

    // Variables to hold the user's email and password
    val email = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}

    val headingSize = 30.sp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.log_in),
                fontSize = headingSize,
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
            LogInButton(stringResource(R.string.log_in), onLogInClick)

            Spacer(modifier = Modifier.height(15.dp))
            ClickableForgotPasswordText()
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
    ClickableText(text = annotatedString, onClick = {offset->
        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also{ span->
                Log.d("ClickableForgotPasswordText", "{$span}")
            }

    })
}

@Composable
fun LogInButton(textValue: String, onLogInClick: () -> Unit) {
    Button(onClick = { onLogInClick() },
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
                text = textValue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    //LoginScreen()
}