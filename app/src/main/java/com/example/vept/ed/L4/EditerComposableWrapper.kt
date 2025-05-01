package com.example.vept.ed.L4

import android.net.Uri
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object EditerComposableWrapper {
    @JvmStatic
    fun setNavComposableContent(
        composeView: ComposeView,
        mainViewModel: EditerMainViewModel,
        cliViewModel: EditSqlCliViewModel,
        listViewModel: EditTableListViewModel,
        modViewModel: EditTableModViewModel
    ) {
        composeView.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    EditerMainDesign(mainViewModel, navController)
                }
                composable("sql?name={name}&type={type}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType; defaultValue = "" },
                        navArgument("type") { type = NavType.StringType; defaultValue = "" }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val type = backStackEntry.arguments?.getString("type") ?: ""
                    cliViewModel.setItemInfo(name, type)
                    EditSqlCliDesign(cliViewModel, navController)
                }
                composable("list?name={name}&type={type}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType; defaultValue = "" },
                        navArgument("type") { type = NavType.StringType; defaultValue = "" }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val type = backStackEntry.arguments?.getString("type") ?: ""
                    listViewModel.setItemInfo(name, type)
                    EditTableListDesign(listViewModel, navController)
                }
                composable("mod?name={name}&type={type}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType; defaultValue = "" },
                        navArgument("type") { type = NavType.StringType; defaultValue = "" }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val type = backStackEntry.arguments?.getString("type") ?: ""
                    modViewModel.setItemInfo(name, type)
                    EditTableModDesign(modViewModel, navController)
                }
            }
        }
    }
}