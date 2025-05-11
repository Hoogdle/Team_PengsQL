package com.example.vept.ed.L4

/*
*
* 중요한건
상단2줄, 열 1줄은 동적으로 보이게 했어도
그 내부의 칸들이 실제로는 동적구조가 아니여야 했다는 것

그렇다면 "상단 2줄" 객체 1개 "열 1줄" 객체 1개를 인위적 배치
그리고 그 사이에 (비율에 잘 맞게) 정적 차트가 들어가야 했다는 점이다
*
*
*중요한건
상단2줄, 열 1줄은 동적으로 보이게 했어도
그 내부의 칸들이 실제로는 동적구조가 아니여야 했다는 것

그렇다면 "상단 2줄" 객체 1개 "열 1줄" 객체 1개를 인위적 배치
그리고 그 사이에 (비율에 잘 맞게) 정적 차트가 들어가야 했다는 점이다
*
*
*
*
*대략 구상중인 구조

[테이블 이름]      간격   [상단 ui]
----------디자인구분선--------------
     간격     [열 이름]
     간격     [검색줄]
[인덱스 열]  [정적 차트]
    [페이징 관련 ui]

*
*칸 하나에 허용되는 최대 글자 수: 30자 3줄 정도 (총 90자)
*
*
* */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vept.R

import com.example.vept.ui.other.ArrowAndMenu
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.TableBackGroundColor
import com.example.vept.ui.theme.TitleColor


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.AlertDialog

import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import kotlin.math.ceil

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp





@Composable
fun EditTableListDesign(
    viewModel: EditTableListViewModel,
    navController: NavHostController
) {
    val TableName = viewModel.getItemName() ?: "이름 없음"
    val ChartType = viewModel.getItemType() ?: "타입 없음"
    val isEditable = viewModel.getItemType() == "테이블"   // 0은 뷰
    val currentPage = remember { mutableStateOf("1") }
    val isInputChanged = remember { mutableStateOf(false) }
    val itemsPerPage = 50


    Column (
        Modifier
            .background(BackGroundColor)
            .padding(top = 25.dp)
    ){
        ArrowAndMenu()
        Spacer(modifier = Modifier.height(15.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            SelectDBTitle("$TableName")
            ReturnToMainButton(navController)       //<<===디버그용===!!//
        }
        DBTable(
            viewModel = viewModel,
            TableName = TableName,
            currentPage = currentPage,
            isEditable = isEditable,
            itemsPerPage = itemsPerPage,
            isInputChanged = isInputChanged
        )
    }
}


@Composable
fun DBTable(
    viewModel: EditTableListViewModel,
    TableName: String,
    currentPage: MutableState<String>,
    isInputChanged: MutableState<Boolean>,
    isEditable: Boolean,
    itemsPerPage: Int
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    val fieldNames = remember {
        viewModel.getFieldNamesNow(TableName)
    }

    val filters = remember { mutableStateOf(List(fieldNames.size) { "" }) }

    // 테이블 데이터 갱신
    val tableData = remember(currentPage.value, filters.value) {
        mutableStateOf(
            viewModel.getFilteredTablePageDataNow(
                TableName,
                currentPage.value.toInt(),
                itemsPerPage,
                filters.value
            )
        )
    }

    // Column 너비 계산
    val columnWidths = remember(tableData.value) {
        val defaultWidth = 100.dp
        tableData.value.firstOrNull()?.indices?.map { col ->
            val maxChar = tableData.value.maxOfOrNull { it.getOrNull(col)?.length ?: 0 } ?: 0
            if (maxChar > 5) defaultWidth + (maxChar - 5) * 5.dp else defaultWidth
        } ?: List(fieldNames.size) { defaultWidth }
    }

    // Row 높이 계산
    val rowHeights = remember(tableData.value) {
        val defaultHeight = 40.dp
        tableData.value.map { row ->
            // 한 줄로 출력되므로 maxLine을 1로 설정
            defaultHeight
        }
    }


    // isInputChanged가 true일 때 데이터 갱신
    LaunchedEffect(isInputChanged.value) {
        if (isInputChanged.value) {
            // 데이터 갱신
            tableData.value = viewModel.getFilteredTablePageDataNow(
                TableName,
                currentPage.value.toInt(),
                itemsPerPage,
                filters.value
            )
            // 갱신 후 isInputChanged를 false로 초기화
            isInputChanged.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
            .background(TableBackGroundColor)
            .padding(horizontal = 30.dp)
            .horizontalScroll(horizontalScrollState)
            .verticalScroll(verticalScrollState)
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        BarTop(fieldNames, columnWidths, filters)
        Row {
            BarLeftIndex(
                tableData.value.size,
                rowHeights,
                currentPage,
                itemsPerPage
            )
            BoxTable(
                data = tableData.value,
                columnWidths = columnWidths,
                rowHeights = rowHeights,
                fieldNames = fieldNames,
                viewModel = viewModel,
                currentPage = currentPage,
                triggerPageUpdate = isInputChanged,
                isEditable = isEditable
            )
        }
        Spacer(Modifier.height(16.dp))
        PageController(
            currentPage = currentPage,
            isInputChanged = isInputChanged
        )
    }
}


@Composable
fun BarTop(
    fieldNames: List<String>,
    columnWidths: List<Dp>,
    filters: MutableState<List<String>>
) {
    Column {
        Row {
            Spacer(Modifier.width(60.dp))
            fieldNames.forEachIndexed { index, name ->
                Box(
                    modifier = Modifier
                        .width(columnWidths[index])
                        .height(40.dp)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = name.take(10),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Row {
            Spacer(Modifier.width(60.dp))
            fieldNames.forEachIndexed { index, _ ->
                TextField(
                    value = filters.value[index],
                    onValueChange = { newValue ->
                        filters.value = filters.value.toMutableList().also {
                            it[index] = newValue
                        }
                    },
                    modifier = Modifier
                        .width(columnWidths[index])
                        .height(40.dp)
                        .border(1.dp, Color.Gray),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun BarLeftIndex(
    rowCount: Int,
    rowHeights: List<Dp>,
    currentPage: MutableState<String>,
    itemsPerPage: Int
) {
    Column {
        repeat(rowCount) { index ->
            // 실제 인덱스를 페이지에 맞춰 계산
            val pageIndex = index + 1 + (currentPage.value.toInt() - 1) * itemsPerPage
            val numStr = "$pageIndex"
            val width = max(60.dp, (numStr.length * 12).dp)

            Box(
                modifier = Modifier
                    .width(width)
                    .height(rowHeights[index])
                    .border(1.dp, Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = numStr)
            }
        }
    }
}

@Composable
fun BoxTable(
    data: List<List<String>>,
    columnWidths: List<Dp>,
    rowHeights: List<Dp>,
    fieldNames: List<String>,
    viewModel: EditTableListViewModel,
    currentPage: MutableState<String>,
    triggerPageUpdate: MutableState<Boolean>,
    isEditable: Boolean
) {
    Column {
        data.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cellText ->
                    val width = columnWidths.getOrNull(colIndex) ?: 100.dp
                    val height = rowHeights.getOrNull(rowIndex) ?: 50.dp
                    val modifier = Modifier
                        .width(width)
                        .height(height)
                        .border(1.dp, Color.Gray)

                    if (isEditable) {
                        EditableTableCell(
                            text = cellText ?: "NULL",
                            rowIndex = rowIndex,
                            colIndex = colIndex,
                            fieldNames = fieldNames,
                            rowData = row,
                            viewModel = viewModel,
                            currentPage = currentPage,
                            triggerPageUpdate = triggerPageUpdate,
                            modifier = modifier
                        )
                    } else {
                        ReadOnlyCell(
                            text = cellText,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditableTableCell(
    text: String?,
    rowIndex: Int,
    colIndex: Int,
    fieldNames: List<String>,
    rowData: List<String>,
    viewModel: EditTableListViewModel,
    currentPage: MutableState<String>,
    triggerPageUpdate: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val editing = remember { mutableStateOf(false) }
    var tempValue by remember { mutableStateOf(text ?: "NULL") }  // 초기값에 null-safe

    if (editing.value) {
        AlertDialog(
            onDismissRequest = { editing.value = false },
            title = { Text("셀 수정") },
            text = {
                Column {
                    Text("변경할 값을 입력하세요:")
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = tempValue,
                        onValueChange = { tempValue = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    editing.value = false
                    if (tempValue != (text ?: "NULL")) {
                        val field = fieldNames.getOrNull(colIndex) ?: return@TextButton
                        viewModel.updateRowBySnapshot(
                            rowData,
                            fieldNames,
                            field,
                            tempValue
                        )
                        triggerPageUpdate.value = true
                    }
                }) {
                    Text("수정하기")
                }
            },
            dismissButton = {
                TextButton(onClick = { editing.value = false }) {
                    Text("취소")
                }
            }
        )
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    editing.value = true
                })
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text ?: "NULL",  // null-safe 렌더링
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ReadOnlyCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


// 일단 페이징은 더 작업 할 수 있으면 돌아오고
// 최대 페이지의 문제 
// 시스템을 동원할거임? -> 한다면 필터 고려하셈
// 안할거면 일단 5페이지 업다운 만 존재하는거고
@Composable
fun PageControler(currentPage: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            val page = (currentPage.value.toIntOrNull() ?: 1).coerceAtLeast(2) - 1
            currentPage.value = page.toString()
        }) {
            Text("이전")
        }

        Spacer(Modifier.width(16.dp))
        Text("현재 페이지: ${currentPage.value}", modifier = Modifier.align(Alignment.CenterVertically))

        Spacer(Modifier.width(16.dp))
        Button(onClick = {
            val page = (currentPage.value.toIntOrNull() ?: 1) + 1
            currentPage.value = page.toString()
        }) {
            Text("다음")
        }
    }
}


@Composable
fun PageController(
    currentPage: MutableState<String>,
    isInputChanged: MutableState<Boolean>
) {
    val input = remember { mutableStateOf(currentPage.value) }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // -5 페이지로
        DBTableArrowButton(ArrowDirection.LEFT_5, currentPage)
        // -1 페이지로
        DBTableArrowButton(ArrowDirection.LEFT, currentPage)

        Spacer(Modifier.width(8.dp))

        // 직접 입력
        BasicTextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier
                .width(50.dp)
                .height(45.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = true
        )

        Spacer(Modifier.width(4.dp))

        // +1 페이지로
        DBTableArrowButton(ArrowDirection.RIGHT, currentPage)
        // +5 페이지로
        DBTableArrowButton(ArrowDirection.RIGHT_5, currentPage)
    }

    // 입력 동기화
    LaunchedEffect(currentPage.value) {
        input.value = currentPage.value
    }

    LaunchedEffect(input.value) {
        val pageInt = input.value.toIntOrNull()
        if (pageInt != null && pageInt > 0) {
            currentPage.value = input.value
            isInputChanged.value = true
        }
    }
}

@Composable
fun DBTableArrowButton(
    direction: ArrowDirection,
    currentPage: MutableState<String>
) {
    val onClick: () -> Unit = when (direction) {
        ArrowDirection.LEFT -> {
            { if (currentPage.value.toInt() > 1) currentPage.value = (currentPage.value.toInt() - 1).toString() }
        }
        ArrowDirection.RIGHT -> {
            { currentPage.value = (currentPage.value.toInt() + 1).toString() }
        }
        ArrowDirection.LEFT_5 -> {
            { if (currentPage.value.toInt() > 5) currentPage.value = (currentPage.value.toInt() - 5).toString() }
        }
        ArrowDirection.RIGHT_5 -> {
            { currentPage.value = (currentPage.value.toInt() + 5).toString() }
        }
    }

    val icon = when (direction) {
        ArrowDirection.LEFT -> R.drawable.one_left
        ArrowDirection.RIGHT -> R.drawable.one_right
        ArrowDirection.LEFT_5 -> R.drawable.double_left
        ArrowDirection.RIGHT_5 -> R.drawable.double_right
    }

    Icon(
        painter = painterResource(icon),
        contentDescription = "",
        tint = TitleColor,
        modifier = Modifier.clickable { onClick() }
    )
}

enum class ArrowDirection {
    LEFT, RIGHT, LEFT_5, RIGHT_5
}
