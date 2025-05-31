package com.example.vept

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vept.ed.L4.EditerMain
import com.example.vept.pl.L4.PlannerMain

import com.example.vept.sysops.L1.FileExplorer
import com.example.vept.sysops.L1.NewFile

//import com.example.vept.ed.L4L.EditorMainActivity

import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.ButtonColor
import com.example.vept.ui.theme.ButtonTextColor
import com.example.vept.ui.theme.HomeDBListColor
import com.example.vept.ui.theme.HomeDownLoadButton
import com.example.vept.ui.theme.TextColor


@Composable
fun Home(
    viewModel: MainDesignViewModel
){



    val context = LocalContext.current


    Column (
        Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(
                top = 10.dp
            )
    ){
        //OnlyMenu()
        Spacer(Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
            ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            HomeDBList(viewModel)
            Spacer(Modifier.width(65.dp))
            Column(
                modifier = Modifier
                    .offset(
                        y = -2.dp
                    )
            ){
                HomeButton(
                    contents = "다이어그램 생성",
                    onClick = {
                        
                        //Todo, 다이어그램 네비게이션 연결
                        val intent = Intent(context, PlannerMain::class.java)
                        context.startActivity(intent)
                    }
                )
                Spacer(Modifier.height(25.dp))
                HomeButton(
                    contents = "DB 생성",
                    onClick = {

                        showDbNameDialog(context,viewModel)

                    }
                )
                Spacer(Modifier.height(25.dp))
                Row {
//                    HomeButton(
//                        contents = "모듈 관리자",
//                        onClick = {}
//                    )

                    Spacer(Modifier.width(225.dp))

                    HomeDownButton(viewModel)
                }
            }
        }

    }
}

fun showDbNameDialog(context: Context,     viewModel: MainDesignViewModel) {
    val editText = EditText(context).apply {
        hint = "DB 이름을 입력하세요"
    }

    AlertDialog.Builder(context)
        .setTitle("새 데이터베이스 생성")
        .setView(editText)
        .setPositiveButton("생성") { dialog, _ ->
            val inputName = editText.text.toString().trim()
            if (inputName.isNotEmpty()) {
                NewFile.createNewDb(context, inputName)
                viewModel.loadDatabaseNames(context) // 리스트 갱신
            } else {
                Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
        .show()
}


@Composable
fun HomeDBList(
    viewModel: MainDesignViewModel
){
    val fileData by viewModel.fileDataLiveData.observeAsState(emptyList())
    val context = LocalContext.current

    val scrollState = rememberScrollState()





    Column (
        modifier = Modifier
            .padding(
                bottom = 50.dp
            )
    ){
        Image(
            modifier = Modifier
                .height(50.dp)
                .padding(
                    start = 70.dp
                )
                .offset(
                    y = -5.dp
                ),
            painter = painterResource(R.drawable.home_title),
            contentDescription = ""
        )
        Column(
            modifier = Modifier
                .padding(
                    start = 70.dp
                )
                .offset(
                )
                .shadow(elevation = 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(HomeDBListColor)
                .width(350.dp)
                .height(230.dp)
                .verticalScroll(scrollState)
        ){
            fileData.forEach { fileName ->
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
                        text = fileName,
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
                        onClick = {
                            val intent = Intent(context, EditerMain::class.java)
                            intent.putExtra(EditerMain.EXTRA_DATABASE_NAME, fileName)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.edit),
                            contentDescription = "",
                            tint = Color(71,144,247)
                        )
                    }

                    Spacer(Modifier.width(5.dp))

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
                        onClick = {
                            AlertDialog.Builder(context)
                                .setTitle("삭제 확인")
                                .setMessage("'$fileName'을 삭제하시겠습니까?")
                                .setPositiveButton("삭제") { _, _ ->
                                    viewModel.deleteDatabase(context, fileName)
                                }
                                .setNegativeButton("취소", null)
                                .show()
                        }
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
    viewModel: MainDesignViewModel
){
    val context = LocalContext.current
    val activity = context as AppCompatActivity // 파일 선택 결과 처리를 위한 Activity 필요
    val fileExplorer = remember { FileExplorer(context, null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                fileExplorer.setSelectedFileUri(uri)
                fileExplorer.classifyAndSave()
                viewModel.loadDatabaseNames(context) // 리스트 갱신
            }
        }
    }

    LaunchedEffect(Unit) {
        fileExplorer.setFilePickerLauncher(launcher)
    }


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
            fileExplorer.openFileExplorer()
        }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.home_download),
            contentDescription = ""
        )
    }
}


@Composable
fun OnlyMenu(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 40.dp,
                end = 40.dp,
                bottom = 5.dp
            ),
        horizontalArrangement = Arrangement.End
    ) {

        Icon(
            painter = painterResource(R.drawable.menu),
            tint = Color.Black,
            contentDescription = "",
        )
    }
}