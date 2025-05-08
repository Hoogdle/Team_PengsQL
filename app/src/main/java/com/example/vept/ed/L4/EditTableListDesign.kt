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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import kotlin.math.ceil
import kotlin.random.Random

private const val itemsPerPage = 50


@Composable
fun EditTableListDesign(
    viewModel: EditTableListViewModel,
    navController: NavHostController
) {
    val TableName = viewModel.getItemName() ?: "이름 없음"
    val ChartType = viewModel.getItemType() ?: "타입 없음"
    val isEditable = viewModel.getItemType() == "테이블"   // 0은 뷰
    val currentPage = remember { mutableStateOf("1") }

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
            isEditable = isEditable
        )
    }
}


@Composable
fun DBTable(
    viewModel: EditTableListViewModel,
    TableName: String,
    currentPage: MutableState<String>,
    isEditable: Boolean
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()



    // 임시 데이터: 50행 x 10열
    val rowCount = 50
    val columnCount = 15

    val tableData = remember(currentPage.value) {
        mutableStateOf(
            viewModel.getTablePageDataNow(TableName, currentPage.value.toInt(), itemsPerPage)
        )
    }

    val fieldNames = List(columnCount) { "열${it + 1}" }
    val data = List(rowCount) { row ->
        List(columnCount) { col ->
            if (Random.nextInt(100) < 5) { // 5% 확률로 긴 텍스트 생성
                "긴텍스트_${"A".repeat(Random.nextInt(10, 30))}"
            } else {
                "R${row + 1}C${col + 1}"
            }
        }
    }

    // 박스별 크기 계산
    val defaultWidth = 100.dp
    val defaultHeight = 40.dp

    val columnWidths = (0 until columnCount).map { col ->
        val maxChar = data.maxOf { it[col].length }
        if (maxChar > 5) defaultWidth + (maxChar - 5) * 5.dp else defaultWidth
    }

    val rowHeights = (0 until rowCount).map { row ->
        val maxLine = data[row].maxOf { ceil(it.length / 30.0).toInt() }
        if (maxLine > 1) defaultHeight + (maxLine - 1) * 20.dp else defaultHeight
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
        BarTop(fieldNames, columnWidths)
        Row {
            BarLeftIndex(rowCount, rowHeights)
            BoxTable(
                data = tableData.value,
                columnWidths = columnWidths, // 미리 계산된 값 사용
                rowHeights = rowHeights
            )
        }
        Spacer(Modifier.height(16.dp))
        PageControler(currentPage = currentPage)
    }
}

@Composable
fun BarTop(fieldNames: List<String>, columnWidths: List<Dp>) {
    Column {
        Row {
            Spacer(Modifier.width(60.dp))
            fieldNames.forEachIndexed { index, name ->
                Box(
                    modifier = Modifier
                        .width(columnWidths[index])
                        .height(40.dp) // TextField와 높이 맞춤
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
                    value = "",
                    onValueChange = {},
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
fun BarLeftIndex(rowCount: Int, rowHeights: List<Dp>) {
    Column {

        repeat(rowCount) { index ->
            val numStr = "${index + 1}"
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
    rowHeights: List<Dp>
) {
    Column {
        data.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    Box(
                        modifier = Modifier
                            .width(columnWidths[colIndex])
                            .height(rowHeights[rowIndex])
                            .border(1.dp, Color.Gray),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = cell,
                            modifier = Modifier.padding(start = 8.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


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




/*====페이징====*/
@Composable
fun DBTablePageControler(
    currentPage: MutableState<String>,
    maximumPage: MutableState<String>,
    isInputChanged: MutableState<Boolean>
) {
    val input = remember { mutableStateOf(currentPage.value) }
    // 페이지 조정 버튼 처리
    Row(modifier = Modifier.padding(8.dp)) {
        // 좌측 더블 버튼
        DBTableArrowButton(ArrowDirection.FIRST, currentPage, maximumPage)
        // 좌측 한 칸 버튼
        DBTableArrowButton(ArrowDirection.LEFT, currentPage, maximumPage)
        // 페이지 번호 입력 (직접 입력)
        BasicTextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier
                .width(50.dp)
                .height(45.dp)
                .background(Color.Gray)
                .padding(8.dp),
            textStyle = TextStyle(color = Color.Black)
        )
        // 페이지 표시
        Text("of ${maximumPage.value}", modifier = Modifier.padding(8.dp))
        // 우측 한 칸 버튼
        DBTableArrowButton(ArrowDirection.RIGHT, currentPage, maximumPage)
        // 우측 더블 버튼
        DBTableArrowButton(ArrowDirection.LAST, currentPage, maximumPage)
    }


    LaunchedEffect(currentPage.value) {
        input.value = currentPage.value
    }

    // 페이지가 바뀌면 `currentPage`에 맞게 변경된 페이지를 업데이트
    LaunchedEffect(input.value) {
        if (input.value.toIntOrNull() != null && input.value.toInt() in 1..maximumPage.value.toInt()) {
            currentPage.value = input.value
            isInputChanged.value = true
        }
    }
}

@Composable
fun DBTableArrowButton(
    direction: ArrowDirection,
    currentPage: MutableState<String>,
    maximumPage: MutableState<String>
) {
    val onClick: () -> Unit = when (direction) {
        ArrowDirection.LEFT -> {
            { if (currentPage.value.toInt() > 1) currentPage.value = (currentPage.value.toInt() - 1).toString() }
        }
        ArrowDirection.RIGHT -> {
            { if (currentPage.value.toInt() < maximumPage.value.toInt()) currentPage.value = (currentPage.value.toInt() + 1).toString() }
        }
        ArrowDirection.FIRST -> {
            { if (currentPage.value.toInt() > 1) currentPage.value = "1" }
        }
        ArrowDirection.LAST -> {
            { if (currentPage.value.toInt() < maximumPage.value.toInt()) currentPage.value = maximumPage.value.toInt().toString() }
        }
    }

    val icon = when (direction) {
        ArrowDirection.LEFT -> R.drawable.one_left
        ArrowDirection.RIGHT -> R.drawable.one_right
        ArrowDirection.FIRST -> R.drawable.double_left
        ArrowDirection.LAST -> R.drawable.double_right
    }

    Icon(
        painter = painterResource(icon),
        contentDescription = "",
        tint = TitleColor,
        modifier = Modifier.clickable { onClick() }
    )
}

enum class ArrowDirection {
    LEFT, RIGHT, FIRST, LAST
}

