package com.example.vept.ui.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vept.R

@Composable
fun ArrowAndMenu(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 40.dp,
                end = 40.dp,
                bottom = 5.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            tint = Color.Black,
            contentDescription = "",
        )
        Icon(
            painter = painterResource(R.drawable.menu),
            tint = Color.Black,
            contentDescription = "",
        )
    }
}