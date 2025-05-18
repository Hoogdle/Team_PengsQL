package com.example.vept.ed.L4

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch


@Composable
fun EditTableModDesign(
    viewModel: EditTableModViewModel,
    navController: NavHostController
) {
    val itemName = viewModel.getItemName() ?: ""
    val isCreateMode = itemName.isBlank()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (isCreateMode) "새 테이블 생성" else "기존 테이블 수정",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 공통 UI: 테이블 이름 수정
        ModTableName(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        if (isCreateMode) {
            NewTable(viewModel)
        } else {
            OldTable(viewModel)
        }
    }
}

@Composable
fun ModTableName(viewModel: EditTableModViewModel) {
    val name by viewModel.tableName.collectAsState()
    OutlinedTextField(
        value = name,
        onValueChange = { viewModel.updateTableName(it) },
        label = { Text("테이블 이름") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NewTable(viewModel: EditTableModViewModel) {
    Text("필드를 추가하세요")

    Button(onClick = {
        viewModel.applyNewTable()
    }) {
        Text("테이블 생성")
    }
}


@Composable
fun OldTable(viewModel: EditTableModViewModel) {
    Text("기존 필드를 수정하세요")

    Button(onClick = {
        viewModel.applyModifyTable()
    }) {
        Text("수정 사항 적용")
    }
}
