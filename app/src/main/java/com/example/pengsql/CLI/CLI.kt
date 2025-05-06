package com.example.pengsql.CLI

import android.text.BoringLayout
import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pengsql.Others.ArrowAndMenu
import com.example.pengsql.R
import com.example.pengsql.SelectDB.SelectDBTitle
import com.example.pengsql.Table.VerticalDividers
import com.example.pengsql.ui.theme.BackGroundColor
import com.example.pengsql.ui.theme.ButtonColor
import com.example.pengsql.ui.theme.ButtonTextColor
import com.example.pengsql.ui.theme.TableBackGroundColor
import com.example.pengsql.ui.theme.TextColor

@Composable
fun CLI(){

    // CLI 각 라인별 명령어를 저장
    val inputs = List(300){ remember { mutableStateOf("") }}

    // 실행 버튼 클릭시 아래의 변수에 명령어를 저장, 위 inputs 변수의 모든 값을 더한 후 optResult 변수에 넣기
    val optResult = remember { mutableStateOf("") }
    var visible = remember { mutableStateOf(false) }
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
            SelectDBTitle("SQL Name")
            CLIButtonPack(
                inputs = inputs,
                optResult = optResult
            )
        }
        CLITemplate(inputs)
    }
    
    // 결과화면을 보기 위한 버튼
    Button(
        onClick = {
            visible.value = !visible.value
        },
        shape = RectangleShape,
        colors = ButtonColors(
            contentColor = Color.Transparent,
            containerColor = Color(214,225,225),
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color(214,225,225)
        ),
        modifier = Modifier
            .offset(
                x = 100.dp,
                y = 350.dp
            )
            .height(50.dp)
            .width(150.dp)
            .clip(RoundedCornerShape(8.dp))
            ,
    ) {
        Image(
            modifier = Modifier
                .size(30.dp)
                .offset(
                    y = -5.dp
                ),
            painter = painterResource(R.drawable.cli_up),
            contentDescription = ""
        )
    }
    AnimatedVisibility(
        visible.value,
        enter = slideInVertically(initialOffsetY = { +300 }) + expandVertically(expandFrom = Alignment.Bottom) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
    ) {
        CLIResult(
            visible,
            "까꿍"
        )
    }
}

@Composable
fun CLIButton(
    img: Int,
    navController: NavController? = null,
    navDestination: String? = null,
    size: Dp = 15.dp,
    onClick : () -> Unit = {}
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
        onClick = {
            onClick()
        },
        colors = ButtonColors(
            contentColor = ButtonTextColor,
            containerColor = ButtonColor,
            disabledContainerColor = ButtonColor,
            disabledContentColor = ButtonTextColor
        ),
        shape = RectangleShape
    ) {
        Image(
            modifier = Modifier.size(size),
            painter = painterResource(img),
            contentDescription = ""
        )
    }
}


@Composable
fun CLIButtonPack(
    inputs: List<MutableState<String>>,
    optResult : MutableState<String>
){
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
        CLIButton(
            R.drawable.cli_right_arrow,
            onClick = {
                val tmp = mutableStateOf("")
                for(i in 0..inputs.size-1){
                    tmp.value += inputs[i].value + "\n"
                }
                if(tmp!=null){
                    optResult.value = tmp.value

                }
                Log.e("ddd","command is : ${optResult.value}")
            }
        )
        CLIButton(R.drawable.clf_left_arrow)
        CLIButton(R.drawable.cli_cancel, size = 19.dp)
    }
}

@Composable
fun CLITemplate(
    inputs: List<MutableState<String>>
){
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    Column (
        modifier = Modifier
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
        Column {
            for(i in 0..299){
                    CLIOneLine(i+1,inputs[i], offset = -(i*28).dp)
            }
        }
    }
}


@Composable
fun CLIOneLine(
    lineNo: Int,
    lineContents: MutableState<String>,
    offset: Dp = 0.dp
){
    Row(
        modifier = Modifier
            .offset(
                y = offset
            )
    ){
        Text(
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(35.dp)
                .offset(
                    x = 15.dp,
                    y = 19.5f.dp
                ),
            text = lineNo.toString(),
            style = TextStyle(
                color = TextColor,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 20.sp
            )
        )
        Spacer(Modifier.width(8.dp))
        VerticalDividers(
            modifier = Modifier
                .height(33.dp)
                .offset(
                    x = 20.dp,
                    y = 20.dp
                )
            ,
            thickness = 1.dp,
            color = Color.LightGray
        )
        Spacer(Modifier.width(8.dp))
        CLITextField(lineContents)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CLITextField(
    input: MutableState<String>
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        // 필터 입력 후 Action에 대해 정의
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            // 예시)
            // 백엔드로 필터링 된 데이터 요청 함수
            // callFilter2Backend(input)

            // 선택된 페이지에 관한 데이터는 이 함수의 input 변수를 활용
            onDone = {

            }
        ),
        value = input.value,
        onValueChange = {
            input.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp
            )
            .clip(RoundedCornerShape(15.dp))
            .background(
                color = Color.Transparent
            )
            .height(60.dp),
        singleLine = true,
        textStyle = TextStyle(
            color = TextColor,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 20.sp
        ),

        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                placeholder = {
                    Text(
                        text = "text",
                        color = Color.LightGray,
                        style = TextStyle(
                            color = TextColor,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 20.sp
                        ),
                    )
                },
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                innerTextField = innerTextField,
                value = input.value.toString(),
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = TextColor,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
            )
        }
    )
}

@Composable
fun CLIResult(
    visible: MutableState<Boolean>,
    result: String
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        // 쿼리문 실행 결과 영역////////////////////////////////
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = result,
            style = TextStyle(
                color = TextColor,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp
            )
        )
        Image(
            modifier = Modifier
                .size(450.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.poketgo),
            contentDescription = ""
        )
        //////////////////////////////////////////////////
    }
    Button(
        shape = RectangleShape,
        colors = ButtonColors(
            contentColor = Color.Transparent,
            containerColor = Color(214,225,225),
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color(214,225,225)
        ),
        modifier = Modifier
            .offset(
                x = 100.dp,
                y = -15.dp
            )
            .height(50.dp)
            .width(150.dp)
            .clip(RoundedCornerShape(8.dp))
        ,
        onClick = {
            visible.value = !visible.value
        }
    ) {
        Image(
            modifier = Modifier
                .size(30.dp)
                .offset(
                    y = 5.dp
                ),
            painter = painterResource(R.drawable.cli_down),
            contentDescription = ""
        )
    }
}