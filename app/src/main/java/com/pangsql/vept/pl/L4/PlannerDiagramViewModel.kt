package com.pangsql.vept.pl.L4

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.pangsql.vept.pl.L4.Compiler.PlannerCompiler

class PlannerDiagramViewModel : ViewModel() {
    private var lastselected: Int = -1
    private var slid = mutableIntStateOf(0)
    private var code = initcode();
    var edg = mutableStateListOf<DiagramEdge>()
        private set
    var dia = mutableStateListOf<Diagram>()
        private set
    private val _errors = mutableStateOf<List<Int>>(emptyList())
    val errors: State<List<Int>> get() = _errors
    private var txtchanged: Boolean = false;
    private var changecount: Int = -1;
    private val compiler: PlannerCompiler = PlannerCompiler(this)
    val positionhash = mutableMapOf<String, Offset>();
    private val lock = Any()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val RunCompile = kotlinx.coroutines.Runnable {
        CompileCode(code)
    }

    val th = Thread {
        while(true) {
            synchronized(lock) {
                if(txtchanged) {
                    changecount = 20
                    txtchanged = false;
                } else {
                    changecount -= 1
                    if(changecount == 0) {
                        mainHandler.post(RunCompile)
                    }
                }
            }
            Thread.sleep(100)
        }
    }

    init {
        compiler.Compile(code)
        th.start()
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

    fun ResetError() {
        _errors.value = listOf()
    }

    fun adderror(ind: Int) {
        _errors.value += ind
    }

    fun addDiagram(name: String, x: Float, y: Float, ls: List<Field>) {
        val tmp = Diagram(name = name, x = positionhash[name]?.x ?: x, y = positionhash[name]?.y ?: y, ls)
        dia.add(tmp)
        positionhash[tmp.name] = Offset(tmp.x, tmp.y);
    }

    fun resetHashPost() {
        positionhash.clear()
    }

    fun addHashPos(name: String, x: Float, y: Float) {
        positionhash[name] = Offset(x, y);
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

    fun initcode(): String {
        return "table Hello { \n" +
                "\tINTEGER ID1 () { PK }\n" +
                "\tINTEGER ID2 () { NN, DF = \"50\" }\n" +
                "}\n" +
                "\n" +
                "table World {\n" +
                "\tINTEGER ID1 () { PK }\n" +
                "\tINTEGER FK1 (Hello.ID1) { }\n" +
                "\tINTEGER ID3 () { DF = \"50\" }\n" +
                "}"
    }

    fun getslid(): Int {
        return slid.intValue
    }

    fun setslid(i: Int) {
        slid.intValue = i
    }

    fun getcode(): String {
        return code
    }

    fun setcode(cd: String) {
        code = cd
    }

    fun ontxtChange() {
        txtchanged = true
    }
}