package com.example.vept.ed.L4

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import com.example.vept.R
import com.example.vept.ui.other.ArrowAndMenuWithTitle
import com.example.vept.ui.theme.AiBackGround
import com.example.vept.ui.theme.AiTextBox
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.ButtonColor
import com.example.vept.ui.theme.HomeDBListColor
import com.example.vept.ui.theme.TableBackGroundColor
import com.example.vept.ui.theme.TextColor
import com.example.vept.ui.theme.UserTextBox

@Composable
fun EditMCPsDesign(
    navController: NavHostController
){
    // 짝수 => ai, 홀수 => 유저 (첫 시작은 ai 부터)
    val count = remember { mutableStateOf(0) }
//    val storedInfo = remember { mutableStateListOf("안녕하세요. PengSQL AI assistant 입니다. 무엇을 도와드릴까요?","피카츄랑 리자몽이랑 싸우면 과연 누가 이길까? 물론 피카츄가 몸집은 리장몽에 비해 매우 작지만..", "저는 리자몽이 이길 것이라 생각해요. 리자몽은 불 타입이고 피카츄는 번개 타입인데....", "아니 그래도 피카츄가 이길 수도 있지 않을까? 리자몽은 몸이 크니깐 둔할 거고, 이런 약점을 민첩한 피카츄가 잘 이용한다면...","아니 그래도 피카츄가 이길 수도 있지 않을까? 리자몽은 몸이 크니깐 둔할 거고, 이런 약점을 민첩한 피카츄가 잘 이용한다면...","아니 그래도 피카츄가 이길 수도 있지 않을까? 리자몽은 몸이 크니깐 둔할 거고, 이런 약점을 민첩한 피카츄가 잘 이용한다면...","아니 그래도 피카츄가 이길 수도 있지 않을까? 리자몽은 몸이 크니깐 둔할 거고, 이런 약점을 민첩한 피카츄가 잘 이용한다면...","아니 그래도 피카츄가 이길 수도 있지 않을까? 리자몽은 몸이 크니깐 둔할 거고, 이런 약점을 민첩한 피카츄가 잘 이용한다면...","아니 그래도 피카츄가 이길 수도 있지 않을까? 리자몽은 몸이 크니깐 둔할 거고, 이런 약점을 민첩한 피카츄가 잘 이용한다면...") }

    val storedInfo = remember { mutableStateListOf("안녕하세요. PengSQL AI assistant 입니다. 무엇을 도와드릴까요?","피카츄랑 리자몽이랑 싸우면 과연 누가 이길까? 물론 피카츄가 몸집은 리장몽에 비해 매우 작지만..") }
    Log.e("zxc",storedInfo.size.toString())


    Scaffold(
        bottomBar = {
            EditMCPsTextField(
                count = count,
                storedInfo = storedInfo
            )
        }
    ) { inner ->

        Column (
            Modifier
                .background(BackGroundColor)
                .padding(top = 25.dp)
        ){
            ArrowAndMenuWithTitle("", navController, Modifier.padding(inner))

            EditMCPsTemplate(
                modifier = Modifier.padding(inner),
                count = count,
                storedInfo = storedInfo
            )
        }
    }
}

@Composable
fun EditMCPsTemplate(
    modifier: Modifier,
    count: MutableState<Int>,
    storedInfo: MutableList<String>
){

    val verticalScrollState = rememberScrollState()

    LaunchedEffect(storedInfo.size){
        verticalScrollState.animateScrollTo(verticalScrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
            .background(AiBackGround)
            .padding(horizontal = 30.dp)
            .verticalScroll(verticalScrollState)
//                    .horizontalScroll(horizontalScrollState)
//                    .verticalScroll(verticalScrollState)
    ){
        Spacer(Modifier.height(25.dp))

        EditMCPsAITurn(storedInfo[0])

        for(i in 1..(storedInfo.size/2)){
            EditMCPsUserTurn(storedInfo[i])

            if(i == (storedInfo.size/2) && storedInfo.size%2==0){}
            else {
                EditMCPsAITurn(storedInfo[i+1])
            }
        }
        Spacer(Modifier.height(65.dp))

    }
}

// AI 말풍선
@Composable
fun EditMCPsAITurn(
    text: String
){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .padding(
                    start = 25.dp,
                    end = 25.dp
                )
                .padding(bottom = 15.dp)
                .shadow(
                    shape = RoundedCornerShape(
                        topEnd = 15.dp,
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp
                    ),
                    elevation = 8.dp
                )


                .clip(
                    RoundedCornerShape(
                        topEnd = 15.dp,
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp
                    )
                )

                .background(AiTextBox)
        ){
            Text(
                lineHeight = 30.sp,
                modifier = Modifier
                    .padding(15.dp),
                style = TextStyle(
                    color = TextColor,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp
                ),
                text = text,

                )
        }
    }
}

// User 말풍성
@Composable
fun EditMCPsUserTurn(
    text: String
){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .padding(
                    start = 25.dp,
                    end = 25.dp
                )
                .padding(bottom = 15.dp)
                .shadow(
                    shape = RoundedCornerShape(
                        topStart = 15.dp,
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp
                    ),
                    elevation = 8.dp
                )


                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp
                    )
                )

                .background(UserTextBox)
        ){
            Text(
                lineHeight = 30.sp,
                modifier = Modifier
                    .padding(15.dp),
                style = TextStyle(
                    color = TextColor,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp
                ),
                text = text,

                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMCPsTextField(
    count: MutableState<Int>,
    storedInfo: MutableList<String>
){
    val input = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        BasicTextField(
            // 필터 입력 후 Action에 대해 정의
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(

                onDone = {},

                ),
            value = input.value,
            onValueChange = {
                input.value = it
            },
            modifier = Modifier
                .width(550.dp)

                .padding(
                    bottom = 15.dp
                )
                .shadow(
                    shape = RoundedCornerShape(
                        topStart = 15.dp,
                        bottomStart = 15.dp
                    ),
                    elevation = 8.dp
                )
                .clip(RoundedCornerShape(
                    topStart = 15.dp,
                    bottomStart = 15.dp
                ))

                .background(
                    color = Color.Transparent
                )
                .height(50.dp)


            ,
            singleLine = true,
            textStyle = TextStyle(
                color = TextColor,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 20.sp
            ),

            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    placeholder = {
//                        Text(
//                            text = "text",
//                            color = Color.LightGray,
//                            style = TextStyle(
//                                color = TextColor,
//                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                                fontSize = 20.sp
//                            ),
//                        )
                    },
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    enabled = true,
                    innerTextField = innerTextField,
                    value = input.value.toString(),
                    interactionSource = interactionSource,
                    contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                        top = 0.dp,
                        bottom = 0.dp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextColor,
                        unfocusedTextColor = TextColor,
                        focusedIndicatorColor = HomeDBListColor,
                        unfocusedIndicatorColor = HomeDBListColor,
                        cursorColor = TextColor,
                        unfocusedContainerColor = HomeDBListColor,
                        focusedContainerColor = HomeDBListColor,
                        errorContainerColor = HomeDBListColor,
                        disabledContainerColor = HomeDBListColor
                    ),
                )
            }
        )
        Button(
            contentPadding = PaddingValues(15.dp),
            shape = RoundedCornerShape(
                topEnd = 15.dp,
                bottomEnd = 15.dp
            ),
            modifier = Modifier
                .height(50.dp)
                .shadow(
                    shape = RoundedCornerShape(
                        topEnd = 15.dp,
                        bottomEnd = 15.dp
                    ),
                    elevation = 8.dp
                )
            ,
            colors = ButtonColors(
                contentColor = ButtonColor,
                containerColor = ButtonColor,
                disabledContentColor = ButtonColor,
                disabledContainerColor = ButtonColor
            ),
            onClick = {
                Log.e("zxc",input.value)
                storedInfo.add(input.value)
//                storedInfo.add("AI의 새로운 답변") // 임시적으로, 디자인 테스트를 위해 2개를 add한 것. 원래는 유저 추가 후 AI 의 답변을 기다린 후 추가
                input.value = ""
            }
        ) {
            Image(
                painter = painterResource(R.drawable.mcps_send),
                contentDescription = "",
            )
        }
    }
}