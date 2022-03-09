package com.example.eplan

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eplan.navigation.BottomNavGraph
import com.example.eplan.ui.BottomNavBar
import com.example.eplan.ui.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            Log.println(Log.DEBUG, "current route (MainScreen.kt)", currentRoute(navController = navController).toString())
            if (currentRoute(navController = navController)?.contains("activityDetails") == false) {
                BottomNavBar(navController = navController) }
            }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}