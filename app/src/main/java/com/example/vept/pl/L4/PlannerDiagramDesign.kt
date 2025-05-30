package com.example.vept.pl.L4

import android.graphics.Matrix
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.scale
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
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
                            val getoff: Offset = Offset((inoffset.x - offset.x) / scale,(inoffset.y - offset.y) / scale)
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
                                Offset(mdiagrams[moveInd].x.dp.toPx(), mdiagrams[moveInd].y.dp.toPx()) + dragAmount / scale
                            viewModel.moveDiagram(moveInd, newOffset.x.toDp().value, newOffset.y.toDp().value)
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