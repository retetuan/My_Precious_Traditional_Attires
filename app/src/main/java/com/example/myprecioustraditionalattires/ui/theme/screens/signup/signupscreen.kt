package com.example.myprecioustraditionalattires.ui.theme.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myprecioustraditionalattires.R
import com.example.myprecioustraditionalattires.data.AuthViewModel
import com.example.myprecioustraditionalattires.navigation.ROUTE_LOGIN

@Composable
fun signupScreen(navController: NavController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    val isLoading by authViewModel.isLoading.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    var usertype by remember { mutableStateOf("User") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register Here",
            fontSize = 20.sp,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.Black)
                .padding(20.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Precious Tradition Attire logo"
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { newUsername -> username = newUsername },
            label = { Text(text = "Enter your username") },
            placeholder = { Text(text = "Please enter your username") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { newEmail -> email = newEmail },
            label = { Text(text = "Enter Email") },
            placeholder = { Text(text = "Please enter Email") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { newPassword -> password = newPassword },
            label = { Text(text = "Enter Password") },
            placeholder = { Text(text = "Please enter Password") },
            modifier = Modifier.fillMaxWidth(0.8f),
                    visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmpassword,
            onValueChange = { newConfirmpassword -> confirmpassword = newConfirmpassword },
            label = { Text(text = "Confirm Password") },
            placeholder = { Text(text = "Please enter confirm Password") },
            modifier = Modifier.fillMaxWidth(0.8f),
                    visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // User Type Dropdown
        Box(modifier = Modifier.fillMaxWidth(0.8f)) {
            OutlinedTextField(
                value = usertype,
                onValueChange = {},
                label = { Text(text = "User Type") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown arrow",
                        modifier = Modifier.clickable { expanded = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(
                    onClick = {
                        usertype = "User"
                        expanded = false
                    },
                    text = { Text("User") }
                )
                DropdownMenuItem(
                    onClick = {
                        usertype = "Administrator"
                        expanded = false
                    },
                    text = { Text("Administrator") }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                authViewModel.signup(username, email, password, confirmpassword, usertype, navController, context)
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.Black, strokeWidth = 4.dp)
            } else {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "REGISTER HERE"
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Clickable Text - Already have an account? Log In Here
        ClickableText(
            text = AnnotatedString("Already have an account? Log In Here"),
            onClick = { navController.navigate(ROUTE_LOGIN) }, // Adjust to your login route
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun signupScreenPreview() {
    signupScreen(rememberNavController())
}
