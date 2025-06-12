package com.pangsql.vept.ed.L4

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pangsql.vept.R
import com.pangsql.vept.ed.L2.EditDB
import com.pangsql.vept.ui.other.ArrowAndTitle
import com.pangsql.vept.ui.theme.AiBackGround
import com.pangsql.vept.ui.theme.AiTextBox
import com.pangsql.vept.ui.theme.BackGroundColor
import com.pangsql.vept.ui.theme.ButtonColor
import com.pangsql.vept.ui.theme.HomeDBListColor
import com.pangsql.vept.ui.theme.TextColor
import com.pangsql.vept.ui.theme.UserTextBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun EditMCPsDesign(
    viewModel: EditSqlCliViewModel,
    navController: NavHostController
){
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color(216,224,227)
    )
    val count = remember { mutableStateOf(0) }

    val storedInfo = remember { mutableStateListOf("안녕하세요. PengSQL AI assistant 입니다. 무엇을 도와드릴까요?") }
    val isButtonOn = remember { mutableStateOf(true) }




    Scaffold(
        bottomBar = {
            EditMCPsTextField(
                viewModel = viewModel,
                count = count,
                storedInfo = storedInfo,
                isButtonOn = isButtonOn
            )
        }
    ) { inner ->
        Column (
            Modifier
                .background(BackGroundColor)
        ){
            ArrowAndTitle(navController = navController, title = "PengSAI", destination = "main")

            EditMCPsTemplate(
                viewModel = viewModel,
                modifier = Modifier.padding(inner),
                count = count,
                storedInfo = storedInfo
            )
        }
    }
}

@Composable
fun EditMCPsTemplate(
    viewModel : EditSqlCliViewModel,
    modifier: Modifier,
    count: MutableState<Int>,
    storedInfo: MutableList<String>
){

    val verticalScrollState = rememberScrollState()
    val cliResult by viewModel.getCliResult().observeAsState("")

//    if (cliResult.isNotBlank()) {
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .background(Color(0xFF222222))
//        ) {
//            Text(
//                "SQL 실행 결과",
//                color = Color.Cyan,
//
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//            Text(
//                cliResult,
//                color = Color.White,
//
//            )
//        }
//    }
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

        for(i in 1..storedInfo.size-1){
            if(i%2==0) {
                EditMCPsAITurn(storedInfo[i])
            }
            else {
                EditMCPsUserTurn(storedInfo[i])
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
                    color = Color.White,
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
    viewModel: EditSqlCliViewModel,
    count: MutableState<Int>,
    storedInfo: MutableList<String>,
    isButtonOn: MutableState<Boolean>
){
    val context = LocalContext.current
    val finalDataSet = remember { mutableStateOf("") }
    val input = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        val editDB = viewModel.editDB
        if (editDB != null) {
            val sb = StringBuilder()
            editDB.getTableName1().forEach { table ->
                val tableInfo = editDB.getTableFields1(table)
                sb.append(tableInfo).append(",")
            }
            if (sb.isNotEmpty()) sb.deleteCharAt(sb.length - 1)
            finalDataSet.value = sb.toString()
        } else {
            Log.e("EditMCPsDesign", "EditDB is null in ViewModel")
        }
    }


    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        val context = LocalContext.current

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
                if (isButtonOn.value) {
                    val tmpStore = input.value
                    isButtonOn.value = false
                    storedInfo.add(tmpStore)
                    storedInfo.add("요청 처리 중 입니다..")

//                    val promptCommand = "언제나 답변은 sqlite3 sql로 제공되어야 한다. 현재 db에 있는 테이블과 필드는 없을 수 있거나 혹은 다음과 같다."
                    val dbSchema = finalDataSet.value
//                    val fullPrompt = listOf(promptCommand, dbSchema, tmpStore)
//                        .joinToString(separator = "\",\n\"", prefix = "[\"", postfix = "\"]")

//                    Log.e("프롬프트", fullPrompt)

                    runBlocking {
                        launch {
                            val aiResponse = AiServer(prompt = tmpStore, db = dbSchema).toString()
                            storedInfo[storedInfo.size - 1] = aiResponse

                            viewModel.executeSQL(aiResponse)
                        }
                    }

                    input.value = ""
                    isButtonOn.value = true


                } else {
                    Toast.makeText(context, "AI가 요청을 처리하고 있습니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show()
                }

            }
        ) {
            Image(
                painter = painterResource(R.drawable.mcps_send),
                contentDescription = "",
            )
        }
    }
}