package com.example.vept.ed.L4

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.vept.R
import com.example.vept.ed.L4L.VerticalDividers
import com.example.vept.ui.other.ArrowAndMenu
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.TableBackGroundColor
import com.example.vept.ui.theme.TextColor
import com.example.vept.ui.theme.TitleColor
import kotlinx.coroutines.delay


@Composable
fun EditTableListDesign(
    viewModel: EditTableListViewModel,
    navController: NavHostController
) {
    val itemName = viewModel.getItemName() ?: "이름 없음"
    val itemType = viewModel.getItemType() ?: "타입 없음"
    var dbTableData:  MutableState<MutableList<List<String>>> = remember { mutableStateOf(mutableListOf<List<String>>()) }

    //val dbTableSample = remember{ }
    val currentPage = remember { mutableStateOf("1") }
    val maximumPage = remember { mutableStateOf("7") } // 임의로 정한 maximum page, 나중에는 실제 DB 데이터 개수에 따른 페이지만큼 초기화 필요

    val liveData = viewModel.getTablePageData(itemName, currentPage.value.toInt())
    val liveDataState by liveData.observeAsState(initial = emptyList())

    val rowCountLive = viewModel.getRowCount(itemName)
    val rowCount by rowCountLive.observeAsState(initial = 0)

    val fieldNamesLive = viewModel.getFieldNames(itemName)
    val fieldNames by fieldNamesLive.observeAsState(initial = emptyList())

    LaunchedEffect(rowCount) {
        maximumPage.value = ((rowCount - 1) / 50 + 1).toString()
        dbTableData.value = liveDataState.toMutableList()
    }

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
            SelectDBTitle("$itemName")
            //SelectDBButton("Table",navController=navController, navDestination = "table")

            //<<===디버그용===!!//
            ReturnToMainButton(navController)
            //!!===디버그용===>>//

            DBTableTextField()
        }

        DBTableTemplate(
            data = dbTableData,
            fieldNames = fieldNames,
            currentPage = currentPage,
            maximumPage = maximumPage,
            viewModel = viewModel

        )
    }
}

@Composable
fun DBTableTextField(){

    val input = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .offset(
                x=-50.dp,
                y=-4.dp
            )
    ){
        Column(){
            Spacer(Modifier.height(30.dp))
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "",
                tint = TitleColor
            )
            Icon(
                painter = painterResource(R.drawable.textfiled_line),
                contentDescription = "",
                tint = TitleColor
            )
        }

        TextField(
            maxLines = 1,
            modifier = Modifier
                .offset(
                    x = 12.dp,
                    y = 15.dp
                )
                .width(200.dp)
                .height(50.dp),
            textStyle = TextStyle(
                fontSize = 14.sp
            ),
            value = input.value,
            onValueChange = {input.value = it},
            colors = TextFieldDefaults.colors(
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor,
                focusedIndicatorColor = TextColor,
                unfocusedIndicatorColor = TextColor,
                cursorColor = TextColor,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )
    }
}


// 데이터 넘버링 생성기
fun DBTableNumberGenerator(
    start: Int
) : MutableList<String> {
    val numberList:MutableList<String> = mutableListOf()
    numberList.add("")
    numberList.add("")
    for(i in start until start+50){
        numberList.add(i.toString())
    }
    return numberList
}


inline fun <T> LiveData<T>.observeOnce(
    lifecycleOwner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            onChanged(value)
            removeObserver(this)
        }
    })
}

fun loadDataForPage(
    pageStr: String,
    data: MutableState<MutableList<List<String>>>,
    viewModel: EditTableListViewModel,
    lifecycleOwner: LifecycleOwner
) {
    val page = pageStr.toIntOrNull() ?: 1
    val itemName = viewModel.getItemName()

    viewModel.getTablePageData(itemName, page).observeOnce(lifecycleOwner) { newData ->
        if (newData != null) {
            data.value = newData.toMutableList()
        }
    }
}


@Composable
fun DBTableTemplate(
    data: MutableState<MutableList<List<String>>>,
    fieldNames: List<String>,
    currentPage: MutableState<String>,
    maximumPage: MutableState<String>,
    viewModel: EditTableListViewModel
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    // 페이지 변경 시 데이터를 갱신하는 로직
    val rowStart = remember(currentPage.value) {
        val startNum = 1
        startNum + ((currentPage.value.toIntOrNull() ?: 1) - 1) * 50
    }

    val numList = remember(currentPage.value) { DBTableNumberGenerator(rowStart) }
    val lifecycleOwner = LocalLifecycleOwner.current


    // 페이지 변경 시 데이터를 로드
    LaunchedEffect(currentPage.value) {
        delay(275) //
        loadDataForPage(currentPage.value, data, viewModel, lifecycleOwner)
    }

    // 데이터를 현재 페이지에 맞게 업데이트
    val columnData = remember(data.value, fieldNames, currentPage.value) {
        val result = mutableListOf<List<String>>()
        if (data.value.isNotEmpty()) {
            val columnCount = data.value[0].size
            for (i in 0 until columnCount) {
                val column = mutableListOf<String>()
                column.add(fieldNames.getOrNull(i) ?: "") // 컬럼명 삽입
                column.addAll(data.value.map { it[i] })
                result.add(column)
            }
        }
        result
    }

    // 페이지 위치에 맞춰 넘버링 조정
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
            .background(TableBackGroundColor)
            .padding(horizontal = 30.dp)
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
    ) {
        Row {
            // 넘버링 출력
            DBTableNumber(numList)

            Box(
                modifier = Modifier.height((38.07f * (data.value.size + 1)).dp)
            ) {
                VerticalDividers(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }

            // 열 출력
            columnData.forEachIndexed { index, column ->
                if (index == 0) {
                    DBTableId(column)
                } else {
                    DBTableText(column)
                }

                // 세로선 출력
                if (index != columnData.lastIndex) {
                    Box(
                        modifier = Modifier.height((38.07f * (data.value.size + 1)).dp)
                    ) {
                        VerticalDividers(
                            modifier = Modifier.fillMaxHeight(),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        // 페이지 네비게이션 컨트롤러
        DBTablePageControler(
            currentPage = currentPage,
            maximumPage = maximumPage,
            isInputChanged = remember { mutableStateOf(false) } // 페이지 변경 상태
        )
    }
}





@Composable
fun DBTableNumber(
    data: List<String>
){
    Column(){
        val mostLong = data.maxOf{it.length}
        data.forEachIndexed { index, item ->
            // empty part
            // list of number는 "","",1,2,3,4,5,... 형태로 나아감
            // 즉 0번째, 1번째 인덱스는 빈 문자열
            if(index == 0 || index == 1){
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .clickable {
                            /* fun() */
                        }
                    ,
                    text = item,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = TitleColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier.width(mostLong.dp*15),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {
                // non-header part
                Column {
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                        ,
                        text = item,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            color = TitleColor,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 14.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(mostLong.dp*15),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun DBTableId(
    data: List<String>
){
    Column() {
        val mostLong = data.maxOf { it.length }
        data.forEachIndexed { index, item ->
            // empty part
            // list of number는 "","",1,2,3,4,5,... 형태로 나아감
            // 즉 0번째, 1번째 인덱스는 빈 문자열
            if (index == 0) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .clickable {
                            /* fun() */
                        },
                    text = item,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = TitleColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier.width(mostLong.dp * 12),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .clickable {
                            /* fun() */
                        },
                    text = "",
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = TitleColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier.width(mostLong.dp * 12),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {
                // non-header part
                Column {
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            ),
                        text = item,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            color = TextColor,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 14.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(mostLong.dp * 12),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DBTableText(
    data: List<String>
){
    val input = remember { mutableStateOf("") }
    val tmpData = remember{ mutableStateListOf<String>().apply { addAll(data) } }
    val interactionSource = remember{ MutableInteractionSource() }
    Log.e("debug","tmpData : ${tmpData.toString()}")
    Log.e("debug","data : ${data.toString()}")

    // textfiled 입려과 같은 recompose 발생시 data가 []가 되는 이상한 현상 발생
    // 따라서 data의 값이 변하고 data가 ![] 인 경우 => 새로운 data가 들어온 경우
    // 에만 tmpData의 값을 변경하여 문제 해결
    if(data != tmpData && !data.isEmpty()){
        tmpData.clear()
        tmpData.apply{addAll(data)}
    }

    Column() {
        val mostLong = tmpData.maxOf { it.length }



        tmpData.forEachIndexed { index, item ->
            // empty part
            // list of number는 "","",1,2,3,4,5,... 형태로 나아감
            // 즉 0번째, 1번째 인덱스는 빈 문자열
            if (index == 0) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .clickable {
                            /* fun() */
                        },
                    text = item,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = TitleColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width(mostLong.dp * 12),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                BasicTextField(
                    // 필터 입력 후 Action에 대해 정의
                    keyboardActions = KeyboardActions(
                        // 예시)
                        // 백엔드로 필터링 된 데이터 요청 함수
                        // callFilter2Backend(input)
                    ),
                    value = input.value,
                    onValueChange = {input.value = it},
                    modifier = Modifier
                        .width(mostLong.dp * 12)
                        .height(55.dp)
                        .width(50.dp)
                        .zIndex(1f)
                        .offset(
                            x = -4.dp,
                            y = -8.dp
                        ),
                    singleLine = true,
                    textStyle = TextStyle(

                        fontSize = 14.sp
                    ),

                    decorationBox = @Composable{ innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            placeholder = {
                                Text(
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
                            value = input.value,
                            interactionSource = interactionSource,
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

                HorizontalDivider(
                    modifier = Modifier
                        .width(mostLong.dp * 12)
                        .offset(
                            y = -18.5.dp
                        )
                    ,
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {
                // non-header part
                Column (
                    modifier =
                        Modifier.offset(
                            y = -18.5.dp
                        )
                ){
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            ),
                        text = item,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            color = TextColor,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 14.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(mostLong.dp * 12),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

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

