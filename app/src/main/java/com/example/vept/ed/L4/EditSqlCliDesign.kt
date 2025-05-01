package com.example.vept.ed.L4

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController


@Composable
fun EditSqlCliDesign(
    viewModel: EditSqlCliViewModel,
    navController: NavHostController
){
    val itemName = viewModel.getItemName() ?: "이름 없음"
    val itemType = viewModel.getItemType() ?: "타입 없음"

    Column {
        ReturnToMainButton(navController)
        Text(text = "EditSqlCliViewModel에서 수정할 항목: $itemName ($itemType)")  // 조회 항목 출력
    }
}
