package com.example.vept.ed.L4

import androidx.compose.ui.platform.ComposeView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController




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
                composable("sql") {
                    EditSqlCliDesign(cliViewModel, navController)
                }
                composable("list") {
                    EditTableListDesign(listViewModel, navController)
                }
                composable("mod") {
                    EditTableModDesign(modViewModel, navController)
                }
            }
        }
    }

}
