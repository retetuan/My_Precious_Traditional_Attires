package com.example.myprecioustraditionalattires.ui.theme.screens.Client

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.myprecioustraditionalattires.R
import com.example.myprecioustraditionalattires.data.ClientViewModel

@Composable
fun AddClientScreen(navController: NavController) {
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val painter = rememberImagePainter(
        data = imageUri.value ?: R.drawable.logo2,
        builder = { crossfade(true) }
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }
    val context = LocalContext.current

    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    // List to store added clients
    val clients = remember { mutableStateListOf<Client>() }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Email, contentDescription = "Email")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "ENTER NEW CLIENT",
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "BACK")
                }
                Button(onClick = {
                    val clientRepository = ClientViewModel()
                    clientRepository.saveClient(
                        firstname = firstname,
                        lastname = lastname,
                        gender = gender,
                        age = age,
                        navController=navController,
                        context=context,
                    )
//                    clients.add(newClient)
//                    firstname = ""
//                    lastname = ""
//                    gender = ""
//                    age = ""

                }) {
                    Text(text = "SAVE")
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(180.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(180.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }
                Text(text = "Attach a picture ")

                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    placeholder = { Text(text = "Enter First Name") },
                    label = { Text(text = "First Name") },
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    placeholder = { Text(text = "Enter Last Name") },
                    label = { Text(text = "Last Name") },
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    placeholder = { Text(text = "Enter Gender") },
                    label = { Text(text = "Gender") },
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    placeholder = { Text(text = "Enter Age") },
                    label = { Text(text = "Age") },
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // LazyColumn to display clients
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(clients) { client ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "First Name: ${client.firstname}")
                                Text(text = "Last Name: ${client.lastname}")
                                Text(text = "Gender: ${client.gender}")
                                Text(text = "Age: ${client.age}")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Client(
    val firstname: String,
    val lastname: String,
    val gender: String,
    val age: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddClientScreenPreview() {
    AddClientScreen(rememberNavController())
}
