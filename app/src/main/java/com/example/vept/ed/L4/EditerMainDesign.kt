package com.example.vept.ed.L4

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vept.ui.other.ArrowAndMenu
import com.example.vept.R
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.TableBackGroundColor
import com.example.vept.ui.theme.TextColor
import com.example.vept.ui.theme.TitleColor

/*
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
            //<<===디버그용===!!//
            DebugDropdown(navController = navController)
            //!!===디버그용===>>//
            SelectDBButtonPack()
        }
        SelectDBTemplate(viewModel, navController)
    }
}*/

@Composable
fun EditerMainDesign(
    viewModel: EditerMainViewModel,
    navController: NavHostController
) {
    val databaseName = viewModel.databaseName
    Column(
        Modifier
            .background(BackGroundColor)
            .padding(top = 25.dp)
    ) {
        ArrowAndMenu()
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SelectDBTitle(databaseName)
            DebugDropdown(navController = navController)
            SelectDBButtonPack()
        }
        SelectDBTemplate(viewModel, navController)
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
fun TextHeader(headName: String, size: Int) {
    Text(
        text = "$headName ($size)",
        style = TextStyle(
            color = TextColor,
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            fontSize = 16.sp
        )
    )
}
/*
@Composable
fun SelectDBTemplate(
    viewModel: EditerMainViewModel,
    navController : NavHostController
) {
    val indexInfoState by viewModel.indexInfo.observeAsState(emptyMap())
    val tableMap by viewModel.tableFieldMap.observeAsState(emptyMap())
    val viewMap by viewModel.viewInfo.observeAsState(emptyMap())
    val triggers by viewModel.triggerList.observeAsState(emptyList())

    val triggerMap = triggers.associateWith { emptyList<String>() }

    val verticalScrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(BackGroundColor)
            .clip(RoundedCornerShape(35.dp, 35.dp, 0.dp, 0.dp))
            .background(TableBackGroundColor)
            .padding(start = 30.dp, end = 30.dp)
            .verticalScroll(verticalScrollState)
    ) {
        Column {
            SectionWithHeaderAndItems("테이블", tableMap, viewModel, navController)
            SectionWithHeaderAndItems("인덱스", indexInfoState, viewModel, navController)
            SectionWithHeaderAndItems("뷰", viewMap, viewModel, navController)
            SectionWithHeaderAndItems("트리거", triggerMap, viewModel, navController)
        }
    }
}

@Composable
fun SectionWithHeaderAndItems(
    headerName: String,
    items: Map<String, List<String>>,
    viewModel: EditerMainViewModel,
    navController: NavHostController
) {
    // 상태를 상위에서 관리하고 하위로 전달
    val isOpened = remember { mutableStateOf(false) }
    SelectDBHeader(
        headName = headerName,
        tableFieldMap = items,
        isOpened = isOpened,
        viewModel = viewModel,
        navController = navController
    )
    Spacer(Modifier.height(30.dp))
    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
}

@Composable
fun SelectDBHeader(
    headName: String,
    tableFieldMap: Map<String, List<String>>,
    isOpened: MutableState<Boolean>, // 상위에서 받은 isOpened 상태
    viewModel: EditerMainViewModel, // 추가
    navController: NavHostController
) {
    if (isOpened.value) {
        Column {
            Row {
                ToggleHeaderButton(isOpened = isOpened)
                TextHeader(headName = headName, size = tableFieldMap.size)
            }
            tableFieldMap.forEach { (itemName, relatedList) ->
                SelectTableItem(
                    itemName = itemName,
                    relatedList = relatedList,
                    itemType = headName,
                    tableIsOpened = isOpened,
                    onView = { name, type ->
                        viewModel.viewItem(name, type, navController)
                    },
                    onEdit = { name, type ->
                        viewModel.editItem(name, type, navController)
                    },
                    onDelete = { name, type ->
                        Log.d("DBAction", "onDelete triggered: $name, $type")
                        viewModel.deleteItem(name, type)  // deleteItem 호출
                    }

                )
            }
        }
    } else {
        Column {
            Row {
                ToggleHeaderButton(isOpened = isOpened)
                TextHeader(headName = headName, size = tableFieldMap.keys.size)
            }
        }
    }
}

@Composable
fun SelectTableItem(
    itemName: String,
    relatedList: List<String>,
    itemType: String, // 자료형에 따른 동작 처리
    tableIsOpened: MutableState<Boolean>, // 부모에서 전달받은 상태
    onView: (String, String) -> Unit,
    onEdit: (String, String) -> Unit,
    onDelete: (String, String) -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val isOpened = remember { mutableStateOf(false) }
    val selectedAction = remember { mutableStateOf(-1) }

    // 자료형에 따라 드롭다운 메뉴 항목 설정
    val dropdownItems = remember(itemType) {
        when (itemType) {
            "테이블" -> listOf("보기", "수정", "삭제")
            "뷰" -> listOf("보기", "삭제")
            "인덱스" -> listOf("수정", "삭제")
            "트리거" -> listOf("수정", "삭제")
            else -> listOf()
        }
    }

    // 동적 데이터를 한 번만 로드하고 저장하도록 처리
    val isDataLoaded = remember { mutableStateOf(false) }

    // 데이터 로딩 (비동기 호출 예시)
    LaunchedEffect(key1 = itemName) {
        if (!isDataLoaded.value) {
            // 비동기 데이터 로드
            // 예: viewModel.loadDataForItem(itemName)
            isDataLoaded.value = true
        }
    }

    // UI 구성
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
                    text = itemName + if (relatedList.isNotEmpty()) " (${relatedList.size})" else "",
                    style = TextStyle(
                        color = TextColor,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    )
                )
            }

            // 관련 항목 단순 표시
            relatedList.forEach { related ->
                Spacer(Modifier.height(5.dp))
                Row {
                    Spacer(Modifier.width(70.dp))
                    Text(
                        text = related,
                        style = TextStyle(
                            color = TextColor,
                            fontFamily = FontFamily(Font(R.font.roboto_thin)),
                            fontSize = 14.sp
                        )
                    )
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
                        isDropDownExpanded.value = true
                    },
                    text = itemName + if (relatedList.isNotEmpty()) " (${relatedList.size})" else "",
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
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = {
                        isDropDownExpanded.value = false
                    }
                ) {
                    dropdownItems.forEach { action ->
                        DropdownMenuItem(
                            text = { Text(action, style = dropdownTextStyle()) },
                            onClick = {
                                selectedAction.value = dropdownItems.indexOf(action)
                                isDropDownExpanded.value = false

                                when (action) {
                                    "보기" -> onView(itemName, itemType)
                                    "수정" -> onEdit(itemName, itemType)
                                    "삭제" -> onDelete(itemName, itemType)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
*/

// 스타일 재사용 함수
fun dropdownTextStyle(): TextStyle {
    return TextStyle(
        color = TextColor,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontSize = 14.sp
    )
}

@Composable
fun SelectDBTemplate(
    viewModel: EditerMainViewModel,
    navController: NavHostController
) {
    val indexInfoState by viewModel.indexInfo.observeAsState(emptyMap())
    val tableMap by viewModel.tableFieldMap.observeAsState(emptyMap())
    val viewMap by viewModel.viewInfo.observeAsState(emptyMap())
    val triggers by viewModel.triggerList.observeAsState(emptyList())
    val triggerMap = triggers.associateWith { emptyList<String>() }

    val sectionExpandedMap = remember { mutableStateMapOf<String, Boolean>() }

    val verticalScrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(TableBackGroundColor)
            .verticalScroll(verticalScrollState)
            .padding(horizontal = 30.dp)
    ) {
        Column {
            listOf("테이블", "인덱스", "뷰", "트리거").forEach { section ->
                val items = when (section) {
                    "테이블" -> tableMap
                    "인덱스" -> indexInfoState
                    "뷰" -> viewMap
                    "트리거" -> triggerMap
                    else -> emptyMap()
                }
                SectionWithHeaderAndItems(
                    headerName = section,
                    items = items,
                    expandedMap = sectionExpandedMap,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun SectionWithHeaderAndItems(
    headerName: String,
    items: Map<String, List<String>>,
    expandedMap: MutableMap<String, Boolean>,
    viewModel: EditerMainViewModel,
    navController: NavHostController
) {
    val isSectionExpanded = expandedMap.getOrPut(headerName) { false }

    // 항목이 0개일 경우 확장 아이콘은 숨기고, 항목 이름은 그대로 표시
    val shouldExpand = items.isNotEmpty()

    Column {
        // 항목이 있을 때만 확장 가능하도록 처리
        Row(
            Modifier
                .clickable { expandedMap[headerName] = !isSectionExpanded }
                .padding(vertical = 8.dp)
        ) {
            // 확장 아이콘: 항목이 있을 때만 표시
            if (shouldExpand) {
                Icon(
                    painter = painterResource(
                        if (isSectionExpanded) R.drawable.small_down else R.drawable.right_arrow
                    ),
                    contentDescription = "",
                    tint = TextColor
                )
            }

            // 항목 이름: 항상 표시
            TextHeader(headName = headerName, size = items.size)
        }

        // 확장된 항목을 표시
        if (isSectionExpanded) {
            items.forEach { (itemName, relatedList) ->
                val itemKey = "$headerName::$itemName"
                val isItemExpanded = expandedMap.getOrPut(itemKey) { false }

                SelectTableItem(
                    itemName = itemName,
                    relatedList = relatedList,
                    itemType = headerName,
                    isExpanded = isItemExpanded,
                    onExpandToggle = { expandedMap[itemKey] = !isItemExpanded },
                    onView = { viewModel.viewItem(it, headerName, navController) },
                    onEdit = { viewModel.editItem(it, headerName, navController) },
                    onDelete = { viewModel.deleteItem(it, headerName) }
                )
            }
        }

        Spacer(Modifier.height(30.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    }
}

@Composable
fun SelectTableItem(
    itemName: String,
    relatedList: List<String>,
    itemType: String,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onView: (String) -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    val dropdownItems = when (itemType) {
        "테이블" -> listOf("보기", "수정", "삭제")
        "뷰" -> listOf("보기", "삭제")
        else -> listOf("수정", "삭제")
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(40.dp))

            // 확장 아이콘: 항목이 있을 때만 표시
            if (relatedList.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable { onExpandToggle() },
                    painter = painterResource(
                        if (isExpanded) R.drawable.small_down else R.drawable.right_arrow
                    ),
                    contentDescription = "",
                    tint = TextColor
                )

                Spacer(Modifier.width(7.dp))
            }

            // 항목 이름과 관련 리스트 크기: 관련 리스트가 있을 때만 (0)을 표시
            Text(
                modifier = Modifier.clickable { isDropDownExpanded.value = true },
                text = "$itemName ${if (relatedList.isNotEmpty()) "(${relatedList.size})" else ""}",
                style = dropdownTextStyle()
            )

            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = { isDropDownExpanded.value = false }
            ) {
                dropdownItems.forEach { action ->
                    DropdownMenuItem(
                        text = { Text(action, style = dropdownTextStyle()) },
                        onClick = {
                            isDropDownExpanded.value = false
                            when (action) {
                                "보기" -> onView(itemName)
                                "수정" -> onEdit(itemName)
                                "삭제" -> onDelete(itemName)
                            }
                        }
                    )
                }
            }
        }

        // 확장된 항목을 표시
        if (isExpanded) {
            relatedList.forEach {
                Row {
                    Spacer(Modifier.width(70.dp))
                    Text(text = it, style = dropdownTextStyle())
                }
            }
        }
    }
}



/*
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
*/
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

