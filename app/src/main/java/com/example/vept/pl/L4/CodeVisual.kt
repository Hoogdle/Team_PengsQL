package com.example.vept.pl.L4

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

class CodeVisual(val errors: List<Int>) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val lines = text.lines()
        var eind = 0;

        val annotated = buildAnnotatedString {
            val errsize = errors.size
            val redcolor = Color.Red.copy(0.48f)
            for ((i, line) in lines.withIndex()) {
                if (errsize != eind && i == errors[eind]) {
                    eind += 1;
                    withStyle(style = SpanStyle(background = redcolor)) {
                        append(line)
                    }
                } else {
                    append(line)
                }
                if (i != lines.lastIndex) append("\n")
            }
        }

        return TransformedText(annotated, OffsetMapping.Identity)
    }
}