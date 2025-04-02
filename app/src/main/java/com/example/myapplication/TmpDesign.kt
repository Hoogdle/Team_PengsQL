package com.example.myapplication

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// MVVM 아키텍쳐를 기반으로 설계하였습니다.

// 쉽게 말해, 이를 웹 프로그래밍에 비유하자면
// Model : 프론트엔드
// viewModel : 백엔드
// 로 비유적으로 생각하시면 될 것 같습니다.

// MVVM에 자세한 내용은 아래 URL을 참조해주세요.
// https://goharry.tistory.com/55


@Composable
fun TmpDesign(
    viewModel: TmpDesignViewModel
){
    Row (
        modifier = Modifier
            .padding(30.dp)
    ){
        Column(modifier = Modifier.weight(8f)){
            Spacer(modifier = Modifier.height(35.dp))
            Title()
            Spacer(modifier = Modifier.height(15.dp))
            FileList(viewModel = viewModel)
        }
        Spacer(modifier = Modifier.width(25.dp))
        Column(modifier = Modifier.weight(2f)){
            Spacer(modifier = Modifier.height(85.dp))
            TmpDesignButtonPack(viewModel)
            Spacer(modifier = Modifier.height(15.dp))
            BottomButton(viewModel = viewModel)
        }
    }
}


// 앱 이름
@Composable
fun Title(){
    Text(
        text = "Peng's QL",
        fontSize = 25.sp,
        style = TextStyle(
            color = Color.Black,
            fontWeight = FontWeight(weight = 20)
        )
    )
}

// 파일 박스(리스트)
@Composable
fun FileList(
    viewModel: TmpDesignViewModel
){
    // 화면 스크롤 state 저장 변수
    val scrollState = rememberScrollState()
    // do not use lazy row, it cause strange error
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ){
        val fileData by viewModel.fileData.collectAsState()
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RectangleShape
                )
                .fillMaxWidth()
        ){
            Row {
                Column(){
                    Text(
                        text = "파일이름",
                        modifier = Modifier.padding(10.dp)
                    )
                    // fileData의 값을 하나씩 갖고 와서 for문 느낌으로 뿌려줍니다.
                    fileData.forEachIndexed { index, tmpDesignData ->
                            Text(
                                text = tmpDesignData.fileName,
                                modifier = Modifier.padding(10.dp)
                            )
                    }
                }
                Column(){
                    Text(
                        text = "파일크기",
                        modifier = Modifier.padding(10.dp)
                    )
                    // fileData의 값을 하나씩 갖고 와서 for문 느낌으로 뿌려줍니다.
                    fileData.forEachIndexed { index, tmpDesignData ->
                        Text(
                            text = "${tmpDesignData.size} Byte",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

// 버튼123 개별
@Composable
fun TmpDesignButton(
    viewModel: TmpDesignViewModel,
    name: String // button name
){
    Button(
        onClick = {
            // 이곳에 파일 추가시 부를 함수를 넣으면 됩니다.
            // 보통 viewModel를 통해 해당 함수를 호출하는 것이 일반적입니다. ex) viewModel.button1()
        }
    ) {
        Text(
            text = name
        )
    }
}

// 버튼123 묶음
@Composable
fun TmpDesignButtonPack(
    viewModel: TmpDesignViewModel
){
    Column(){
        TmpDesignButton(viewModel,"버튼1")
        TmpDesignButton(viewModel,"버튼2")
        TmpDesignButton(viewModel,"버튼3")
    }
}

// 파일 추가 버튼
@Composable
fun BottomButton(
    viewModel: TmpDesignViewModel
){
    Button(
        onClick = {
            // 이곳에 파일 추가시 부를 함수를 넣으면 됩니다.
            // 보통 viewModel를 통해 해당 함수를 호출하는 것이 일반적입니다. ex) viewModel.fileAdd()
        }
    ) {
        Text(
            text = "+"
        )
    }
}