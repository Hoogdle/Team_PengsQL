package com.example.pengsql.Table


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.EmptyPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pengsql.Others.ArrowAndMenu
import com.example.pengsql.R
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.ButtonColor
import com.example.pengsql.ui.theme.ButtonTextColor
import com.example.pengsql.ui.theme.EmptyBox
import com.example.pengsql.ui.theme.GreenBox
import com.example.pengsql.ui.theme.TableBackGroundColor
import com.example.pengsql.ui.theme.TextColor
import com.example.pengsql.ui.theme.TitleColor


// =========================================================READ ME=========================================================
// 구현 난이도가 꽤 있어서 주석을 충분히 달지는 못했습니다.
// *** 중요 ***
// 하민님과 상의 했던 대로 테이블 데이터가 아래와 같이 주어진다고 가정하고 개발하였습니다.

// ex)
// givenTableData = [["Filed Name", "Data Type", "NN", "AI", "U"], ["a","b","c","d","e",...],["a","b","c","d","e",...]]
// 즉, List<List<String>> 형태로 주어진다고 가정하였고, 만약에 다른 형태로 주어진다면 백엔드 쪽에서 이를 위와 같은 형태로 변환하는 것이 좋을 것 같습니다..(프론트에서 또 바꾸기에는 쪼매 힘들 것 같네요..)

// 유저가 무언가를 선택했을 때 화면이 바꾸는 것을 구현하려면(ex. 필터 선택시 테이블 데이터 변경) remember라는 변수를 통해서 아래의 코드를 약간 변경해야 합니다.
// 이 사항은 추후에 상의하는게 좋을 거 같습니다.
// =========================================================READ ME=========================================================

@Composable
fun Table(){
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
            TableTitle("Table Name")
            TableButtonPack()
        }
        TableTemplate(tableDataSamples)
    }
}


@Composable
fun TableTitle(
    text: String
){
    Column {
        Text(
            modifier = Modifier
                .padding(start = 45.dp),
            text = text,
            style = TextStyle(
                color = TitleColor,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 28.sp
            )
        )
    }
}



@Composable
fun TableTemplate(
    data: List<List<String>>
){
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val tmpColumn: MutableList<String> = mutableListOf()
    val tmpOneColumn: MutableList<String> = mutableListOf()

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
    ){
        for (i in 0..8){
            data.forEachIndexed { index, item ->
                tmpColumn.add(item[i])
            }
        }
        tmpColumn.forEachIndexed { index, item ->
            tmpOneColumn.add(item)
            Log.e("debug","${data.size}")
            if( (index+1)%data.size == 0){

                // 텍스트 열, Selector 열 설정
                if((index+1)/data.size == 2 ||  (index+1)/data.size ==8){
                    TableDropDown(tmpOneColumn, dropDownSample)
                } else if ((index+1)/data.size in 3..6) {
                    TableMarker(tmpOneColumn)
                } else {
                    TableText(tmpOneColumn)
                }

                // 세로선
                Box(
                    modifier = Modifier.height((37*data.size).dp)
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

// 테이블 중 텍스트 열
@Composable
fun TableText(
    data: List<String>
){
    Column(){

        val mostLong = data.maxOf{it.length}
        data.forEachIndexed { index, item ->
            if(index == 0){
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
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier.width(mostLong.dp*12),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {
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
                            color = TextColor,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 14.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(mostLong.dp*12),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }

}

// 테이블 중 Selector 열
@Composable
fun TableDropDown(
    data: List<String>,
    dropDownDataList: List<String>
){
    val isDropDownExpanded = remember {
        val tmp = mutableStateListOf<Boolean>()
        for(i in 0..data.size-1){
            tmp.add(false)
        }
        tmp
    }

    val itemPosition = remember {mutableStateOf(0)}
    val tmpData = remember{ mutableStateListOf<String>().apply { addAll(data) } }

    Column(){

        Log.e("debug","${isDropDownExpanded.toString()}")
        Log.e("debug","start")
        Log.e("debug","${tmpData.toString()}") // after recompose, all the data of data(list) has gone...
        val mostLong = tmpData.maxOf{it.length}
        Log.e("debug","end")
        tmpData.forEachIndexed { index, item ->
            if(index == 0){
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
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier.width(mostLong.dp*12),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {

                Column {
                    Row(){
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                painter = painterResource(R.drawable.small_down),
                                contentDescription = "",
                                tint = TextColor
                            )
                        }
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 2.dp,
                                    top = 10.dp,
                                    bottom = 10.dp
                                )
                                .clickable {
                                    isDropDownExpanded[index] = true
                                    /* fun() */
                                }
                            ,
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
                    }
                    HorizontalDivider(
                        modifier = Modifier.width(mostLong.dp*12),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )

                    DropdownMenu(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(TableBackGroundColor)
                            .border(
                                width = 1.dp,
                                color = TextColor,
                                shape = RectangleShape
                            ),
                        expanded = isDropDownExpanded[index],
                        onDismissRequest = {
                            isDropDownExpanded[index] = false
                        }
                    ) {
                        dropDownDataList.forEachIndexed { dropIndex, dropItems ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = dropItems,
                                        style = TextStyle(
                                            color = TextColor,
                                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                            fontSize = 14.sp
                                        ),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    isDropDownExpanded[index] = false
                                    itemPosition.value = dropIndex
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


// 테이블 중 Makrer 열
@Composable
fun TableMarker(
    data: List<String>

){
    Column(){
        val mostLong = data.maxOf{it.length}
        data.forEachIndexed { index, item ->
            if(index == 0){
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
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                HorizontalDivider(
                    modifier = Modifier.width(mostLong.dp*12),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {
                Column {
                    if(item.toBoolean()){
                        Icon(
                            modifier = Modifier
                                .width(36.dp)
                                .height(36.dp)
                                .padding(7.dp),
                            painter = painterResource(R.drawable.green_box),
                            contentDescription = "",
                            tint = GreenBox
                        )
                    }else{
                        Icon(
                            modifier = Modifier
                                .width(36.dp)
                                .height(36.dp)
                                .padding(7.dp),
                            painter = painterResource(R.drawable.empty_box),
                            contentDescription = "",
                            tint = EmptyBox
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.width(mostLong.dp*12),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun TableButton(
    text: String
){
    Button(
        contentPadding = PaddingValues(7.dp),
        modifier = Modifier
            .height(30.dp)
            .padding(
                start = 1.dp,
                end = 1.dp
            )
        ,
        onClick = {},
        colors = ButtonColors(
            contentColor = ButtonTextColor,
            containerColor = ButtonColor,
            disabledContainerColor = ButtonColor,
            disabledContentColor = ButtonTextColor
        ),
        shape = RectangleShape
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            style = TextStyle(
                color = ButtonTextColor,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 15.sp
            )
        )
    }
}


@Composable
fun TableButtonPack(){
    Row (
        Modifier
            .padding(
            end = 15.dp
        )
            .offset(
                x = -50.dp,
                y=3.dp
            )
            .clip(RoundedCornerShape(8.dp,8.dp,0.dp,0.dp))
    ){
        TableButton("추가")
        TableButton("삭제")
        TableButton("제약조건")
        TableButton("SQL")
    }
}

@Composable
fun TableShadow(alpha: Float = 0.1f, height: Dp = 8.dp) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = alpha),
                    Color.Transparent,
                )
            )
        )
    )
}

@Composable
fun VerticalDividers(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) {
    Row() {
        Canvas(
            modifier
                .fillMaxHeight()
                .width(thickness)
        ) {
            drawLine(
                color = color,
                strokeWidth = thickness.toPx(),
                start = Offset(thickness.toPx() / 2, 0f),
                end = Offset(thickness.toPx() / 2, size.height),
            )
        }
    }
}

@Composable
fun HorizontalDividers(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) {
    Canvas(
        modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            end = Offset(thickness.toPx() / 2, 0f),
            start = Offset(thickness.toPx() / 2, size.height),
        )
    }
}
