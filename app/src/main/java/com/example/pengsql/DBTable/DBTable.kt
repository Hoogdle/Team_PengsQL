package com.example.pengsql.DBTable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.pengsql.Others.ArrowAndMenu
import com.example.pengsql.R
import com.example.pengsql.SelectDB.SelectDBButton
import com.example.pengsql.SelectDB.SelectDBButtonPack
import com.example.pengsql.SelectDB.SelectDBHeader
import com.example.pengsql.SelectDB.SelectDBTemplate
import com.example.pengsql.SelectDB.SelectDBTitle
import com.example.pengsql.SelectDB.indexChild
import com.example.pengsql.SelectDB.indexChildOfChild
import com.example.pengsql.SelectDB.tableChild
import com.example.pengsql.SelectDB.tableChildOfChild
import com.example.pengsql.SelectDB.triggerChild
import com.example.pengsql.SelectDB.triggerChildOfChild
import com.example.pengsql.SelectDB.viewChild
import com.example.pengsql.SelectDB.viewChildOfChild
import com.example.pengsql.Table.TableDropDown
import com.example.pengsql.Table.TableMarker
import com.example.pengsql.Table.TableText
import com.example.pengsql.Table.VerticalDividers
import com.example.pengsql.Table.dropDownSample
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.TableBackGroundColor
import com.example.pengsql.ui.theme.TextColor
import com.example.pengsql.ui.theme.TitleColor
import kotlin.math.sin

@Composable
fun DBTable(
    navController: NavController
){
    val dbTableSample = remember{ mutableStateOf(dbTableSample1)}
    val currentPage = remember { mutableStateOf(1) }
    val maximumPage = remember { mutableStateOf(7) } // 임의로 정한 maximum page, 나중에는 실제 DB 데이터 개수에 따른 페이지만큼 초기화 필요

    Column (
        Modifier
            .background(BackGroundColor)
            .padding(
                top = 25.dp
            )
    ){

        ArrowAndMenu()
        Spacer(modifier = Modifier.height(15.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            SelectDBTitle("Table Name")
            SelectDBButton("Table",navController=navController, navDestination = "table")
            DBTableTextField()

        }

        DBTableTemplate(dbTableSample.value)
    }
    Box(
        modifier = Modifier
            .offset(
                x = 250.dp,
                y = 350.dp
            )
    ){
        DBTableArrowPack(
            currentPage = currentPage,
            maximumPage = maximumPage,
            dbTableSample = dbTableSample
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


@Composable
fun DBTableTemplate(
    data: List<List<String>>
){
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val tmpColumn: MutableList<String> = mutableListOf()
    val tmpOneColumn: MutableList<String> = mutableListOf()
    val startNum = remember { mutableStateOf(1) }
    lateinit var numList: MutableList<String>

    Row (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(BackGroundColor)
        .clip(shape = RoundedCornerShape(35.dp, 35.dp, 0.dp, 0.dp))
        .background(TableBackGroundColor)
        .padding(
            start = 30.dp,
            end = 30.dp,
        )
        .verticalScroll(verticalScrollState)
        .horizontalScroll(horizontalScrollState)
    ) {
        // 주어진 header에 따라 나눠서 담기
        // 행 기준 데이터를 열 기준으로 변환(화면에 출력할 때 밸런스를 맞추기 위해서 열 기준 출력이 필수적)
        for (i in 0 until data[0].size){
            data.forEachIndexed { index, item ->
                tmpColumn.add(item[i])
            }
        }

        numList = DBTableNumberGenerator(startNum.value)
        DBTableNumber(numList)
        // 세로선
        Box(
            modifier = Modifier.height((37.33f*numList.size).dp)
        ){
            VerticalDividers(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
        
        // 변환된 열 기준 데이터로 화면에 뿌리기
        tmpColumn.forEachIndexed { index, item ->
            tmpOneColumn.add(item)

            // 하나의 열 씩 출력 하도록 modular 연산 수행.
            if( (index+1)%data.size == 0){
                if((index+1)/data.size == 1){
                    DBTableId(tmpOneColumn)
                } else {
                    DBTableText(tmpOneColumn)
                }

                // 세로선
                Box(
                    modifier = Modifier.height((38.07f*data.size).dp)
                ){
                    // 마지막 테이블 세로선 삭제
                    if(index+1 != tmpColumn.size){
                        VerticalDividers(
                            modifier = Modifier.fillMaxHeight(),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }

                tmpOneColumn.clear()
            }
        }
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
                            y= -8.dp
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
fun DBTableOneLeft(
    currentPage: MutableState<Int>,
    maximumPage: MutableState<Int>,
    dbTableSample: MutableState<MutableList<List<String>>>
){
    Icon(
        painter = painterResource(R.drawable.one_left),
        contentDescription = "",
        tint = TitleColor,
        modifier = Modifier.clickable {
            if (currentPage.value != 1){
                // 백엔드에 이전 50번째의 데이터 요청
                dbTableSample.value = dbTableSample2
            }
        }
    )
}

// 이거를 Template안으로
// Template에서 마지막 줄 제거하기
@Composable
fun DBTableArrowPack(
    currentPage: MutableState<Int>,
    maximumPage: MutableState<Int>,
    dbTableSample: MutableState<MutableList<List<String>>>
){
    Row {

        DBTableDoubleLeft(
                currentPage = currentPage,
        maximumPage = maximumPage,
        dbTableSample = dbTableSample
        )
        DBTableOneLeft(
            currentPage = currentPage,
            maximumPage = maximumPage,
            dbTableSample = dbTableSample
        )
        //
        DBTableOneRight(
            currentPage = currentPage,
            maximumPage = maximumPage,
            dbTableSample = dbTableSample
        )
        DBTableDoubleRight(
            currentPage = currentPage,
            maximumPage = maximumPage,
            dbTableSample = dbTableSample
        )
    }
}

@Composable
fun DBTableDoubleLeft(
    currentPage: MutableState<Int>,
    maximumPage: MutableState<Int>,
    dbTableSample: MutableState<MutableList<List<String>>>
){
    Icon(
        painter = painterResource(R.drawable.double_left),
        contentDescription = "",
        tint = TitleColor,
        modifier = Modifier
            .clickable {
                if (currentPage.value != 1){
                    // 백엔드에 이전 50번째의 데이터 요청
                    dbTableSample.value = dbTableSample2
                    currentPage.value += 1
                }
            }
    )
}

@Composable
fun DBTableOneRight(
    currentPage: MutableState<Int>,
    maximumPage: MutableState<Int>,
    dbTableSample: MutableState<MutableList<List<String>>>
){
    Icon(
        painter = painterResource(R.drawable.one_right),
        contentDescription = "",
        tint = TitleColor,
        modifier = Modifier
            .clickable {
            if (currentPage.value != 1){
                // 백엔드에 이전 50번째의 데이터 요청
                dbTableSample.value = dbTableSample2
                currentPage.value += 1
            }
        }
    )
}

@Composable
fun DBTableDoubleRight(
    currentPage: MutableState<Int>,
    maximumPage: MutableState<Int>,
    dbTableSample: MutableState<MutableList<List<String>>>
){
    Icon(
        painter = painterResource(R.drawable.double_right),
        contentDescription = "",
        tint = TitleColor,
        modifier = Modifier
            .clickable {
                if (currentPage.value != 1){
                    // 백엔드에 이전 50번째의 데이터 요청
                    dbTableSample.value = dbTableSample2
                    currentPage.value += 1
                }
            }
    )
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



