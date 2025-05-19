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
import androidx.compose.runtime.LaunchedEffect
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
    LaunchedEffect(Unit) {
        viewModel.refreshAllData()
    }


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
    val shouldExpand = items.isNotEmpty()

    Column {
        Row(
            Modifier
                .clickable { expandedMap[headerName] = !isSectionExpanded }
                .padding(vertical = 8.dp)
        ) {
            if (shouldExpand) {
                Icon(
                    painter = painterResource(
                        if (isSectionExpanded) R.drawable.small_down else R.drawable.right_arrow
                    ),
                    contentDescription = "",
                    tint = TextColor
                )
            }
            TextHeader(headName = headerName, size = items.size)
        }
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

