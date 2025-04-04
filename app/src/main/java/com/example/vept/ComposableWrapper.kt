package com.example.vept

import androidx.compose.ui.platform.ComposeView

// Java에서 호출할 수 있도록 `@JvmStatic` 추가
object ComposableWrapper {
    @JvmStatic
    fun setComposableContent(composeView: ComposeView, viewModel: MainDesignViewModel) {
        composeView.setContent {
            MainDesign(viewModel)
        }
    }
}
