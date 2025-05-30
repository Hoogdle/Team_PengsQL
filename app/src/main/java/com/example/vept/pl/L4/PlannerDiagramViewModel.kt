package com.example.vept.pl.L4

import android.graphics.Point
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.vept.pl.L4.Compiler.PlannerCompiler
import com.example.vept.pl.L4.Compiler.TempCode

class PlannerDiagramViewModel : ViewModel() {
    var lastselected: Int = -1
    var edg = mutableStateListOf<DiagramEdge>()
        private set
    var dia = mutableStateListOf<Diagram>()
        private set
    val compiler: PlannerCompiler = PlannerCompiler(this)
    val positionhash = mutableMapOf<String, Offset>();
    init {
        compiler.Compile(TempCode.code)
        //edg.add(DiagramEdge(0, 1,0,0))
    }

    fun CompileCode(code : String) {
        compiler.Compile(code)
    }

    fun ResetDiagram() {
        dia.removeAll { true }
        edg.removeAll { true }
        lastselected = -1
    }

    fun addDiagram(name: String, x: Float, y: Float, ls: List<Field>) {
        val tmp = Diagram(name = name, x = positionhash[name]?.x ?: x, y = positionhash[name]?.y ?: y, ls)
        dia.add(tmp)
        positionhash[tmp.name] = Offset(tmp.x, tmp.y);
    }

    fun moveDiagram(index: Int, newX: Float, newY: Float) {
        dia[index] = dia[index].moveTo(newX, newY)
        positionhash[dia[index].name] = Offset(newX, newY);
    }

    fun selectDiagram(index: Int) {
        dia[index] = dia[index].select()
        lastselected = index;
    }

    fun unselect() {
        if(lastselected != -1) {
            dia[lastselected] = dia[lastselected].unselect()
            lastselected = -1;
        }
    }

    fun AddForienKey(FromDia: Int, ToDia: Int, FromField: Int, ToField: Int) {
        edg.add(DiagramEdge(FromDia,ToDia,FromField,ToField))
    }

    fun getDiagram(ind: Int): Diagram {
        return dia[ind]
    }
}