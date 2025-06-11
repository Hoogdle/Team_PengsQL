package com.pangsql.vept.pl.L4

import androidx.compose.ui.text.TextMeasurer

data class Field(val name: String, val type: String, val pk: Boolean = false, val nn: Boolean = false, val df: Any = 0)

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
            var leftside = i.name;
            var rightside = i.type;
            if(i.pk) leftside += "(PK)"
            if(i.nn) rightside += "(NN)"
            val tmp: Float = meas.measure(leftside).size.width.toFloat() + meas.measure(rightside).size.width.toFloat() + 30f * den
            if(tmp > width) width = tmp;
        }
    }
}