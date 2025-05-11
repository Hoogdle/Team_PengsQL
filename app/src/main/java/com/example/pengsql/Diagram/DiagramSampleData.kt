package com.example.pengsql.Diagram

data class DiagramTableData(
    val title: String,
    val contents: Map<String,String>
)

val diagramSampleData = DiagramTableData(
    title = "Pokemon",
    mapOf(
        "monster1" to "String",
        "monster2" to "String",
        "monster3" to "String"
    )

)
