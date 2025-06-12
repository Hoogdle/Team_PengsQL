package com.pangsql.vept

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object ComposableWrapper {
    @JvmStatic
    fun setComposableContent(composeView: ComposeView, viewModel: MainDesignViewModel) {
        composeView.setContent {
            //MainDesign(viewModel)
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Color(216,224,227)
            )
            Home(viewModel)
        }
    }
}
