package com.example.vept.pl.L4

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.vept.R
import com.example.vept.ed.L4.DebugDropdown
import com.example.vept.ed.L4.SelectDBButtonPack
import com.example.vept.ed.L4.SelectDBTemplate
import com.example.vept.ed.L4.SelectDBTitle
import com.example.vept.ui.other.ArrowAndMenu
import com.example.vept.ui.theme.BackGroundColor
import com.example.vept.ui.theme.ButtonColor
import com.example.vept.ui.theme.ButtonTextColor
import com.example.vept.ui.theme.TitleColor
import kotlin.math.round

@Composable
fun MainDesign(
    viewModel: PlannerDiagramViewModel
){
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
            SelectDiagramTitle("Diagram")
        }
        DrawCanvas(viewModel)
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
fun DrawCanvas(viewModel: PlannerDiagramViewModel) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange * scale
    }
    val mdiagrams = viewModel.dia
    val textMeasurer = rememberTextMeasurer()
    var moveInd: Int = -1;
    Canvas(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .fillMaxSize()
            .background(Color.White)
            .transformable(state = state)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        run {
                            mdiagrams.forEachIndexed { index, dia ->
                                run {
                                    if (dia.hitTest(offset.x, offset.y, 1f / 1f.toDp().value)) {
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
            .pointerInput("dragging") {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        val sz: Int = mdiagrams.size - 1

                        if (moveInd != -1) {
                            val newOffset =
                                Offset(mdiagrams[moveInd].x.dp.toPx(), mdiagrams[moveInd].y.dp.toPx()) + dragAmount
                            viewModel.moveDiagram(moveInd, newOffset.x.toDp().value, newOffset.y.toDp().value)
                        }
                    },
                    onDragEnd = {
                        moveInd = -1;
                        viewModel.unselect();
                    }
                )
            }
            .onSizeChanged {

                //pointerOffset.value.SetOffset(Offset(it.width / 2f, it.height / 2f))
            },
        onDraw = {
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
                val den: Float = 1f / 1f.toDp().value
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
                drawText(textMeasurer, dia.name, topLeft = Offset(offX, (dia.y+1) * den), style = TextStyle(color = ButtonTextColor))
                dia.fields.forEachIndexed { ind, li ->
                    drawText(textMeasurer, li.name, topLeft = Offset(offX, (offY+ind*20) * den), style = TextStyle(color = Color.Black))
                    drawText(textMeasurer, li.type, topLeft = Offset((dia.x - 5) * den + dia.width - textMeasurer.measure(li.type).size.width, (offY+ind*20) * den), style = TextStyle(color = Color.DarkGray))
                    if(ind != 0)
                        drawLine(color = Color.Gray,
                            start = Offset((dia.x + 2) * den, (offYLine+ind*20) * den),
                            end = Offset((dia.x - 2) * den + dia.width, (offYLine+ind*20) * den),
                            strokeWidth = 2f)
                }
            }
        }
    )
}