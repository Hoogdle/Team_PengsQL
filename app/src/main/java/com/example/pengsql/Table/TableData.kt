package com.example.pengsql.Table

// 데이터베이스 데이터 형식
//[
// ["id", "name", "address"]
//[1, "Luís", "Gonçalves"],
//[2, "Leonie", "Köhler"],
//[3, "François", "Tremblay"]
//]

//data class TableData(
//    val name: String,
//    val type: String,
//    val option1: Boolean = false,
//    val option2: Boolean = false,
//    val option3: Boolean = false,
//    val option4: Boolean = false,
//    val defaultValue: String,
//    val collation: String,
//    val foreignKey: String
//)
//
//val tableSample1 = TableData(
//    name = "Hi",
//    type = "String",
//    option2 = true,
//    defaultValue = "hello",
//    collation = "what",
//    foreignKey = "is it"
//)
//
//val tableSample2 = TableData(
//    name = "Hi",
//    type = "String",
//    option2 = true,
//    defaultValue = "hello",
//    collation = "what",
//    foreignKey = "is it"
//)
//
//val tableSample3 = TableData(
//    name = "Hi",
//    type = "String",
//    option2 = true,
//    defaultValue = "hello",
//    collation = "what",
//    foreignKey = "is it"
//)
//
val columnsTableData = listOf("Field Name", "Data Type", "NN", "PK", "AI", "U", "Default Value", "Collation", "Foreign Key")
val sample1 = listOf("Hi","String","true","true","false","true","hellohellohellohellohellohello","whathellohello","ishellohellohello ithellohellohellohellohellohello")

val tableDataSamples = listOf(columnsTableData,sample1, sample1, sample1,sample1,sample1,sample1,sample1,sample1, sample1, sample1,sample1,sample1,sample1,sample1)
val dropDownSample = listOf("apple","banana","mango","orange","watermelon")
//val sampleTableData = listOf(tableSample1, tableSample2, tableSample3)
