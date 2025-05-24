package com.example.vept.ed.L4

import android.annotation.SuppressLint
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
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import com.example.vept.R
import com.example.vept.ui.other.ArrowAndMenu
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.ButtonColor
import com.example.vept.ui.theme.ButtonTextColor
import com.example.vept.ui.theme.TableBackGroundColor
import com.example.vept.ui.theme.TextColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
// UI 요소 관련
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
// 그 외 기본 Compose 요소들
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import com.example.vept.ui.other.ArrowAndMenuCLI

@SuppressLint("UnrememberedMutableState")
@Composable
fun EditSqlCliDesign(
    viewModel: EditSqlCliViewModel,
    navController: NavHostController
) {
    val lineNo = remember { mutableStateOf(10) }
    val userInputs = remember { mutableListOf(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
    ) }


    // TextField, 10 증가에서 3 증가로 고쳤습니다. 3이 훨씬 부드럽게 돌아가는거 같습니담.
    for(i in userInputs.size-1 downTo 0){
        if(userInputs[i].value != "" && i>lineNo.value-3){
            for(i in 0..2) userInputs.add(mutableStateOf(""))
            lineNo.value += 3
        }
    }

    val cliResult by viewModel.getCliResult().observeAsState()
    val visible = remember { mutableStateOf(false) }

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(cliResult) {
        if (!cliResult.isNullOrBlank()) {
            visible.value = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .background(BackGroundColor)
                .padding(top = 25.dp)
                .fillMaxSize()
        ) {
            ArrowAndMenuCLI(
                title = "SQL CLI",
                navController = navController,
                viewModel = viewModel,
                textState = userInputs,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackGroundColor)
                    .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
                    .background(TableBackGroundColor)
                    .padding(horizontal = 30.dp)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
//                    .horizontalScroll(horizontalScrollState)
//                    .verticalScroll(verticalScrollState)
            ){
                for(i in 0..userInputs.size-1){
                    CLIOneLine(i+1,userInputs[i],offset = -(i*28).dp)
                }
            }
        }

        // 결과창
        AnimatedVisibility(
            visible = visible.value,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            CLIResultBox(
                result = cliResult ?: ""
            )
        }

        // 항상 보이는 토글 버튼
        Button(
            onClick = { visible.value = !visible.value },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .height(40.dp)
                .width(130.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(214, 225, 225))
        ) {
            Text(if (visible.value) "닫기" else "결과 보기")
        }
    }
}
@Composable
fun CLIButtonPack(
    viewModel: EditSqlCliViewModel,
    textState: MutableList<MutableState<String>>,
) {
    Row(
        Modifier
            .offset(
                y = 15.dp
            )
            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
    ) {
        CLIButton(
            R.drawable.cli_right_arrow,
            onClick = {
                var result: String = ""
                textState.forEachIndexed { index, item ->
                    result += item.value
                }
                Log.e("ddd",result)
                viewModel.executeSQL(result)
            }
        )
        CLIButton(
            R.drawable.cli_cancel,
            size = 19.dp,
            onClick = {
                viewModel.cancelExecution()
            }
        )
    }
}



@Composable
fun CLIButton(
    img: Int,
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




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SqlCliWithIndexBar(
    lineNo: MutableState<Int>,
    textState: MutableList<MutableState<String>>
) {


    val scrollState = rememberScrollState()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }


    Row(
        Modifier
            .fillMaxSize()
            .background(TableBackGroundColor)
            .padding(10.dp)

    ) {

        Spacer(modifier = Modifier.width(8.dp))

        // Main Input Field

    }
}

@Composable
fun CLIOneLine(
    lineNoFixed: Int,
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
            text = lineNoFixed.toString(),
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

@Composable
fun CLIResultBox(
    result: String) {
    Surface(
        color = Color.White,
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 300.dp)
            .padding(8.dp)
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = result,
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CLITextField(
    input: MutableState<String>
) {
    val interactionSource = remember { MutableInteractionSource() }
    val (focusRequester) = FocusRequester.createRefs()
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
            .fillMaxWidth()
            .focusRequester(focusRequester)
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
