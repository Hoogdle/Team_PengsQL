package com.example.vept

import androidx.compose.ui.platform.ComposeView

object ComposableWrapper {
    @JvmStatic
    fun setComposableContent(composeView: ComposeView, viewModel: MainDesignViewModel) {
        composeView.setContent {
            MainDesign(viewModel)
        }
    }
}
