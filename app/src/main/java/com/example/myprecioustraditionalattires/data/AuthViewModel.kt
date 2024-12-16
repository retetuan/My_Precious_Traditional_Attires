package com.example.myprecioustraditionalattires.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.myprecioustraditionalattires.models.Client
import com.example.myprecioustraditionalattires.models.SignupModel
import com.example.myprecioustraditionalattires.navigation.ROUTE_ADD_ITEM
import com.example.myprecioustraditionalattires.navigation.ROUTE_HOME
import com.example.myprecioustraditionalattires.navigation.ROUTE_HOME_ONE
import com.example.myprecioustraditionalattires.navigation.ROUTE_HOME_TWO
import com.example.myprecioustraditionalattires.navigation.ROUTE_LOGIN
import com.example.myprecioustraditionalattires.navigation.ROUTE_VIEW_CLIENT
import com.example.myprecioustraditionalattires.navigation.ROUTE_VIEW_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun signup(
        userName: String,
        email: String, password: String, confirmPassword: String,
        userType: String, navController: NavController, context: Context
    ) {
        // Check if all fields are filled
        if (userName.isBlank() ||
            email.isBlank() || password.isBlank() || confirmPassword.isBlank() || userType.isBlank()) {
            showToast("Please fill all the fields", context)
            return
        }

        // Validate if passwords match
        if (password != confirmPassword) {
            showToast("Passwords do not match", context)
            return
        }

        // Show loading indicator
        _isLoading.value = true

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _isLoading.value = false

            if (task.isSuccessful) {
                // Get the user ID
                val userId = mAuth.currentUser?.uid ?: ""

                // Create a user model for saving to the database
                val userData = SignupModel(
                    userName = userName,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    userType = userType,
                    userId = userId
                )

                // Save user data to the Firebase Realtime Database
                saveUserToDatabase(userId, userData, navController, context)

                // Update user profile with display name
                val user = mAuth.currentUser
                val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(userData.userName)
                    .build()

                user?.updateProfile(profile)?.addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        showToast("Display name set correctly", context)
                    } else {
                        showToast("Failed to set display name", context)
                    }
                }
            } else {
                _errorMessage.value = task.exception?.message
                showToast(task.exception?.message ?: "Registration failed", context)
            }
        }
    }

    fun saveUserToDatabase(
        userId: String, userData: SignupModel,
        navController: NavController, context: Context
    ) {
        val regRef = FirebaseDatabase.getInstance()
            .getReference("Users/$userId")
        regRef.setValue(userData).addOnCompleteListener { regRefTask ->
            if (regRefTask.isSuccessful) {
                showToast("User Successfully Registered", context)
                navController.navigate(ROUTE_LOGIN)
            } else {
                _errorMessage.value = regRefTask.exception?.message
                showToast(
                    regRefTask.exception?.message ?: "Database error",
                    context
                )
            }
        }
    }

    fun viewUsers(client: MutableState<Client>,
                  clients: SnapshotStateList<Client>, context: Context
    ): SnapshotStateList<Client> {
        val ref = FirebaseDatabase.getInstance().getReference()
            .child("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clients.clear()
                for (snap in snapshot.children) {
                    val value = snap.getValue(Client::class.java)
                    value?.let {
                        client.value = it
                        clients.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch users,", context)
            }
        })
        return clients
    }

    fun updateUser(context: Context, navController: NavController,
                   userName: String, email: String, userType: String, password: String, userId: String) {
        val databaseReference = FirebaseDatabase.getInstance()
            .getReference("Users/$userId")
        val updateUser = Client(userId, userName, email, userType, password)
        databaseReference.setValue(updateUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Client Updated Successfully", context)
                    navController.navigate(ROUTE_VIEW_USERS)
                } else {
                    showToast("Record Update failed", context)
                }
            }
    }

    fun deleteUser(context: Context, userId: String,
                   navController: NavController) {
        AlertDialog.Builder(context)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete this user?")
            .setPositiveButton("Yes") { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Users/$userId")
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("User deleted Successfully,", context)
                    } else {
                        showToast("User not deleted", context)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun login(email: String, password: String, navController: NavController, context: Context) {
        if (email.isBlank() || password.isBlank()) {
            showToast("Email and password are required", context)
            return
        }

        _isLoading.value = true

        // Authenticate the user with email and password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _isLoading.value = false

            if (task.isSuccessful) {
                val userId = mAuth.currentUser?.uid ?: ""
                // Fetch the user's data from the Firebase database
                val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

                userRef.get().addOnCompleteListener { dataTask ->
                    if (dataTask.isSuccessful && dataTask.result.exists()) {
                        val userType = dataTask.result.child("userType").value.toString()

                        // Navigate to respective dashboard based on userType
                        when (userType) {
                            "User" -> {
                                showToast("Welcome, Client!", context)
                                navController.navigate(ROUTE_HOME_TWO)
                            }
                            "Administrator" -> {
                                showToast("Welcome, Admin!", context)
                                navController.navigate(ROUTE_HOME_ONE)
                            }
                            else -> {
                                showToast("User type not recognized", context)
                            }
                        }
                    } else {
                        showToast("Failed to retrieve user data", context)
                    }
                }.addOnFailureListener {
                    showToast("Database error: ${it.message}", context)
                }
            } else {
                _errorMessage.value = task.exception?.message
                showToast(task.exception?.message ?: "Login Failed", context)
            }
        }
    }

    fun logout(navController: NavController, context: Context) {
        FirebaseAuth.getInstance().signOut()
        showToast("Logged Out Successfully", context)
        navController.navigate(ROUTE_LOGIN)
    }
}
