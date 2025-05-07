package com.example.pengsql.Home

import android.graphics.drawable.RotateDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.evaluateY
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.pengsql.Others.OnlyMenu
import com.example.pengsql.R
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.ButtonColor
import com.example.pengsql.ui.theme.ButtonTextColor
import com.example.pengsql.ui.theme.HomeDBListColor
import com.example.pengsql.ui.theme.HomeDownLoadButton
import com.example.pengsql.ui.theme.TextColor

val sampleDB = listOf("apple","banana","kiwi","tomato","orange","mango","corn","lemon","watermelon","water","kimchi","omg")
@Composable
fun Home(){
    Column (
        Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(
                top = 25.dp
            )
    ){
        OnlyMenu()
        Spacer(Modifier.height(5.dp))
        Image(
            modifier = Modifier
                .height(50.dp)
                .offset(
                    x= 100.dp,
                ),
            painter = painterResource(R.drawable.home_title),
            contentDescription = ""
        )
        Row(){
            HomeDBList(sampleDB)
            Spacer(Modifier.width(140.dp))
            Column(
                modifier = Modifier
                    .offset(
                        y = -2.dp
                    )
            ){
                HomeButton(
                    contents = "다이어그램 생성",
                    onClick = {}
                )
                Spacer(Modifier.height(45.dp))
                HomeButton(
                    contents = "DB 생성",
                    onClick = {}
                )
                Spacer(Modifier.height(45.dp))
                Row {
                    HomeButton(
                        contents = "모듈 관리자",
                        onClick = {}
                    )

                    Spacer(Modifier.width(25.dp))

                    HomeDownButton(
                        onClick = {}
                    )
                }
            }
        }

    }
}

@Composable
fun HomeDBList(
    dbList: List<String> // 임시로 DB 데이터 설정(샘플)
){
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .offset(
                x = 100.dp
            )
            .shadow(elevation = 25.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(HomeDBListColor)
            .width(350.dp)
            .height(230.dp)
            .verticalScroll(scrollState)
    ){
        dbList.forEachIndexed { index, item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    modifier = Modifier
                        .padding(
                            10.dp
                        )
                        .fillMaxWidth()
                        .weight(8f)
                        .height(30.dp)
                        .offset(

                        )
                    ,
                    text = item,
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 18.sp
                    )
                )

                Button(
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .size(20.dp)
                        .offset(
                            y = 10.dp
                        ),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = "",
                        tint = Color(71,144,247)
                    )
                }

                Spacer(Modifier.width(0.dp))

                Button(
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .size(20.dp)
                        .offset(
                            x = -8.dp,
                            y = 10.dp
                        ),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.new_del),
                        contentDescription = "",
                        tint = Color(223,34,37)

                    )
                }

            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(
                        y = -5.dp
                    )
            )
        }
    }
}

@Composable
fun HomeButton(
    contents: String,
    onClick : ()->Unit
){
    Button(
        colors = ButtonColors(
            containerColor = ButtonColor,
            contentColor = ButtonTextColor,
            disabledContainerColor = ButtonColor,
            disabledContentColor = ButtonTextColor
        ),
        shape = RectangleShape,
        onClick = {
            onClick()
        },
        modifier = Modifier
            .shadow(
                elevation = 25.dp
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(
                    y = 5.dp
                )
                .width(165.dp)
                .height(30.dp)
            ,
            text = contents,
            style = TextStyle(
                color = ButtonTextColor,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun HomeDownButton(
    onClick: () -> Unit
){
    Button(
        contentPadding = PaddingValues(10.dp),
        shape = RectangleShape,
        colors = ButtonColors(
            contentColor = ButtonTextColor,
            containerColor = HomeDownLoadButton,
            disabledContentColor = ButtonTextColor,
            disabledContainerColor = HomeDownLoadButton
        ),
        modifier = Modifier
            .shadow(
                elevation = 25.dp,
            )
            .clip(RoundedCornerShape(10.dp))
            .width(50.dp)
            .height(50.dp),
        onClick = {
            onClick()
        }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.home_download),
            contentDescription = ""
        )
    }
}

