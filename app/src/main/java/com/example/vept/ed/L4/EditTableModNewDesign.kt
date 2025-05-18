package com.example.vept.ed.L4

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


/*
1. 테이블 이름을 작성할 수 있는 텍스트
2. 필드 추가 버튼이 있어야 한다.
2.1. 필드 추가를 누를시 4.선택된 필드 내부 수정으로 간다.
3. 필드는 [이름, 타입, [pk nn ai u] 활성화 여부, 디폴트 값] 로 구현된다.
3.1. 필드는 선택될 수 있으며, 내부 수정을 할 수 있다.
3.2. 필드는 삭제될 수 있으며, 각 필드에 삭제 버튼이 있다.
4. 선택된 필드 내부 수정
4.1. 이미 있던 테이블이 아니기에(구현된 테이블 수정이 아님), 수정은 자유롭다.
4.2. 조건 -> 필드 중복 이름 검사 + 필드는 이름과 있어야 한다. [타입은 기본 정수로 제공할 것임]
5. 적용하기 버튼이 있다.
5.1. 조건-> 테이블 이름이 필요함 + 필드는 한 개 이상 필요함
5.2. 이 모든 것이 적용된 테이블이 생성된다.
6. sql 실행 에러
6.1. 테이블 이름이 이미 있어 실패한 경우, 메시지를 뛰우고 적용하기 버튼을 누르기 전 상태를 유지한다. -> 아직 필드 값은 유지되고 있다.
6.2. 그외의 실행 에러가 있는 경우는 메시지만 뛰운다.

* */

data class Field(
    val name: String,
    val type: String = "INTEGER",
    val isPrimaryKey: Boolean = false,
    val isNotNull: Boolean = false,
    val isAutoIncrement: Boolean = false,
    val isUnique: Boolean = false,
    val defaultValue: String? = null,
    val isReferenced: Boolean = false
)
@Composable
fun EditTableModNewDesign(
    viewModel: EditTableModViewModel,
    navController: NavHostController
) {
    var tableName by remember { mutableStateOf("") }
    var fields by remember { mutableStateOf(listOf<Field>()) }

    var showDialog by remember { mutableStateOf(false) }
    var showSqlDialog by remember { mutableStateOf(false) }
    var generatedSql by remember { mutableStateOf("") }

    var showEditDialog by remember { mutableStateOf(false) }
    var editingFieldIndex by remember { mutableStateOf(-1) }
    var editingField by remember { mutableStateOf<Field?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("새 테이블 생성", fontSize = 20.sp)

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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${field.name} (${field.type})", modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            editingFieldIndex = index
                            editingField = field
                            showEditDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "수정")
                        }
                        IconButton(onClick = {
                            fields = fields.toMutableList().apply { removeAt(index) }
                        }) {
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
                            // 경고 메시지 (Toast/Snackbar/Log 중 택)
                            Log.e("EditTable", "PK는 하나만 지정해야 합니다.")
                            return@TextButton
                        }

                        val success = viewModel.createTableFromFields(tableName, fields)
                        if (success) {
                            showSqlDialog = false
                            navController.popBackStack()
                        } else {
                            Log.e("EditTable", "테이블 생성 실패")
                        }
                    },
                    enabled = tableName.isNotBlank() && fields.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✅ 적용하기")
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


@Composable
fun FieldEditDialogCustom(
    existingNames: List<String>,
    initialField: Field? = null,
    onConfirm: (Field) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            FieldEditDialogContent(
                existingNames = existingNames,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun FieldEditDialogContent(
    existingNames: List<String>,
    onConfirm: (Field) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("INTEGER") }

    var isPk by remember { mutableStateOf(false) }
    var isNn by remember { mutableStateOf(false) }
    var isAi by remember { mutableStateOf(false) }

    var isUnique by remember { mutableStateOf(false) }
    var defaultValue by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {

        Text("필드 추가", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("필드 이름") },
            modifier = Modifier.fillMaxWidth()
        )

        TypeDropdownField(type = type, onTypeChange = { type = it })

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = defaultValue,
            onValueChange = { defaultValue = it },
            label = { Text("디폴트 값 (옵션)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isPk, onCheckedChange = { isPk = it })
                Text("Primary Key")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isNn, onCheckedChange = { isNn = it })
                Text("Not Null")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isAi, onCheckedChange = { isAi = it })
                Text("Auto Increment")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isUnique, onCheckedChange = { isUnique = it })
                Text("Unique")
            }
        }

        Spacer(Modifier.height(12.dp))

        error?.let {
            Text(it, color = Color.Red)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (name.isBlank()) {
                    error = "필드 이름은 필수입니다."
                } else if (existingNames.contains(name)) {
                    error = "필드 이름이 중복됩니다."
                } else {
                    onConfirm(
                        Field(
                            name = name,
                            type = type, // 🔹 타입 반영
                            isPrimaryKey = isPk,
                            isNotNull = isNn,
                            isAutoIncrement = isAi,
                            isUnique = isUnique,
                            defaultValue = defaultValue.takeIf { it.isNotBlank() }
                        )
                    )
                }
            }) {
                Text("추가")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdownField(
    type: String,
    onTypeChange: (String) -> Unit
) {
    val typeOptions = listOf("INTEGER", "REAL", "TEXT", "BLOB", "NULL")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = type,
            onValueChange = { onTypeChange(it) },
            label = { Text("타입") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            typeOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onTypeChange(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun generateCreateTableQuery(tableName: String, fields: List<Field>): String {
    val definitions = fields.joinToString(",\n  ") { field ->
        buildList {
            add("${field.name} ${field.type}")
            if (field.isPrimaryKey) add("PRIMARY KEY")
            if (field.isAutoIncrement) add("AUTOINCREMENT")
            if (field.isNotNull) add("NOT NULL")
            if (field.isUnique) add("UNIQUE")
            field.defaultValue?.let {
                val formatted = if (it.matches(Regex("^-?\\d+(\\.\\d+)?$"))) it else "'$it'"
                add("DEFAULT $formatted")
            }
        }.joinToString(" ")
    }
    return "CREATE TABLE $tableName (\n  $definitions\n);"
}


