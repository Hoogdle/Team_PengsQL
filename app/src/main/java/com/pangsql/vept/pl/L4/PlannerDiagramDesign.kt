package com.pangsql.vept.pl.L4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pangsql.vept.R
import com.pangsql.vept.ui.theme.BackGroundColor
import com.pangsql.vept.ui.theme.ButtonColor
import com.pangsql.vept.ui.theme.ButtonTextColor
import com.pangsql.vept.ui.theme.TableBackGroundColor
import com.pangsql.vept.ui.theme.TitleColor


@Composable
fun MainDesign(
    viewModel: PlannerDiagramViewModel
){
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color(216,224,227)
    )
    val targetOffset = if (viewModel.getslid() == 1) {
        0.dp
    } else {
        400.dp
    }

    val offsetY by animateDpAsState(
        targetValue = targetOffset,
        animationSpec = tween(300),
        label = "offsetY"
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackGroundColor)) {
        Column(
            Modifier
                .fillMaxSize()
                .background(TableBackGroundColor)
        ) {
            //ArrowAndMenu()

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SelectDiagramTitle("Diagram")
                Button(
                    onClick = { viewModel.setslid(1) },
                    contentPadding = PaddingValues(7.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .padding(
                            start = 1.dp,
                            end = 1.dp
                        )
                        .offset(x= -10.dp)
                        .offset(y=3.dp)
                    ,
                    colors = ButtonColors(
                        contentColor = ButtonTextColor,
                        containerColor = ButtonColor,
                        disabledContainerColor = ButtonColor,
                        disabledContentColor = ButtonTextColor
                    ),
                    shape = RoundedCornerShape(
                        topStart = 5.dp,
                        topEnd = 5.dp
                    )
                ) {
                    Text("코드 편집기")
                }
            }
            DrawCanvas(viewModel)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = offsetY)
                .fillMaxSize()
                .imePadding()
                .background(Color.White.copy(alpha = 0.78f))
        ) {
            Test(viewModel)
        }
    }
}

@Composable
fun SelectDiagramTitle(
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
fun Test(
    viewModel: PlannerDiagramViewModel
){

    val context = LocalContext.current
    val textContent = remember { mutableStateOf(viewModel.getcode()) }
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val errors by viewModel.errors

    // SAF 파일 열기 Launcher
    val readLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                val fileName: String = getFileNameFromUri(context, uri)
                if (fileName.endsWith(".dbia") == true) {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val content = inputStream?.bufferedReader().use { it?.readText() ?: "" }
                    val hashlist = content.split('%')
                    val hashslist = hashlist[0].split('\n')
                    viewModel.resetHashPost()
                    hashslist.forEach { it ->
                        val ssmsg = it.split(':')
                        if(!ssmsg[0].equals(""))
                            viewModel.addHashPos(ssmsg[0], ssmsg[1].toFloat(),ssmsg[2].toFloat())
                    }
                    textContent.value = hashlist[1]
                    viewModel.setcode(hashlist[1])
                    viewModel.ontxtChange()
                }
            }
        }
    }

    // SAF 저장하기 Launcher
    val saveLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri: Uri? ->
        var poshash: String = "";
        viewModel.positionhash.map { it -> poshash += it.key + ":" + it.value.x + ":" + it.value.y + "\n" }
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write((poshash + "%" + textContent.value).toByteArray())
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .imePadding()
    ) {
        Row(modifier = Modifier
            .padding(top = 16.dp, bottom = 1.dp)
            .size(screenWidthDp, screenHeightDp * 4 / 5)
            .imePadding()) {

            // 텍스트 내용 입력/출력 필드
            BasicTextField(
                value = textContent.value,
                onValueChange = { textContent.value = it; viewModel.setcode(it); viewModel.ontxtChange() },
                textStyle = TextStyle(fontSize = 16.sp),
                visualTransformation = CodeVisual(errors),
                modifier = Modifier
                    .fillMaxSize(),
                decorationBox = { innerTextField ->
                    // 레이아웃이나 배경 등 추가 가능
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        innerTextField()
                    }
                }
            )
        }
        Row(modifier = Modifier
            .weight(1f)
            .padding(bottom = 1.dp)
            .fillMaxWidth()
            .imePadding()) {
            Button(
                onClick = { viewModel.setslid(0) },
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier
                    .height(30.dp)
                    .padding(
                        start = 5.dp,
                        end = 5.dp
                    )
                    .fillMaxWidth()
                    .weight(1f)
                ,
                colors = ButtonColors(
                    contentColor = ButtonTextColor,
                    containerColor = ButtonColor,
                    disabledContainerColor = ButtonColor,
                    disabledContentColor = ButtonTextColor
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("닫기")
            }
            // Read 버튼
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                    }
                    readLauncher.launch(intent) },
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier
                    .height(30.dp)
                    .padding(
                        start = 5.dp,
                        end = 5.dp
                    )
                    .fillMaxWidth()
                    .weight(1f)
                ,
                colors = ButtonColors(
                    contentColor = ButtonTextColor,
                    containerColor = ButtonColor,
                    disabledContainerColor = ButtonColor,
                    disabledContentColor = ButtonTextColor
                ),
                shape = RoundedCornerShape(10.dp)
            ) {Text("Read (.dbia)")}
            // Save 버튼
            Button(
                onClick = {saveLauncher.launch("saved_file.dbia") },
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier
                    .height(30.dp)
                    .padding(
                        start = 5.dp,
                        end = 5.dp
                    )
                    .fillMaxWidth()
                    .weight(1f)
                ,
                colors = ButtonColors(
                    contentColor = ButtonTextColor,
                    containerColor = ButtonColor,
                    disabledContainerColor = ButtonColor,
                    disabledContentColor = ButtonTextColor
                ),
                shape = RoundedCornerShape(10.dp)
            ) {Text("Save (.dbia)") }
        }
    }
}



fun getFileNameFromUri(
    context: Context,
    uri: Uri
): String {
    var result: String? = null
    var cursor: Cursor? = null

    try {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        cursor = context.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            result = cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return result!!
}

@Composable
fun DrawCanvas(viewModel: PlannerDiagramViewModel) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val mdiagrams = viewModel.dia
    val medges = viewModel.edg
    val textMeasurer = rememberTextMeasurer()
    var moveInd: Int = -1;
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { inoffset ->
                        run {
                            val getoff: Offset = Offset(
                                (inoffset.x - offset.x) / scale,
                                (inoffset.y - offset.y) / scale
                            )
                            mdiagrams.forEachIndexed { index, dia ->
                                run {
                                    if (dia.hitTest(getoff.x, getoff.y, 1f / 1f.toDp().value)) {
                                        viewModel.unselect()
                                        viewModel.selectDiagram(index)
                                        moveInd = index;
                                    }
                                }
                            }
                        }
                    },
                    onDoubleTap = {
                        scale = 1f;
                        offset = Offset.Zero;
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, Zoom, _ ->
                        val newScale = scale * Zoom
                        val scaleChange = newScale / scale

                        offset = (offset - centroid) * scaleChange + centroid + pan
                        scale = newScale
                    }
                )
            }
            .pointerInput("dragging") {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        if (moveInd != -1) {
                            val newOffset =
                                Offset(
                                    mdiagrams[moveInd].x.dp.toPx(),
                                    mdiagrams[moveInd].y.dp.toPx()
                                ) + dragAmount / scale
                            viewModel.moveDiagram(
                                moveInd,
                                newOffset.x.toDp().value,
                                newOffset.y.toDp().value
                            )
                        }
                    },
                    onDragEnd = {
                        moveInd = -1;
                        viewModel.unselect();
                    }
                )
            }
    ) {
        drawIntoCanvas { canvas ->
            canvas.withSave {
                canvas.translate(offset.x, offset.y)
                canvas.scale(scale, scale)

                val den: Float = 1f / 1f.toDp().value

                clipRect(
                    left = -offset.x / scale,
                    top = -offset.y / scale,
                    right = (size.width - offset.x) / scale,
                    bottom = (size.height - offset.y) / scale
                ) {
                    mdiagrams.forEach { dia ->
                        if(dia.width == 0f)
                            dia.calSize(textMeasurer, 1f / 1f.toDp().value)
                        if (dia.selected) {
                            drawRect(
                                color = Color.Cyan,
                                topLeft = Offset((dia.x - 5).dp.toPx(), (dia.y - 5).dp.toPx()),
                                size = Size(dia.width + 10f.dp.toPx(), dia.height + 10f.dp.toPx())
                            )
                        }
                        val offX: Float = (dia.x + 5) * den
                        val offY: Float = dia.y + 21
                        val offYLine: Float = dia.y + 20
                        drawRect(
                            color = ButtonColor,
                            topLeft = Offset(dia.x * den, dia.y * den),
                            size = Size(dia.width, dia.height)
                        )
                        drawRect(
                            color = Color.White,
                            topLeft = Offset((dia.x + 2) * den, (dia.y + 20) * den),
                            size = Size(dia.width - 4 * den, dia.height - 22 * den)
                        )
                        val meas = textMeasurer.measure(text = dia.name, style = TextStyle(color = ButtonTextColor), constraints = Constraints(maxWidth = Int.MAX_VALUE, maxHeight = Int.MAX_VALUE))
                        drawText(meas, topLeft = Offset(offX, (dia.y+1) * den))
                        dia.fields.forEachIndexed { ind, li ->
                            var leftside = li.name;
                            var rightside = li.type;
                            if(li.pk)
                                leftside += "(PK)"
                            if(li.nn)
                                rightside += "(NN)"
                            val limeaso = textMeasurer.measure(text = leftside, style = TextStyle(color = Color.Black), constraints = Constraints(maxWidth = Int.MAX_VALUE, maxHeight = Int.MAX_VALUE))
                            val limeast = textMeasurer.measure(text = rightside, style = TextStyle(color = Color.DarkGray), constraints = Constraints(maxWidth = Int.MAX_VALUE, maxHeight = Int.MAX_VALUE))
                            drawText(limeaso, topLeft = Offset(offX, (offY+ind*20) * den))
                            drawText(limeast, topLeft = Offset((dia.x - 5) * den + dia.width - textMeasurer.measure(rightside).size.width, (offY+ind*20) * den))
                            if(ind != 0)
                                drawLine(color = Color.Gray,
                                    start = Offset((dia.x + 2) * den, (offYLine+ind*20) * den),
                                    end = Offset((dia.x - 2) * den + dia.width, (offYLine+ind*20) * den),
                                    strokeWidth = 2f)
                        }
                    }
                    medges.forEach { sedg ->
                        val lines: List<Offset> = sedg.getLine(den, mdiagrams)
                        lines.windowed(2).forEach { (start, end) ->
                            drawLine(
                                color = ButtonColor,
                                start = start,
                                end = end,
                                strokeWidth = 4f
                            )
                        }
                    }
                }
            }
        }
    }
}