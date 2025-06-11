package com.pangsql.vept.pl.L4

import androidx.compose.ui.platform.ComposeView

object PlannerComposableWrapper {
    @JvmStatic
    fun setComposableContent(composeView: ComposeView, viewModel: PlannerDiagramViewModel) {
        composeView.setContent {
            MainDesign(viewModel)
        }
    }
}