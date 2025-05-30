package com.example.vept.pl.L4

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope

data class DiagramEdge(
    val FromDia: Int,
    val ToDia: Int,
    val FromInd: Int,
    val ToInd: Int
) {
    fun getLine(den: Float, dias: List<Diagram>): List<Offset> {
        val Points: MutableList<Offset> = ArrayList();
        val FromX: Float = dias[FromDia].x * den
        val FromY: Float = (dias[FromDia].y + 30f + 20f * FromInd) * den
        val ToX: Float = dias[ToDia].x * den
        val ToY: Float = (dias[ToDia].y + 30f + 20f * ToInd) * den
        if (FromX + dias[FromDia].width < ToX) {
            val aver = (FromX + dias[FromDia].width + ToX) * 0.5f
            Points.add(Offset(FromX + dias[FromDia].width, FromY))
            Points.add(Offset(aver, FromY))
            Points.add(Offset(aver, ToY))
            Points.add(Offset(ToX, ToY))
        } else if(ToX + dias[ToDia].width < FromX) {
            val aver = (ToX + dias[ToDia].width + FromX) * 0.5f
            Points.add(Offset(ToX + dias[ToDia].width, ToY))
            Points.add(Offset(aver, ToY))
            Points.add(Offset(aver, FromY))
            Points.add(Offset(FromX, FromY))
        } else if(FromX + dias[FromDia].width < ToX + dias[ToDia].width) {
            Points.add(Offset(FromX + dias[FromDia].width, FromY))
            Points.add(Offset(ToX + dias[ToDia].width + 10f, FromY))
            Points.add(Offset(ToX + dias[ToDia].width + 10f, ToY))
            Points.add(Offset(ToX + dias[ToDia].width, ToY))
        } else {
            Points.add(Offset(FromX + dias[FromDia].width, FromY))
            Points.add(Offset(FromX + dias[FromDia].width + 10f, FromY))
            Points.add(Offset(FromX + dias[FromDia].width + 10f, ToY))
            Points.add(Offset(ToX + dias[ToDia].width, ToY))
        }
        return Points;
    }
}