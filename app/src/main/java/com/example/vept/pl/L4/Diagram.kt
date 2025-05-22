package com.example.vept.pl.L4

import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.vept.R
import org.w3c.dom.Text

data class Field(val name: String, val type: String)

data class Diagram(
    val name: String,
    val x: Float,
    val y: Float,
    val fields: List<Field>,
    var width: Float = 0f,
    var height: Float = 0f,
    val selected: Boolean = false
) {

    fun hitTest(testX: Float, testY: Float, den: Float): Boolean {
        return testX in x * den..(x * den + width) && testY in y * den..(y * den + height)
    }

    fun select(): Diagram = this.copy(selected = true)
    fun unselect(): Diagram = this.copy(selected = false)

    fun moveTo(newX: Float, newY: Float): Diagram = this.copy(x = newX, y = newY)

    fun calSize(meas: TextMeasurer, den: Float) {
        width = meas.measure(name).size.width.toFloat() + 10f * den
        height = 20f * (fields.count() + 1) * den
        for (i in fields) {
            val tmp: Float = meas.measure(i.name).size.width.toFloat() + meas.measure(i.type).size.width.toFloat() + 30f * den
            if(tmp > width) width = tmp;
        }
    }
}