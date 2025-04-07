package com.example.vept.ed.L4

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import com.example.vept.ui.other.ArrowAndMenu
import com.example.vept.R
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.ButtonColor
import com.example.vept.ui.theme.ButtonTextColor
import com.example.vept.ui.theme.TableBackGroundColor
import com.example.vept.ui.theme.TextColor
import com.example.vept.ui.theme.TitleColor



@Composable
fun EditerMainDesign(
    viewModel: EditerMainViewModel,
    navController: NavHostController
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
            SelectDBTitle(viewModel.databaseName)


            SelectDBButton(
                text = "Table",
                onNavigate = { destination ->
                    navController.navigate(destination)
                }
            )
            SelectDBButtonPack()
        }
        SelectDBTemplate(viewModel)
    }
}
@Composable
fun SelectDBTitle(
    text: String
){
    Column {
        Text(
            modifier = Modifier
                .padding(start = 45.dp),
            text = text,
            style = TextStyle(
                color = TitleColor,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 28.sp
            )
        )
    }
}


@Composable
fun SelectDBTemplate(viewModel: EditerMainViewModel){
    val indexInfoState by viewModel.indexInfo.observeAsState(emptyMap())
    val tableMap by viewModel.tableFieldMap.observeAsState(emptyMap())
    val viewMap by viewModel.viewInfo.observeAsState(emptyMap())
    val triggers by viewModel.triggerList.observeAsState(emptyList())
    fun toSingleLayerMap(triggerList: List<String>): Map<String, List<String>> {
        return triggerList.associateWith { emptyList() }
    }
    val triggerMap = toSingleLayerMap(triggers)

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
    ){
        Column {
            SelectDBHeader(
                headName = "테이블",
                tableFieldMap = tableMap

            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray

            )
            SelectDBHeader(
                headName = "인덱스",
                tableFieldMap = indexInfoState

            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray

            )
            SelectDBHeader(
                headName = "뷰",
                tableFieldMap = viewMap

            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray

            )
            SelectDBHeader(
                headName = "트리거",
                tableFieldMap = triggerMap
            )
            Spacer(Modifier.height(30.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun SelectDBHeader(
    headName: String,
    tableFieldMap: Map<String, List<String>>
) {
    val isOpened = remember{ mutableStateOf(false) }

    if (isOpened.value) {
        Column {
            Spacer(Modifier.height(30.dp))
            Row {
                Spacer(Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .offset(x = 4.dp, y = 1.dp)
                        .clickable { isOpened.value = false },
                    painter = painterResource(R.drawable.small_down),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "$headName (${tableFieldMap.size})",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 16.sp
                    )
                )
            }
            tableFieldMap.forEach { (parentName, childList) ->
                SelectDBParent(
                    parentName = parentName,
                    childList = childList,
                    tableIsOpened = isOpened
                )
            }
        }
    } else {
        Column {
            Spacer(Modifier.height(30.dp))
            Row {
                Spacer(Modifier.width(20.dp))
                Icon(
                    modifier = Modifier
                        .offset(x = 1.dp, y = 3.dp)
                        .clickable { isOpened.value = true },
                    painter = painterResource(R.drawable.right_arrow),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "$headName (${tableFieldMap.keys.size})",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun SelectDBParent(
    tableIsOpened: MutableState<Boolean>,
    parentName: String,
    childList: List<String>
) {
    // 트리거 전용: 자식 없음
    val isTrigger = childList.isEmpty()

    // 자식 DropDown condition
    val isDropDownExpanded = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(childList.size) { add(false) }
        }
    }

    // 부모 DropDown condition
    val isParentDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableStateOf(0) }
    val isOpened = remember { mutableStateOf(false) }

    if (tableIsOpened.value && isOpened.value) {
        Column {
            Spacer(Modifier.height(10.dp))
            Row {
                Spacer(Modifier.width(34.dp))
                Icon(
                    modifier = Modifier
                        .offset(x = 1.dp, y = 1.dp)
                        .clickable { isOpened.value = false },
                    painter = painterResource(R.drawable.small_down),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(2.5f.dp))
                Text(
                    text = parentName + if (!isTrigger) " (${childList.size})" else "",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    )
                )
            }

            if (!isTrigger) {
                childList.forEachIndexed { index, child ->
                    Column {
                        Spacer(Modifier.height(5.dp))
                        Row {
                            Spacer(Modifier.width(70.dp))
                            Text(
                                modifier = Modifier.clickable {
                                    isDropDownExpanded[index] = true
                                },
                                text = child,
                                style = TextStyle(
                                    color = TextColor,
                                    fontFamily = FontFamily(Font(R.font.roboto_thin)),
                                    fontSize = 14.sp
                                )
                            )
                            DropdownMenu(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(TableBackGroundColor)
                                    .border(1.dp, TextColor, RectangleShape),
                                expanded = isDropDownExpanded[index],
                                onDismissRequest = {
                                    isDropDownExpanded[index] = false
                                }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "테이블 보기",
                                            style = TextStyle(
                                                color = TextColor,
                                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                                fontSize = 14.sp
                                            )
                                        )
                                    },
                                    onClick = {
                                        isDropDownExpanded[index] = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        Column {
            Spacer(Modifier.height(10.dp))
            Row {
                Spacer(Modifier.width(40.dp))
                Icon(
                    modifier = Modifier
                        .offset(x = 1.dp, y = 3.dp)
                        .clickable { isOpened.value = true },
                    painter = painterResource(R.drawable.right_arrow),
                    contentDescription = "",
                    tint = TextColor
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    modifier = Modifier.clickable {
                        isParentDropDownExpanded.value = true
                    },
                    text = parentName + if (!isTrigger) " (${childList.size})" else "",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    )
                )
                DropdownMenu(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(TableBackGroundColor)
                        .border(1.dp, TextColor, RectangleShape),
                    expanded = isParentDropDownExpanded.value,
                    onDismissRequest = {
                        isParentDropDownExpanded.value = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "테이블 수정",
                                style = TextStyle(
                                    color = TextColor,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    fontSize = 14.sp
                                )
                            )
                        },
                        onClick = {
                            isParentDropDownExpanded.value = false
                            itemPosition.value = 0
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "테이블 삭제",
                                style = TextStyle(
                                    color = TextColor,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    fontSize = 14.sp
                                )
                            )
                        },
                        onClick = {
                            isParentDropDownExpanded.value = false
                            itemPosition.value = 1
                        }
                    )
                }
            }
        }
    }
}




@Composable
fun SelectDBButton(
    text: String,
    onNavigate: (String) -> Unit
){
    val context = LocalContext.current

    Button(
        contentPadding = PaddingValues(7.dp),
        modifier = Modifier
            .height(30.dp)
            .padding(
                start = 1.dp,
                end = 1.dp
            )
        ,
        onClick = { //
            onNavigate("mod")
            // 이동
            //val intent = Intent(context, EditTableMod::class.java)
            //intent.putExtra(EditTableMod.EXTRA_DATABASE_NAME, EditerMain.EXTRA_DATABASE_NAME)
            //context.startActivity(intent)


        },
        colors = ButtonColors(
            contentColor = ButtonTextColor,
            containerColor = ButtonColor,
            disabledContainerColor = ButtonColor,
            disabledContentColor = ButtonTextColor
        ),
        shape = RectangleShape
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            style = TextStyle(
                color = ButtonTextColor,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 15.sp
            )
        )
    }
}


@Composable
fun SelectDBButtonPack(){
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
        /*
        SelectDBButton(
            "테이블 생성",
            navController = TODO()
        )
        SelectDBButton(
            "인덱스 생성",
            navController = TODO()
        )
        SelectDBButton(
            "저장",
            navController = TODO()
        )
        SelectDBButton(
            "취소",
            navController = TODO()
        )*/
    }
}
