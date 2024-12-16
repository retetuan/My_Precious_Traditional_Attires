package com.example.myprecioustraditionalattires.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.myprecioustraditionalattires.R
import com.example.myprecioustraditionalattires.data.AuthViewModel
import com.example.myprecioustraditionalattires.data.ItemViewModel
import com.example.myprecioustraditionalattires.models.Item
import com.example.myprecioustraditionalattires.navigation.ROUTE_HOME_TWO
import com.example.myprecioustraditionalattires.navigation.ROUTE_PAY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDashboard(
    context: Context,
    navController: NavHostController,
    itemViewModel: ItemViewModel = viewModel()
) {
    val items = remember { mutableStateListOf<Item>() }
    val emptyItemState = remember { mutableStateOf(Item("", "", "", "", "", "")) }

    // Fetch items from Firebase
    LaunchedEffect(Unit) {
        itemViewModel.viewItems(emptyItemState, items, context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "LIST OF ITEMS",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu */ }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = {
                        val authRepository = AuthViewModel()
                        authRepository.logout(navController, context)
                    }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "LogOut")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.Blue) {
                IconButton(onClick = { navController.navigate(ROUTE_HOME_TWO) }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Handle Twitter */ }) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Twitter", tint = Color.White)
                }
                IconButton(onClick = { /* Handle LinkedIn */ }) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "LinkedIn", tint = Color.White)
                }
            }
        },
        content = { padding ->
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items.chunked(2)) { rowItems ->
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(rowItems) { item ->
                                ItemCard(
                                    item = item,
                                    navController = navController,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ItemCard(
    item: Item,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imagePainter = if (item.imageUrl.isEmpty()) {
                painterResource(R.drawable.masaiwear)
            } else {
                rememberAsyncImagePainter(item.imageUrl)
            }

            Image(
                painter = imagePainter,
                contentDescription = "Item Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.clothename, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Gender: ${item.gender}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Age: ${item.age}", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(ROUTE_PAY) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Pay")
            }
        }
    }
}
