package com.example.vept.pl.L4.Compiler

object TempCode {
    val code: String =
        "table Hello { \n" +
                "\tINTEGER ABCD() {PK,NN}\n" +
                "\tINTEGER BCD(Hello5.ABCD) {}\n" +
                "\tINTEGER CCD() {}\n" +
                "\tINTEGER DCD() {}\n" +
                "\tINTEGER GCD() {}\n" +
                "\tINTEGER FCD() {}\n" +
                "\tINTEGER ECD() {}\n" +
                "}\n" +
                "\n" +
                "table Hello3 { \n" +
                "\tINTEGER ABCD() {PK}\n" +
                "\tINTEGER BCD(Hello.ABCD) {}\n" +
                "\tINTEGER CCD() {}\n" +
                "\tINTEGER DCD() {}\n" +
                "\tINTEGER GCD() {}\n" +
                "\tINTEGER FCD() {}\n" +
                "\tINTEGER ECD() {}\n" +
                "}\n" +
                "\n" +
                "table Hello5 { \n" +
                "\tINTEGER ABCD() {PK}\n" +
                "\tINTEGER BCD() {}\n" +
                "\tINTEGER CCD() {}\n" +
                "\tINTEGER DCD() {}\n" +
                "\tINTEGER GCD() {}\n" +
                "\tINTEGER FCD() {}\n" +
                "\tINTEGER ECD() {}\n" +
                "}\n" +
                "\n" +
                "table Hello2 {\n" +
                "\tINTEGER AAAA() {PK}\n" +
                "}"
}