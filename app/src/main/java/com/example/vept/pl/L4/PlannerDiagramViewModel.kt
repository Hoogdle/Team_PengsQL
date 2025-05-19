package com.example.vept.pl.L4

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PlannerDiagramViewModel : ViewModel() {
    var lastselected: Int = -1
    var dia = mutableStateListOf<Diagram>()
        private set
    init {
        addDiagram("김수한무거북이와두루미", 0f,0f, listOf(Field("id", "Int"),Field("ID", "Int"),Field("ID", "Int")))
        addDiagram("Hello1", 100f,0f, ArrayList())
        addDiagram("Hello2", 200f,0f, ArrayList())
        addDiagram("Hello3", 300f,0f, ArrayList())
        addDiagram("Hello4", 400f,0f, ArrayList())
    }

    fun addDiagram(name: String, x: Float, y: Float, ls: List<Field>) {
        dia.add(Diagram(name = name, x = x, y = y, ls))
    }

    fun moveDiagram(index: Int, newX: Float, newY: Float) {
        dia[index] = dia[index].moveTo(newX, newY)
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
}