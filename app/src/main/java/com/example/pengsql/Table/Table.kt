package com.example.pengsql.Table


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.pengsql.ui.theme.TableBackGroundColor
import com.example.pengsql.ui.theme.TextColor
import com.example.pengsql.ui.theme.TitleColor

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
        TableTitle("Table Name")
        Spacer(modifier = Modifier.height(8.dp))
        TableTemplate(tableDataSamples)
    }
}


@Composable
fun TableTitle(
    text: String
){

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
                TableColumn(tmpOneColumn)

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

@Composable
fun TableColumn(
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


@Composable
fun TableButton(){

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
