package com.example.myprecioustraditionalattires.ui.theme.screens.Item

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myprecioustraditionalattires.R
import com.example.myprecioustraditionalattires.data.ItemViewModel
import com.example.myprecioustraditionalattires.models.Item
import com.example.myprecioustraditionalattires.navigation.ROUTE_UPDATE_ITEM

@Composable
fun ViewItems(
    navController: NavHostController,
    itemViewModel: ItemViewModel = ItemViewModel()
) {
    val context = LocalContext.current
    val emptyItemState = remember { mutableStateOf(Item("", "", "", "", "", "")) }
    val items = remember { mutableStateListOf<Item>() }

    itemViewModel.viewItems(emptyItemState, items, context)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Items",
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(items) { item ->
                ItemCard(
                    item = item,
                    navController = navController,
                    itemRepository = itemViewModel,
                    context = context
                )
            }
        }
    }
}


@Composable
fun ItemCard(
    item: Item,
    navController: NavHostController,
    itemRepository: ItemViewModel,
    context: Context
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .height(210.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.Gray)
        ) {
            Row {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.logo2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(200.dp)
                            .height(150.dp)
                            .padding(10.dp)
                    )
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = { itemRepository.deleteItem(context, item.id, navController) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text(
                                text = "REMOVE",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = { navController.navigate("$ROUTE_UPDATE_ITEM/${item.id}") },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text(
                                text = "UPDATE",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "CLOTHE NAME",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.clothename,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "DESCRIPTION",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.description,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "GENDER",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.gender,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AGE",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.age,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ViewItemDetail(id: String, viewModel: ItemViewModel = viewModel(), context: Context) {
    val item = remember { mutableStateOf(Item("", "", "", "", "", "")) }

    LaunchedEffect(id) {
        viewModel.viewItemById(id, item, context)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Item Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Cloth Name: ${item.value.clothename}")
        Text(text = "Description: ${item.value.description}")
        Text(text = "Gender: ${item.value.gender}")
        Text(text = "Age: ${item.value.age}")
    }
}

@Preview
@Composable
fun ViewItemsPreview() {
    ViewItems(rememberNavController())
}
