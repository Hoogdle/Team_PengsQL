package com.example.vept.ed.L4

import androidx.compose.ui.platform.ComposeView

object EditerComposableWrapper {
    @JvmStatic
    fun setEditerComposableContent(composeView: ComposeView, viewModel: EditerMainViewModel) {
        composeView.setContent {
            EditerMainDesign(viewModel)
        }
    }
    @JvmStatic
    fun setSqlCliComposableContent(composeView: ComposeView, viewModel: EditSqlCliViewModel) {
        composeView.setContent {
            EditSqlCliDesign(viewModel)
        }
    }
    @JvmStatic
    fun setTableListComposableContent(composeView: ComposeView, viewModel: EditTableListViewModel) {
        composeView.setContent {
            EditTableListDesign(viewModel)
        }
    }
    @JvmStatic
    fun setTableModComposableContent(composeView: ComposeView, viewModel: EditTableModViewModel) {
        composeView.setContent {
            EditTableModDesign(viewModel)
        }
    }
}
