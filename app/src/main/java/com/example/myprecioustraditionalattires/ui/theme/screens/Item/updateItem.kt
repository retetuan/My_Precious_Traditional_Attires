package com.example.myprecioustraditionalattires.ui.theme.screens.Item

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myprecioustraditionalattires.R
import com.example.myprecioustraditionalattires.data.ClientViewModel
import com.example.myprecioustraditionalattires.data.ItemViewModel
import com.example.myprecioustraditionalattires.models.Client
import com.example.myprecioustraditionalattires.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun updateItemScreen(navController: NavController, id:String){
    val imageUri = rememberSaveable() {
        mutableStateOf<Uri?>(null)
    }
    val painter = rememberImagePainter(
        data =imageUri.value ?: R.drawable.logo2,
        builder = {crossfade(true)})
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        uri?.let {imageUri.value = it}

    }
    val context = LocalContext.current
    var clothename by remember {
        mutableStateOf(value = "")
    }
    var description by remember {
        mutableStateOf(value = "")
    }
    var gender by remember {
        mutableStateOf(value = "")
    }
    var age by remember {
        mutableStateOf(value = "")
    }
    val currentDataRef = FirebaseDatabase.getInstance()
        .getReference()
        .child("Item/$id")
    DisposableEffect(Unit){
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                val item = snapshot.getValue(Item::class.java)
                item?.let {
                    clothename = it.clothename
                    description = it.description
                    gender = it.gender
                    age = it.age
                }
            }
            override fun onCancelled(error: DatabaseError){
                Toast.makeText(context,error.message, Toast.LENGTH_LONG).show()
            }
        }
        currentDataRef.addValueEventListener(listener)
        onDispose { currentDataRef.removeEventListener(listener) }
    }
    Scaffold (
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
                    FloatingActionButton(onClick = { /*TODO*/ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ){
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")

                    }



                }
            )


        }
    ){innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)){
            Text(text = "UPDATE ITEM",
                fontSize = 25.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.Green)
            )
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "BACK")

                }
                Button(onClick = {
                    val itemRepository = ItemViewModel()
                    itemRepository.updateItem(
                        context = context,
                        navController = navController,
                        clothename = clothename,
                        description = description,
                        gender = gender,
                        age = age,
                        id = id
                    )

                }) {
                    Text(text = "UPDATE")

                }

            }
            Column (modifier =
            Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
                Card (
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(180.dp)
                ){
                    Image(painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(180.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop)

                }
                Text(text = "Update picture")

            }
            OutlinedTextField(value = clothename,
                onValueChange = {newClothename -> clothename = newClothename},
                placeholder = { Text(text = "Enter Clothes Name") },
                label = { Text(text = "Enter Clothes Name") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = description,
                onValueChange = {newDescription -> description = newDescription},
                placeholder = { Text(text = "Enter Description") },
                label = { Text(text = "Enter Description") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = gender,
                onValueChange = {newGender -> gender = newGender},
                placeholder = { Text(text = "Enter your Gender") },
                label = { Text(text = "Enter your Gender") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = age,
                onValueChange = {newAge -> age = newAge},
                placeholder = { Text(text = "Enter your Age") },
                label = { Text(text = "Enter your Age") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally))




        }

    }

}
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun updateItemScreenPreview(){
//    updateItemScreen(rememberNavController(),)
//}