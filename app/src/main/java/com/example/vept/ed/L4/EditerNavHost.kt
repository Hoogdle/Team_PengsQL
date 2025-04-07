package com.example.vept.ed.L4

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun EditerNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "main"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            EditerMainDesign(
                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("mod") {
            EditTableModDesign(
                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("sql") {
            EditSqlCliDesign(
                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("list") {
            EditTableListDesign(
                viewModel = viewModel(),
                navController = navController
            )
        }
    }
}
