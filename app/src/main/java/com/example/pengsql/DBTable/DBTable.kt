package com.example.pengsql.DBTable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
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

@Composable
fun DBTable(
    navController: NavController
){
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
        DBTableTemplate(dbTableSample)
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
        for (i in 0..8){
            data.forEachIndexed { index, item ->
                tmpColumn.add(item[i])
            }
        }
        tmpColumn.forEachIndexed { index, item ->
            tmpOneColumn.add(item)
            if( (index+1)%data.size == 0){
                DBTableText(tmpOneColumn)

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
fun DBTableNumber(){

}

@Composable
fun DBTableId(
    dataList: List<String>
){

}
@Composable
fun DBTableText(
    dataList: List<String>
){

}


// 데이터 넘버링 생성기
fun DBTableNumberGenerator(
    start: Int
) : MutableList<Int> {
    val numberList:MutableList<Int> = mutableListOf()
    for(i in start until start+50){
        numberList.add(i)
    }
    return numberList
}

