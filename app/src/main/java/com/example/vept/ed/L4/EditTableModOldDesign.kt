package com.example.vept.ed.L4

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/*
1. 테이블 이름을 수정할 수 있는 텍스트
2. 필드 추가 버튼이 있어야 한다.
2.1. 필드 추가를 누를시 4.선택된 필드 내부 수정으로 간다.
3. 필드는 [이름, 타입, [pk nn ai u] 활성화 여부, 디폴트 값] 로 구현된다.
3.1. 필드는 선택될 수 있으며, 내부 수정을 할 수 있다.
3.2. 필드는 삭제될 수 있으며, 각 필드에 삭제 버튼이 있다.
3.3. 필드는 처음 상태를 가져오고 수정할 수 있는 상태이다.
4. 선택된 필드 내부 수정
4.1. 이미 있던 테이블이기 때문에 기존 자료와 충돌하는지 문제를 확인해야 한다.
4.1.1. 즉 수정될 수 없는 외부 참조가 되고 있는 필드는 ui로 필드가 수정 삭제가 무력화 되어 있어야 한다
4.1.2. 그외의 수정은 기존과 동일 할것이다.
4.2. 조건 -> 필드 중복 이름 검사 + 필드는 이름과 있어야 한다. [타입은 기본 정수로 제공할 것임]
5. 적용하기 버튼이 있다.
5.1. 조건-> 테이블 이름이 필요함 + 필드는 한 개 이상 필요함
5.2. 이 모든 것이 적용된 테이블이 생성된다. -> 다일로그로 뛰우고 PK검증후 실제 적용된다.
6. sql 실행 에러
6.1. 테이블 이름이 이미 있어 실패한 경우, 메시지를 뛰우고 적용하기 버튼을 누르기 전 상태를 유지한다. -> 아직 필드 값은 유지되고 있다.
6.2. 그외의 실행 에러가 있는 경우는 메시지만 뛰운다.
*/




data class FieldFix(
    val name: String,
    val type: String = "INTEGER",
    val isPrimaryKey: Boolean = false,
    val isNotNull: Boolean = false,
    val isAutoIncrement: Boolean = false,
    val isUnique: Boolean = false,
    val defaultValue: String? = null,
    val isReferenced: Boolean = false
)


fun FieldFix.toField(): Field {
    return Field(
        name = this.name,
        type = this.type,
        isPrimaryKey = this.isPrimaryKey,
        isAutoIncrement = this.isAutoIncrement,
        isNotNull = this.isNotNull,
        isUnique = this.isUnique,
        defaultValue = this.defaultValue,
        isReferenced = this.isReferenced
    )
}





@Composable
fun EditTableModOldDesign(
    viewModel: EditTableModViewModel,
    navController: NavHostController,
    name: String,
    type: String
) {
    var tableName by remember { mutableStateOf(name) }
    var fields by remember { mutableStateOf(listOf<Field>()) }

    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingFieldIndex by remember { mutableStateOf(-1) }
    var editingField by remember { mutableStateOf<Field?>(null) }

    var showSqlDialog by remember { mutableStateOf(false) }
    var generatedSql by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val loadedFields = viewModel.getEditableFieldsForTable(name)
        fields = loadedFields.map { it.toField() }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("기존 테이블 수정", fontSize = 20.sp)

        OutlinedTextField(
            value = tableName,
            onValueChange = { tableName = it },
            label = { Text("테이블 이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = { showDialog = true }) {
            Text("➕ 필드 추가")
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(fields) { index, field ->
                val isDisabled = field.isReferenced

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            if (isDisabled) Color.LightGray.copy(alpha = 0.2f) else Color.Unspecified
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${field.name} (${field.type})", modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                if (!isDisabled) {
                                    editingFieldIndex = index
                                    editingField = field
                                    showEditDialog = true
                                }
                            },
                            enabled = !isDisabled
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "수정")
                        }

                        IconButton(
                            onClick = {
                                if (!isDisabled) {
                                    fields = fields.toMutableList().apply { removeAt(index) }
                                }
                            },
                            enabled = !isDisabled
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                generatedSql = generateCreateTableQuery(tableName, fields)
                showSqlDialog = true
            },
            enabled = tableName.isNotBlank() && fields.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("적용하기")
        }
    }

    if (showDialog) {
        FieldEditDialogCustom(
            existingNames = fields.map { it.name },
            onConfirm = { newField ->
                fields = fields + newField
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    if (showEditDialog && editingField != null) {
        FieldEditDialogCustom(
            existingNames = fields.mapIndexedNotNull { i, f -> if (i != editingFieldIndex) f.name else null },
            initialField = editingField,
            onConfirm = { updatedField ->
                fields = fields.toMutableList().apply {
                    this[editingFieldIndex] = updatedField
                }
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    if (showSqlDialog) {
        AlertDialog(
            onDismissRequest = { showSqlDialog = false },
            title = { Text("생성될 SQL") },
            text = {
                Text(
                    generatedSql,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val pkCount = fields.count { it.isPrimaryKey }
                        if (pkCount > 1) {
                            Log.e("EditTable", "PK는 하나만 지정해야 합니다.")
                            return@TextButton
                        }

                        val success = viewModel.recreateTableWithFix(tableName, fields)
                        if (success) {
                            showSqlDialog = false
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            Log.e("EditTable", "테이블 재생성 실패")
                        }
                    },
                    enabled = tableName.isNotBlank() && fields.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("적용하기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSqlDialog = false }) {
                    Text("닫기")
                }
            }
        )
    }
}






