//package com.example.pengsql.Table
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.GridItemSpan
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.itemsIndexed
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.DividerDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.vept.ui.other.ArrowAndMenu
//import com.example.pengsql.R
//import com.example.pengsql.ui.theme.BackGroundColor
//import com.example.pengsql.ui.theme.TableBackGroundColor
//import com.example.pengsql.ui.theme.TextColor
//import com.example.pengsql.ui.theme.TitleColor
//
//@Composable
//fun Table(){
//    Column (
//        Modifier
//            .background(BackGroundColor)
//            .padding(
//                top = 25.dp
//            )
//    ){
//        ArrowAndMenu()
//        Spacer(modifier = Modifier.height(15.dp))
//        TableTitle("Table Name")
//        Spacer(modifier = Modifier.height(8.dp))
//        TableFirstRow()
//        TableOtherAllRow(tableDataSamples)
//    }
//}
//
//@Composable
//fun TableTitle(
//    text: String
//){
//
//    Text(
//        modifier = Modifier
//            .padding(start = 45.dp),
//        text = text,
//        style = TextStyle(
//            color = TitleColor,
//            fontFamily = FontFamily(Font(R.font.roboto_bold)),
//            fontSize = 28.sp
//        )
//    )
//}
//
//
//// lazy는 기본적으로 무한한 height를 가정하므로, lazy안에 lazy를 넣으면 에러 발생..
//// => lazy에 모두 다 넣어버리기 => 스파게티 코드...
//
//// 1. List<List<String>> => List<String>
//// 2. put the result of '1' to LazyGrid
//// 3. 변수 하나를 두고 9개가 이후에는 박스 텍스트 대신 박스가 출려되도록 카운트
//@Composable
//fun TableFirstRow(){
//    val verticalScrollState = rememberScrollState()
//    val horizontalScrollState = rememberScrollState()
//    var showLine: Boolean = true
//
//    // 테이블의 첫 번째 행의 정보 가져오기
//    val columnsOfTableData = columnsTableData
//
//
//    Column (modifier = Modifier
//        .fillMaxWidth()
//        .fillMaxHeight()
//        .background(BackGroundColor)
//        .clip(shape = RoundedCornerShape(35.dp, 35.dp, 0.dp, 0.dp))
//        .background(TableBackGroundColor)
//        .padding(
//            start = 30.dp,
//            end = 30.dp,
//        )){
//        Box(){
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(38),
//                contentPadding = PaddingValues(5.dp)
//            ) {
//                itemsIndexed(
//                    columnsOfTableData,
//                    span = { index, item ->
//                        val spanCount = if(index in 2..5) 2 else 6
//                        GridItemSpan(spanCount)
//                    }
//                ){ index, item ->
//                    if(index == columnsOfTableData.size-1){
//                        showLine = false
//                    }
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ){
//                        Text(
//                            modifier = Modifier
//                                .padding(
//                                    start = 10.dp,
//                                    top = 10.dp,
//                                    bottom = 10.dp
//                                )
//                            ,
//                            text = item,
//                            textAlign = TextAlign.Start,
//                            style = TextStyle(
//                                color = TextColor,
//                                fontFamily = FontFamily(Font(R.font.roboto_bold)),
//                                fontSize = 14.sp
//                            ),
//                            overflow = TextOverflow.Ellipsis,
//                            maxLines = 1
//                        )
//
//                        if(showLine){
//                            Box(
//                                modifier = Modifier.height(40.dp)
//                            ){
//                                com.example.pengsql.Table.VerticalDivider(
//                                    thickness = 1.dp,
//                                    color = Color.LightGray
//                                )
//                            }
//                        }
//
//                    }
//                }
//            }
//            Column(
//                modifier = Modifier.fillMaxSize()
//            ){
//                Spacer(modifier = Modifier.height(45.dp))
//                HorizontalDivider(
//                    color = Color.LightGray
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun TableOtherOneRow(
//    data: List<String>
//){
//    val verticalScrollState = rememberScrollState()
//    val horizontalScrollState = rememberScrollState()
//    var showLine: Boolean = true
//
//    // 테이블의 첫 번째 행의 정보 가져오기
//    val data = data
//    Column (modifier = Modifier
//        .fillMaxWidth()
//        .fillMaxHeight()
//        .background(BackGroundColor)
//        .clip(shape = RoundedCornerShape(35.dp, 35.dp, 0.dp, 0.dp))
//        .background(TableBackGroundColor)
//        .padding(
//            start = 30.dp,
//            end = 30.dp,
//        )){
//        Box(){
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(38),
//                contentPadding = PaddingValues(5.dp)
//            ) {
//                itemsIndexed(
//                    data,
//                    span = { index, item ->
//                        val spanCount = if(index in 2..5) 2 else 6
//                        GridItemSpan(spanCount)
//                    }
//                ){ index, item ->
//                    if(index == item.length-1){
//                        showLine = false
//                    }
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ){
//                        Text(
//                            modifier = Modifier
//                                .padding(
//                                    start = 10.dp,
//                                    top = 10.dp,
//                                    bottom = 10.dp
//                                )
//                            ,
//                            text = item,
//                            textAlign = TextAlign.Start,
//                            style = TextStyle(
//                                color = TextColor,
//                                fontFamily = FontFamily(Font(R.font.roboto_bold)),
//                                fontSize = 14.sp
//                            ),
//                            overflow = TextOverflow.Ellipsis,
//                            maxLines = 1
//                        )
//
//                        if(showLine){
//                            Box(
//                                modifier = Modifier.height(40.dp)
//                            ){
//                                com.example.pengsql.Table.VerticalDivider(
//                                    thickness = 1.dp,
//                                    color = Color.LightGray
//                                )
//                            }
//                        }
//
//                    }
//                }
//            }
//            Column(
//                modifier = Modifier.fillMaxSize()
//            ){
//                Spacer(modifier = Modifier.height(45.dp))
//                HorizontalDivider(
//                    color = Color.LightGray
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun TableOtherAllRow(
//    data: List<List<String>>
//){
//    Column(){
//        data.forEach {
//            TableOtherOneRow(it)
//        }
//    }
//}
//@Composable
//fun TableButton(){
//
//}
//
//@Composable
//fun TableShadow(alpha: Float = 0.1f, height: Dp = 8.dp) {
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .height(height)
//        .background(
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    Color.Black.copy(alpha = alpha),
//                    Color.Transparent,
//                )
//            )
//        )
//    )
//}
//
//@Composable
//fun VerticalDivider(
//    modifier: Modifier = Modifier,
//    thickness: Dp = DividerDefaults.Thickness,
//    color: Color = DividerDefaults.color,
//) = Canvas(modifier
//    .fillMaxHeight()
//    .width(thickness)) {
//    drawLine(
//        color = color,
//        strokeWidth = thickness.toPx(),
//        start = Offset(thickness.toPx() / 2, 0f),
//        end = Offset(thickness.toPx() / 2, size.height),
//    )
//}