package com.pangsql.vept.ed.L4

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
import com.pangsql.vept.R

import com.pangsql.vept.ui.theme.BackGroundColor
import com.pangsql.vept.ui.theme.TableBackGroundColor
import com.pangsql.vept.ui.theme.TitleColor


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog

import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.pangsql.vept.ui.other.ArrowAndTitle
import com.pangsql.vept.ui.theme.TextColor


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
        ArrowAndTitle(navController = navController, title = TableName, destination = "main")

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarTop(
    fieldNames: List<String>,
    columnWidths: List<Dp>,
    filters: MutableState<List<String>>
) {

    val interactionSource = remember{ MutableInteractionSource() }

    Column {
        Row {
            Spacer(Modifier.width(60.dp))
            fieldNames.forEachIndexed { index, name ->
                Box(
                    modifier = Modifier
                        .width(columnWidths[index])
                        .height(40.dp)
                        .border(0.5.dp, Color.LightGray)
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
//            fieldNames.forEachIndexed { index, _ ->
//                TextField(
//                    value = filters.value[index],
//                    onValueChange = { newValue ->
//                        filters.value = filters.value.toMutableList().also {
//                            it[index] = newValue
//                        }
//                    },
//                    modifier = Modifier
//                        .width(columnWidths[index])
//                        .height(40.dp)
//                        .border(0.5.dp, Color.LightGray),
//                    singleLine = true
//                )
//            }

            fieldNames.forEachIndexed { index, _ ->
                BasicTextField(
                    // 필터 입력 후 Action에 대해 정의
                    keyboardActions = KeyboardActions(
                        // 예시)
                        // 백엔드로 필터링 된 데이터 요청 함수
                        // callFilter2Backend(input)
                    ),
                    value = filters.value[index],
                    onValueChange = {
                            newValue ->
                        filters.value = filters.value.toMutableList().also {
                            it[index] = newValue
                        }
                    },
                    modifier = Modifier
                        .border(
                            width = 0.5.dp,
                            color = Color.LightGray
                        )
                        .width(columnWidths[index])
                        .height(40.dp)
                        .width(50.dp)
                        .zIndex(1f)
                        .offset(
                            x = -4.dp,
                            y= -2.dp
                        ),
                    singleLine = true,
                    textStyle = TextStyle(

                        fontSize = 14.sp
                    ),

                    decorationBox = @Composable{ innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            placeholder = {
                                Text(
                                    modifier = Modifier
                                        .offset(
                                            y = 0.dp
                                        ),
                                    text = "Filter",
                                    color = Color.LightGray,
                                    style = TextStyle(
                                        color = TitleColor,
                                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                        fontSize = 14.sp
                                    ),
                                )
                            },
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            enabled = true,
                            innerTextField = innerTextField,
                            value = filters.value[index],
                            interactionSource = interactionSource,
                            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                top = 0.dp,
                                bottom = 0.dp
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = TextColor,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        )
                    }
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
                    .border(0.5.dp, Color.LightGray),
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
                        .border(0.5.dp, Color.LightGray)

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


        // 직접 입력
        BasicTextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier
                .offset(
                    x = 20.dp,
                    y = 3.dp
                )
                .width(50.dp)
                .height(45.dp)
                .padding(),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = true
        )


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
