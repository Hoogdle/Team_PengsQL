package com.example.vept.ed.L4

class whereerror {



}



//    var dbTableData:  MutableState<MutableList<List<String>>> = remember { mutableStateOf(mutableListOf<List<String>>()) }
//
//    val liveData = viewModel.getTablePageData(itemName, currentPage.value.toInt(), itemsPerPage)
//    val liveDataState by liveData.observeAsState(initial = emptyList())
//
//    val rowCountLive = viewModel.getRowCount(itemName)
//    val rowCount by rowCountLive.observeAsState(initial = 0)
//
//    val fieldNamesLive = viewModel.getFieldNames(itemName)
//    val fieldNames by fieldNamesLive.observeAsState(initial = emptyList())
//    LaunchedEffect(rowCount) {
//        maximumPage.value = ((rowCount - 1) / itemsPerPage + 1).toString()
//        dbTableData.value = liveDataState.toMutableList()
//    }

//@Composable
//fun VerticalDividers(
//    modifier: Modifier = Modifier,
//    thickness: Dp = DividerDefaults.Thickness,
//    color: Color = DividerDefaults.color,
//) {
//    Row() {
//        Canvas(
//            modifier
//                .fillMaxHeight()
//                .width(thickness)
//        ) {
//            drawLine(
//                color = color,
//                strokeWidth = thickness.toPx(),
//                start = Offset(thickness.toPx() / 2, 0f),
//                end = Offset(thickness.toPx() / 2, size.height),
//            )
//        }
//    }
//}

//
//
//@Composable
//fun DBTableTextField(   //검색 바(search bar)
//){
//    val input = remember { mutableStateOf("") }
//    Box(
//        modifier = Modifier
//            .offset(
//                x=-50.dp,
//                y=-4.dp
//            )
//    ){
//        Column(){
//            Spacer(Modifier.height(30.dp))
//            Icon(
//                painter = painterResource(R.drawable.search),
//                contentDescription = "",
//                tint = TitleColor
//            )
//            Icon(
//                painter = painterResource(R.drawable.textfiled_line),
//                contentDescription = "",
//                tint = TitleColor
//            )
//        }
//        TextField(
//            maxLines = 1,
//            modifier = Modifier
//                .offset(
//                    x = 12.dp,
//                    y = 15.dp
//                )
//                .width(200.dp)
//                .height(50.dp),
//            textStyle = TextStyle(
//                fontSize = 14.sp
//            ),
//            value = input.value,
//            onValueChange = {input.value = it},
//            colors = TextFieldDefaults.colors(
//                focusedTextColor = TextColor,
//                unfocusedTextColor = TextColor,
//                focusedIndicatorColor = TextColor,
//                unfocusedIndicatorColor = TextColor,
//                cursorColor = TextColor,
//                unfocusedContainerColor = Color.Transparent,
//                focusedContainerColor = Color.Transparent,
//                errorContainerColor = Color.Transparent,
//                disabledContainerColor = Color.Transparent
//            )
//        )
//    }
//}
//
//
//fun DBTableNumberGenerator( // 데이터 넘버링 생성기
//    start: Int
//) : MutableList<String> {
//    val numberList:MutableList<String> = mutableListOf()
//    numberList.add("")
//    numberList.add("")
//    for(i in start until start+itemsPerPage){
//        numberList.add(i.toString())
//    }
//    return numberList
//}
//
//inline fun <T> LiveData<T>.observeOnce( //객체 생성 방식
//    lifecycleOwner: LifecycleOwner,
//    crossinline onChanged: (T) -> Unit
//) {
//    observe(lifecycleOwner, object : Observer<T> {
//        override fun onChanged(value: T) {
//            onChanged(value)
//            removeObserver(this)
//        }
//    })
//}
//
//fun loadDataForPage(                    // 업데이트 정도
//    pageStr: String,
//    data: MutableState<MutableList<List<String>>>,
//    viewModel: EditTableListViewModel,
//    lifecycleOwner: LifecycleOwner
//) {
//    val page = pageStr.toIntOrNull() ?: 1
//    val itemName = viewModel.getItemName()
//
//    viewModel.getTablePageData(itemName, page, itemsPerPage).observeOnce(lifecycleOwner) { newData ->
//        if (newData != null) {
//            data.value = newData.toMutableList()
//        }
//    }
//}
//





//
//    @Composable
//fun DBTableTemplate(    // 테이블
//    data: MutableState<MutableList<List<String>>>,
//    fieldNames: List<String>,
//    currentPage: MutableState<String>,
//    maximumPage: MutableState<String>,
//    viewModel: EditTableListViewModel,
//    isEditable: Boolean
//) {
//    val verticalScrollState = rememberScrollState()
//    val horizontalScrollState = rememberScrollState()
//
//    // 페이지 변경 시 데이터를 갱신하는 로직
//    val rowStart = remember(currentPage.value) {
//        val startNum = 1
//        startNum + ((currentPage.value.toIntOrNull() ?: 1) - 1) * itemsPerPage
//    }
//    val numList = remember(currentPage.value) { DBTableNumberGenerator(rowStart) }
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val triggerPageUpdate = remember { mutableStateOf(false) }
//
//    // 페이지 변경 시 데이터를 로드
//    LaunchedEffect(currentPage.value) {
//        delay(275)
//        loadDataForPage(currentPage.value, data, viewModel, lifecycleOwner)
//    }
//    // 트리거 값이 바뀌었을 때 페이지 데이터를 갱신
//    LaunchedEffect(currentPage.value, triggerPageUpdate.value) {
//        if (triggerPageUpdate.value) {
//            loadDataForPage(currentPage.value, data, viewModel, lifecycleOwner)
//            triggerPageUpdate.value = false
//        }
//    }
//    // 데이터를 현재 페이지에 맞게 업데이트
//    val columnData = remember(data.value, fieldNames, currentPage.value) {
//        val result = mutableListOf<List<String>>()
//        if (data.value.isNotEmpty()) {
//            val columnCount = data.value[0].size
//            for (i in 0 until columnCount) {
//                val column = mutableListOf<String>()
//                column.add(fieldNames.getOrNull(i) ?: "") // 컬럼명 삽입
//                column.addAll(data.value.map { it[i] })
//                result.add(column)
//            }
//        }
//        result
//    }
//    // 페이지 위치에 맞춰 넘버링 조정
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(BackGroundColor)
//            .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
//            .background(TableBackGroundColor)
//            .padding(horizontal = 30.dp)
//            .verticalScroll(verticalScrollState)
//            .horizontalScroll(horizontalScrollState)
//    ) {
//        Row {
//            // 넘버링 출력
//            DBTableNumber(numList)
//            Box(
//                modifier = Modifier.height((38.07f * (data.value.size + 1)).dp)
//            ) {
//                VerticalDividers(
//                    modifier = Modifier.fillMaxHeight(),
//                    thickness = 1.dp,
//                    color = Color.LightGray
//                )
//            }
//
//            // 열 출력
//            columnData.forEachIndexed { index, column ->
//                if (index == 0) {
//                    DBTableId(column)
//                } else {
//                    DBTableText(
//                        column = column,
//                        isEditable = isEditable,
//                        rowDataList = data.value,
//                        columnIndex = index,
//                        fieldNames = fieldNames,
//                        viewModel = viewModel,
//                        currentPage = currentPage,
//                        triggerPageUpdate = triggerPageUpdate
//                    )
//                }
//                // 세로선 출력
//                if (index != columnData.lastIndex) {
//                    Box(
//                        modifier = Modifier.height((38.07f * (data.value.size + 1)).dp)
//                    ) {
//                        VerticalDividers(
//                            modifier = Modifier.fillMaxHeight(),
//                            thickness = 1.dp,
//                            color = Color.LightGray
//                        )
//                    }
//                }
//            }
//        }
//        // 페이지 네비게이션 컨트롤러
//
//    }
//}





//
//@Composable
//fun DBTableNumber(      //
//    data: List<String>
//){
//    Column(){
//        val mostLong = data.maxOf{it.length}
//        data.forEachIndexed { index, item ->
//            // empty part
//            // list of number는 "","",1,2,3,4,5,... 형태로 나아감
//            // 즉 0번째, 1번째 인덱스는 빈 문자열
//            if(index == 0 || index == 1){
//                Text(
//                    modifier = Modifier
//                        .padding(
//                            start = 10.dp,
//                            top = 10.dp,
//                            bottom = 10.dp
//                        )
//                        .clickable {
//                            /* fun() */
//                        }
//                    ,
//                    text = item,
//                    textAlign = TextAlign.Start,
//                    style = TextStyle(
//                        color = TitleColor,
//                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
//                        fontSize = 14.sp
//                    ),
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1
//                )
//                HorizontalDivider(
//                    modifier = Modifier.width(mostLong.dp*15),
//                    thickness = 1.dp,
//                    color = Color.LightGray
//                )
//            } else {
//                // non-header part
//                Column {
//                    Text(
//                        modifier = Modifier
//                            .padding(
//                                start = 10.dp,
//                                top = 10.dp,
//                                bottom = 10.dp
//                            )
//                        ,
//                        text = item,
//                        textAlign = TextAlign.Start,
//                        style = TextStyle(
//                            color = TitleColor,
//                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                            fontSize = 14.sp
//                        ),
//                        overflow = TextOverflow.Ellipsis,
//                        maxLines = 1
//                    )
//                    HorizontalDivider(
//                        modifier = Modifier.width(mostLong.dp*15),
//                        thickness = 1.dp,
//                        color = Color.LightGray
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DBTableId(          //
//    data: List<String>
//){
//    Column() {
//        val mostLong = data.maxOf { it.length }
//        data.forEachIndexed { index, item ->
//            // empty part
//            // list of number는 "","",1,2,3,4,5,... 형태로 나아감
//            // 즉 0번째, 1번째 인덱스는 빈 문자열
//            if (index == 0) {
//                Text(
//                    modifier = Modifier
//                        .padding(
//                            start = 10.dp,
//                            top = 10.dp,
//                            bottom = 10.dp
//                        )
//                        .clickable {
//                            /* fun() */
//                        },
//                    text = item,
//                    textAlign = TextAlign.Start,
//                    style = TextStyle(
//                        color = TitleColor,
//                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
//                        fontSize = 14.sp
//                    ),
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1
//                )
//                HorizontalDivider(
//                    modifier = Modifier.width(mostLong.dp * 12),
//                    thickness = 1.dp,
//                    color = Color.LightGray
//                )
//                Text(
//                    modifier = Modifier
//                        .padding(
//                            start = 10.dp,
//                            top = 10.dp,
//                            bottom = 10.dp
//                        )
//                        .clickable {
//                            /* fun() */
//                        },
//                    text = "",
//                    textAlign = TextAlign.Start,
//                    style = TextStyle(
//                        color = TitleColor,
//                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
//                        fontSize = 14.sp
//                    ),
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1
//                )
//                HorizontalDivider(
//                    modifier = Modifier.width(mostLong.dp * 12),
//                    thickness = 1.dp,
//                    color = Color.LightGray
//                )
//            } else {
//                // non-header part
//                Column {
//                    Text(
//                        modifier = Modifier
//                            .padding(
//                                start = 10.dp,
//                                top = 10.dp,
//                                bottom = 10.dp
//                            ),
//                        text = item,
//                        textAlign = TextAlign.Start,
//                        style = TextStyle(
//                            color = TextColor,
//                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                            fontSize = 14.sp
//                        ),
//                        overflow = TextOverflow.Ellipsis,
//                        maxLines = 1
//                    )
//                    HorizontalDivider(
//                        modifier = Modifier.width(mostLong.dp * 12),
//                        thickness = 1.dp,
//                        color = Color.LightGray
//                    )
//                }
//            }
//        }
//    }
//}

//
//@Composable
//fun DBTableText(
//    column: List<String>,
//    isEditable: Boolean,
//    rowDataList: List<List<String>>,
//    columnIndex: Int,
//    fieldNames: List<String>,
//    viewModel: EditTableListViewModel,
//    currentPage: MutableState<String>,
//    triggerPageUpdate: MutableState<Boolean>
//
//) {
//    val filterInput = remember { mutableStateOf("") }
//    val displayData = remember { mutableStateListOf<String>() }
//    val editingIndex = remember { mutableStateOf<Int?>(null) }
//
//    // data 갱신 감지
//    LaunchedEffect(column) {
//        displayData.clear()
//        displayData.addAll(column)
//    }
//
//    val mostLong = displayData.maxOfOrNull { it.length }?.coerceAtLeast(6) ?: 6
//    val cellWidth = mostLong.dp * 12
//
//    Column {
//        displayData.forEachIndexed { rowIndex, cellText ->
//            if (rowIndex == 0) {
//                // 헤더
//                HeaderCell(cellText, cellWidth, filterInput)
//            } else {
//                if (isEditable) {
//                    EditableCell(
//                        text = column[rowIndex],
//                        rowData = rowDataList[rowIndex - 1],
//                        columnName = fieldNames[columnIndex],
//                        fieldNames = fieldNames,
//                        width = cellWidth,
//                        index = rowIndex,
//                        editingIndex = editingIndex,
//                        viewModel = viewModel,
//                        currentPage = currentPage,
//                        triggerPageUpdate = triggerPageUpdate
//                    )
//                } else {
//                    ReadOnlyCell(cellText, cellWidth)
//                }
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HeaderCell(
//    title: String,
//    width: Dp,
//    input: MutableState<String>
//) {
//    Text(
//        modifier = Modifier
//            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
//        text = title,
//        textAlign = TextAlign.Start,
//        style = TextStyle(
//            color = TitleColor,
//            fontFamily = FontFamily(Font(R.font.roboto_bold)),
//            fontSize = 14.sp
//        ),
//        overflow = TextOverflow.Ellipsis,
//        maxLines = 1
//    )
//
//    HorizontalDivider(modifier = Modifier.width(width), thickness = 1.dp, color = Color.LightGray)
//
//    BasicTextField(
//        value = input.value,
//        onValueChange = { input.value = it },
//        modifier = Modifier
//            .width(width)
//            .height(55.dp)
//            .zIndex(1f)
//            .offset(x = -4.dp, y = -8.dp),
//        singleLine = true,
//        textStyle = TextStyle(fontSize = 14.sp),
//        decorationBox = @Composable{ innerTextField ->
//            TextFieldDefaults.DecorationBox(
//                placeholder = {
//                    Text(
//                        text = "Filter",
//                        color = Color.LightGray,
//                        style = TextStyle(
//                            color = TitleColor,
//                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                            fontSize = 14.sp
//                        ),
//                    )
//                },
//                singleLine = true,
//                visualTransformation = VisualTransformation.None,
//                enabled = true,
//                innerTextField = innerTextField,
//                value = input.value,
//                interactionSource = remember { MutableInteractionSource() },
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = TextColor,
//                    unfocusedTextColor = TextColor,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    cursorColor = TextColor,
//                    unfocusedContainerColor = Color.Transparent,
//                    focusedContainerColor = Color.Transparent,
//                    errorContainerColor = Color.Transparent,
//                    disabledContainerColor = Color.Transparent
//                )
//            )
//        }
//    )
//    HorizontalDivider(modifier = Modifier
//        .width(width)
//        .offset(y = -18.5.dp), thickness = 1.dp, color = Color.LightGray)
//}
//
//@Composable
//fun EditableCell(
//    text: String,
//    rowData: List<String>,
//    columnName: String,
//    fieldNames: List<String>,
//    width: Dp,
//    index: Int,
//    editingIndex: MutableState<Int?>,
//    viewModel: EditTableListViewModel,
//    currentPage: MutableState<String>,
//    triggerPageUpdate: MutableState<Boolean>
//) {
//    val isEditing = editingIndex.value == index
//    val showDialog = remember { mutableStateOf(false) }
//    var localValue = text
//
//    if (isEditing && showDialog.value) {
//        AlertDialog(
//            onDismissRequest = {
//                showDialog.value = false
//                editingIndex.value = null
//            },
//            title = { Text("셀 수정") },
//            text = {
//                var tempValue by remember { mutableStateOf(localValue) } // 이건 임시 입력값 용
//                Column {
//                    Text("변경할 값을 입력하세요:")
//                    Spacer(Modifier.height(8.dp))
//                    TextField(
//                        value = tempValue,
//                        onValueChange = { tempValue = it },
//                        singleLine = true
//                    )
//                }
//                localValue = tempValue // 최신 입력값으로 반영
//            },
//            confirmButton = {
//                TextButton(onClick = {
//                    showDialog.value = false
//                    editingIndex.value = null
//                    if (localValue != text) {
//                        viewModel.updateRowBySnapshot(
//                            rowData,
//                            fieldNames,
//                            columnName,
//                            localValue
//                        )
//                        val originalPage = currentPage.value
//                        currentPage.value = "-1"
//                        currentPage.value = originalPage
//
//                        triggerPageUpdate.value = true
//                    }
//                }) {
//                    Text("수정하기")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = {
//                    showDialog.value = false
//                    editingIndex.value = null
//                }) {
//                    Text("취소")
//                }
//            }
//        )
//    }
//    Column(
//        modifier = Modifier
//            .offset(y = -18.5.dp)
//            .pointerInput(Unit) {
//                detectTapGestures(onDoubleTap = {
//                    editingIndex.value = index
//                    showDialog.value = true
//                })
//            }
//    ) {
//        Text(
//            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
//            text = text,
//            textAlign = TextAlign.Start,
//            style = TextStyle(
//                color = if (isEditing) Color.Blue else TextColor,
//                fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                fontSize = 14.sp
//            ),
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1
//        )
//        HorizontalDivider(Modifier.width(width), thickness = 1.dp, color = Color.LightGray)
//    }
//}
//
//@Composable
//fun ReadOnlyCell(value: String, width: Dp) { //Log.e("임시","뷰")
//    Column(modifier = Modifier.offset(y = -18.5.dp)) {
//        Text(
//            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
//            text = value,
//            textAlign = TextAlign.Start,
//            style = TextStyle(
//                color = TextColor,
//                fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                fontSize = 14.sp
//            ),
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1
//        )
//        HorizontalDivider(modifier = Modifier.width(width), thickness = 1.dp, color = Color.LightGray)
//    }
//}
//
//

