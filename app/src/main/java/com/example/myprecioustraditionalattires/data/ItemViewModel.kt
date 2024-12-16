package com.example.myprecioustraditionalattires.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.myprecioustraditionalattires.models.Item
import com.example.myprecioustraditionalattires.navigation.ROUTE_VIEW_ITEM
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ItemViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    fun saveItem(
        clothename: String,
        description: String,
        gender: String,
        age: String,
        navController: NavController,
        context: Context
    ) {
        val id = System.currentTimeMillis().toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("Item/$id")

        val itemData = Item("", clothename, description, gender, age, id)

        dbRef.setValue(itemData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Item added successfully", context)
                    navController.navigate(ROUTE_VIEW_ITEM)
                } else {
                    showToast("Item not added successfully", context)
                }
            }
    }

    fun viewItemById(id: String, item: MutableState<Item>, context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Item/$id")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Item::class.java)
                if (value != null) {
                    item.value = value // Set the individual item
                } else {
                    showToast("Item not found", context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch item", context)
            }
        })
    }

    fun viewItems(
        emptyItemState: MutableState<Item>,
        items: SnapshotStateList<Item>,
        context: Context
    ): SnapshotStateList<Item> {
        val ref = FirebaseDatabase.getInstance().getReference("Item")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                for (childSnapshot in snapshot.children) {
                    val value = childSnapshot.getValue(Item::class.java)
                    if (value != null) {
                        items.add(value)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch items: ${error.message}", context)
            }
        })

        return items
    }

    fun updateItem(
        context: Context,
        navController: NavController,
        clothename: String,
        description: String,
        gender: String,
        age: String,
        id: String
    ) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Item/$id")
        val updateItem = Item("", clothename, description, gender, age, id)

        databaseReference.setValue(updateItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Item Updated Successfully", context)
                    navController.navigate(ROUTE_VIEW_ITEM)
                } else {
                    showToast("Record Update failed", context)
                }
            }
    }

    fun deleteItem(context: Context, id: String, navController: NavController) {
        AlertDialog.Builder(context)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item")
            .setPositiveButton("Yes") { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("Item/$id")
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Item deleted successfully", context)
                    } else {
                        showToast("Item not deleted", context)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
