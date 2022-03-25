package com.example.eplan.presentation.ui
//
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.eplan.presentation.ui.components.BottomNavBar
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {
//            val cr = currentRoute(navController = navController)
//            if (cr?.contains("activityDetails") == false && !cr.contains("appointmentDetails")) {
//                BottomNavBar(navController = navController)
//            }
//        }
//    ) {
//        ApplicationNavGraph(navController = navController)
//    }
//}
//
//@Composable
//fun currentRoute(navController: NavHostController): String? {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    return navBackStackEntry?.destination?.route
//}