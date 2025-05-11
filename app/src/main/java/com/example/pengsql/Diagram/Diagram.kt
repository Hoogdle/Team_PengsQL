package com.example.pengsql.Diagram

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pengsql.DBTable.DBTableTextField
import com.example.pengsql.Others.ArrowAndMenu
import com.example.pengsql.SelectDB.SelectDBButton
import com.example.pengsql.SelectDB.SelectDBButtonPack
import com.example.pengsql.SelectDB.SelectDBTitle
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.TableBackGroundColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pengsql.R
import com.example.pengsql.ui.theme.ButtonColor
import com.example.pengsql.ui.theme.ButtonTextColor
import com.example.pengsql.ui.theme.TextColor
import com.example.pengsql.ui.theme.TitleColor


@Composable
fun Diagram(
    navController: NavController
){

    Column (
        Modifier
            .background(BackGroundColor)
            .padding(
                top = 25.dp
            )
    ) {

        ArrowAndMenu()
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SelectDBTitle("Table Name")
            DiagramButtonPack()

        }
        DiagramTemplate()
        DiagramTable(diagramSampleData)
    }
}

// 버튼 모음
@Composable
fun DiagramButtonPack(){
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
        SelectDBButton("버튼")
        SelectDBButton("버튼")
        SelectDBButton("버튼")
        SelectDBButton("버튼")
    }
}

// 'DB 테이블' 이 배치될 배경, Zoom in and out 기능 구현
@Composable
fun DiagramTemplate(){
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    var scale by remember { mutableStateOf(1f) }

    // 초기 Offset 기억
    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val minScale = 1f
    val maxScale = 4f

    val slowMovement = 0.5f


    Column (
        modifier = Modifier

            .fillMaxWidth()
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(35.dp, 35.dp, 0.dp, 0.dp))
            .background(TableBackGroundColor)
            .padding(
                start = 30.dp,
                end = 30.dp,
            )

            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // Update scale with the zoom
                    val newScale = scale * zoom
                    scale = newScale.coerceIn(minScale, maxScale)

                    // Calculate new offsets based on zoom and pan
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val offsetXChange = (centerX - offsetX) * (newScale / scale - 1)
                    val offsetYChange = (centerY - offsetY) * (newScale / scale - 1)

                    // Calculate min and max offsets
                    val maxOffsetX = (size.width / 2) * (scale - 1)
                    val minOffsetX = -maxOffsetX
                    val maxOffsetY = (size.height / 2) * (scale - 1)
                    val minOffsetY = -maxOffsetY

                    // Update offsets while ensuring they stay within bounds
                    if (scale * zoom <= maxScale) {
                        offsetX = (offsetX + pan.x * scale * slowMovement + offsetXChange)
                            .coerceIn(minOffsetX, maxOffsetX)
                        offsetY = (offsetY + pan.y * scale * slowMovement + offsetYChange)
                            .coerceIn(minOffsetY, maxOffsetY)
                    }

                    // Store initial offset on pan
                    if (pan != Offset(0f, 0f) && initialOffset == Offset(0f, 0f)) {
                        initialOffset = Offset(offsetX, offsetY)
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        // Reset scale and offset on double tap
                        if (scale != 1f) {
                            scale = 1f
                            offsetX = initialOffset.x
                            offsetY = initialOffset.y
                        } else {
                            scale = 2f
                        }
                    }
                )
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
    ){

            Row {
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
                DiagramTable(
                    diagramSampleData
                )
            }
            DiagramTable(
                diagramSampleData
            )
            DiagramTable(
                diagramSampleData
            )
            DiagramTable(
                diagramSampleData
            )
            DiagramTable(
                diagramSampleData
            )
        }



}


// DB 테이블
@Composable
fun DiagramTable(
    tableData: DiagramTableData
){
    var largestLength = tableData.title.length


    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(
                topStart = 5.dp,
                topEnd = 5.dp
            ))
            .border(
                width = 1.5.dp,
                color = ButtonColor
            )

    ){
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ButtonColor
                )
                .padding(
                    start = 5.dp
                )
                .height(30.dp)
                .offset(
                    y = 7.dp
                )
                ,
            text = tableData.title,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = ButtonTextColor,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 14.sp
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        for((index, entry) in tableData.contents.entries.withIndex()){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,

                modifier = Modifier
                    .border(
                        width = 0.5.dp,
                        color = Color.LightGray
                    )
                    .fillMaxWidth()
                    .background(Color.White)

                    .padding(
                        start = 5.dp,
                        end = 5.dp
                    )

            ){
                Text(
                    modifier = Modifier
                        .background(
                            color = Color.White
                        )
                        .height(30.dp)
                        .offset(
                            y = 7.dp
                        )
                    ,
                    text = entry.key,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(Modifier.width(20.dp))
                Text(
                    modifier = Modifier
                        .background(
                            color = Color.White
                        )
                        .height(30.dp)
                        .offset(
                            y = 7.dp
                        )
                    ,
                    text = entry.value,
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
        }
    }
}