package com.example.vept.pl.L4

import androidx.compose.ui.platform.ComposeView
import kotlinx.coroutines.flow.MutableStateFlow

object PlannerComposableWrapper {
    @JvmStatic
    fun setComposableContent(composeView: ComposeView, viewModel: PlannerDiagramViewModel) {
        composeView.setContent {
            MainDesign(viewModel)
        }
    }
}