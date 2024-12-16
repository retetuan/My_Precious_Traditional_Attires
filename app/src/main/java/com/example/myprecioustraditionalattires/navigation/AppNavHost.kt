package com.example.myprecioustraditionalattires.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprecioustraditionalattires.ui.screens.ClientDashboard
import com.example.myprecioustraditionalattires.ui.theme.screens.Client.AddClientScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.Client.ViewClients
import com.example.myprecioustraditionalattires.ui.theme.screens.Client.updateClientScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.Item.AddItemScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.Item.ViewItems
import com.example.myprecioustraditionalattires.ui.theme.screens.Item.updateItemScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.PaymentScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.SplashScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.dashboards.AdminDashboard
import com.example.myprecioustraditionalattires.ui.theme.screens.login.loginScreen
import com.example.myprecioustraditionalattires.ui.theme.screens.signup.signupScreen
import editItem

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ROUTE_SPLASH) {
            SplashScreen {
                navController.navigate(ROUTE_REGISTER) {
                    popUpTo(ROUTE_SPLASH) { inclusive = true }
                }
            }
        }
        composable(ROUTE_REGISTER) { signupScreen(navController) }
        composable(ROUTE_LOGIN) { loginScreen(navController) }
        composable(ROUTE_PAY) { PaymentScreen(navController) }
        composable(ROUTE_HOME_ONE) { AdminDashboard(navController) }
        composable(ROUTE_ADD_CLIENT) { AddClientScreen(navController) }
        composable(ROUTE_VIEW_CLIENT) { ViewClients(navController) }
        composable("$ROUTE_UPDATE_CLIENT/{id}") { backStackEntry ->
            updateClientScreen(
                navController,
                backStackEntry.arguments?.getString("id").orEmpty()
            )
        }
        composable(ROUTE_VIEW_ITEM) { ViewItems(navController) }
        composable("$ROUTE_UPDATE_ITEM/{id}") { backStackEntry ->
            updateItemScreen(
                navController,
                backStackEntry.arguments?.getString("id").orEmpty()
            )
        }
        composable(ROUTE_ADD_ITEM) { AddItemScreen(navController) }
        composable(ROUTE_HOME_TWO) {
            val context = LocalContext.current
            ClientDashboard(context = context, navController = navController)
        }
        composable(ROUTE_EDIT_ITEM) { editItem(navController) }
        composable(ROUTE_VIEW_USERS) { ViewItems(navController) } // Check if this is intentional or needs adjustment
        composable("$ROUTE_UPDATE_USERS/{id}") { backStackEntry ->
            updateItemScreen(
                navController,
                backStackEntry.arguments?.getString("id").orEmpty()
            )
        }
    }
}
