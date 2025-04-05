package com.example.pengsql.SelectDB

import android.text.BoringLayout
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pengsql.Others.ArrowAndMenu
import com.example.pengsql.R
import com.example.pengsql.Table.TableButtonPack
import com.example.pengsql.Table.TableDropDown
import com.example.pengsql.Table.TableMarker
import com.example.pengsql.Table.TableTemplate
import com.example.pengsql.Table.TableText
import com.example.pengsql.Table.TableTitle
import com.example.pengsql.Table.VerticalDividers
import com.example.pengsql.Table.dropDownSample
import com.example.pengsql.Table.tableDataSamples
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.ButtonColor
import com.example.pengsql.ui.theme.ButtonTextColor
import com.example.pengsql.ui.theme.TableBackGroundColor
import com.example.pengsql.ui.theme.TextColor
import com.example.pengsql.ui.theme.TitleColor

@Composable
fun SelectDB(){
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
            SelectDBTitle("DB Name")
            SelectDBButtonPack()
        }
        SelectDBTemplate()
    }
}

@Composable
fun SelectDBTitle(
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
fun SelectDBTemplate(){
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
    ){
        Column {
            SelectDBHeader(
                headName = "테이블",
                parentList = tableChild,
                childList = tableChildOfChild
            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            SelectDBHeader(
                headName = "인덱스",
                parentList = indexChild,
                childList = indexChildOfChild
            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            SelectDBHeader(
                headName = "뷰",
                parentList = viewChild,
                childList = viewChildOfChild
            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            SelectDBHeader(
                headName = "트리거",
                parentList = triggerChild,
                childList = triggerChildOfChild
            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
        }


    }
}

@Composable
fun SelectDBHeader(
    headName: String,
    parentList: List<String>,
    childList: List<String>
) {
    val isOpened = remember{ mutableStateOf(false) }

    if (isOpened.value) {
        Column(){
            Spacer(Modifier.height(30.dp))
            Row {
                Spacer(Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .offset(
                            x= 4.dp,
                            y=1.dp
                        )
                        .clickable {
                            isOpened.value = false
                        },
                    painter = painterResource(R.drawable.small_down),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(10.dp))
                Text(

                    text = headName + " (${parentList.size})",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 16.sp
                    )
                )
            }
            parentList.forEachIndexed { index, parent ->
                SelectDBParent(
                    childList = childList,
                    parentName = parent,
                    tableIsOpened = isOpened
                )
            }
        }
    } else {
        Column (){
            Spacer(Modifier.height(30.dp))
            Row {
                Spacer(Modifier.width(20.dp))
                Icon(
                    modifier = Modifier
                        .offset(
                            x= 1.dp,
                            y = 3.dp
                        )
                        .clickable {
                            isOpened.value = true
                        },
                    painter = painterResource(R.drawable.right_arrow),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(10.dp))
                Text(

                    text = headName + " (${childList.size})",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun SelectDBParent(
    tableIsOpened: MutableState<Boolean>,
    parentName: String,
    childList: List<String>
){
    val isOpened = remember { mutableStateOf(false) }
    if (tableIsOpened.value && isOpened.value) {
        Column {
            Spacer(Modifier.height(10.dp))
            Row {
                Spacer(Modifier.width(34.dp))
                Icon(
                    modifier = Modifier
                        .offset(
                            x= 1.dp,
                            y=1.dp
                        )
                        .clickable {
                            isOpened.value = false
                        },
                    painter = painterResource(R.drawable.small_down),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(2.5f.dp))
                Text(
                    text = parentName + " (${childList.size})",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    )
                )
            }
            childList.forEachIndexed { index, child ->
                Row {
                    Spacer(Modifier.width(60.dp))
                    Text(
                        text = child,
                        style = TextStyle(
                            color = TextColor,
                            fontFamily = FontFamily(Font(R.font.roboto_thin)),
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    } else {
        Column{
            Spacer(Modifier.height(10.dp))
            Row {
                Spacer(Modifier.width(40.dp))
                Icon(
                    modifier = Modifier
                        .offset(
                            x= 1.dp,
                            y = 3.dp
                        )
                        .clickable {
                            isOpened.value = true
                        },
                    painter = painterResource(R.drawable.right_arrow),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = parentName + " (${childList.size})",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}




@Composable
fun SelectDBButton(
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
fun SelectDBButtonPack(){
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
        SelectDBButton("테이블 생성")
        SelectDBButton("인덱스 생성")
        SelectDBButton("저장")
        SelectDBButton("취소")
    }
}
