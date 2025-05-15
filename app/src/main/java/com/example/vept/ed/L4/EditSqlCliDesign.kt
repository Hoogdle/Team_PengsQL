package com.example.vept.ed.L4

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

@Composable
fun EditSqlCliDesign(
    viewModel: EditSqlCliViewModel,
    navController: NavHostController
) {
    val textState = remember { mutableStateOf("") }
    val cliResult by viewModel.getCliResult().observeAsState()
    val visible = remember { mutableStateOf(false) }

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
            ArrowAndMenu()
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SelectDBTitle("SQL CLI")
                CLIButtonPack(viewModel = viewModel, textState = textState)
            }
            SqlCliWithIndexBar(textState = textState)
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
    textState: MutableState<String>
) {
    Row(
        Modifier
            .padding(end = 15.dp)
            .offset(x = (-50).dp, y = 3.dp)
            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
    ) {
        CLIButton(
            R.drawable.cli_right_arrow,
            onClick = {
                viewModel.executeSQL(textState.value)
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




@Composable
fun SqlCliWithIndexBar(
    textState: MutableState<String>
) {
    val scrollState = rememberScrollState()

    val indexText = remember(textState.value) {
        val lineCount = textState.value.count { it == '\n' } + 1
        (1..lineCount).joinToString("\n") { it.toString() }
    }

    Row(
        Modifier
            .fillMaxSize()
            .background(TableBackGroundColor)
            .padding(10.dp)
    ) {
        // Index TextField (읽기 전용)
        BasicTextField(
            value = indexText,
            onValueChange = {}, // 읽기 전용
            enabled = false,
            readOnly = true,
            modifier = Modifier
                .width(40.dp)
                .background(Color.DarkGray)
                .verticalScroll(scrollState),

            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.End
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Main Input Field
        BasicTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.Black
            )
        )
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