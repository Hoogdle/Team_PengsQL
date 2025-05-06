package com.example.pengsql.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pengsql.Others.OnlyMenu
import com.example.pengsql.R
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.HomeDBListColor
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
        HomeDBList(sampleDB)

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
            .shadow(elevation = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(HomeDBListColor)
            .width(350.dp)
            .height(230.dp)
            .verticalScroll(scrollState)
    ){
        dbList.forEachIndexed { index, item ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .offset(
                        x = 25.dp,
                        y = 8.dp
                    )
                    .clickable { 
                        // 터치시 행동 정의
                    }
                ,
                text = (index+1).toString() +"  "+ item,
                style = TextStyle(
                    color = TextColor,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 16.sp
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun HomeButton(
    contents: String,
    onClick : ()->Unit
){

}