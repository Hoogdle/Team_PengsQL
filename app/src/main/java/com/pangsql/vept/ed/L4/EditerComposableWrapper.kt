package com.pangsql.vept.ed.L4

import androidx.compose.ui.platform.ComposeView
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pangsql.vept.Home
import com.pangsql.vept.MainDesignViewModel
import com.pangsql.vept.pl.L4.MainDesign
import com.pangsql.vept.pl.L4.PlannerDiagramViewModel

object EditerComposableWrapper {
    @JvmStatic
    fun setNavComposableContent(
        composeView: ComposeView,
        mainViewModel: EditerMainViewModel,
        cliViewModel: EditSqlCliViewModel,
        listViewModel: EditTableListViewModel,
        modViewModel: EditTableModViewModel,
        homeViewModel: MainDesignViewModel,
        diagramViewModel: PlannerDiagramViewModel
    ) {
        composeView.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    EditerMainDesign(mainViewModel, navController)
                }

                composable("diagram"){
                    MainDesign(diagramViewModel)
                }
                composable("home"){
                    Home(viewModel = homeViewModel)
                }
                // 임시로 ai navigator 생성
                composable("ai"){
                    EditMCPsDesign(viewModel = cliViewModel, navController)
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
                composable(
                    "mod?name={name}&type={type}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType; defaultValue = "" },
                        navArgument("type") { type = NavType.StringType; defaultValue = "테이블" }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val type = backStackEntry.arguments?.getString("type") ?: "테이블"

                    if (name.isBlank()) {
                        EditTableModNewDesign(
                            viewModel = modViewModel,
                            navController = navController
                        )
                    } else {
                        EditTableModOldDesign(
                            viewModel = modViewModel,
                            navController = navController,
                            name = name,
                            type = type
                        )
                    }
                }

            }
        }
    }
}