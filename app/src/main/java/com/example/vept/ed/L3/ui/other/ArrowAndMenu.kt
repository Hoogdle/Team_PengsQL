package com.example.vept.ui.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vept.R
import com.example.vept.ed.L4.CLIButtonPack
import com.example.vept.ed.L4.DebugDropdown
import com.example.vept.ed.L4.EditMainButtonPack
import com.example.vept.ed.L4.EditSqlCliViewModel
import com.example.vept.ed.L4.ReturnToMainButton
import com.example.vept.ed.L4.SelectDBTitle

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

@Composable
fun ArrowAndMenuWithTitle(
    title: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 40.dp,
                end = 40.dp,
                bottom = 10.dp
            ),
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                tint = Color.Black,
                contentDescription = "",
            )
            Modifier.width(10.dp)
            SelectDBTitle(title)
        }

        Row {
            Modifier.width(25.dp)
            EditMainButtonPack(navController = navController)
            DebugDropdown(navController = navController)
            ReturnToMainButton(navController)       //<<===디버그용===!!//
        }
    }
}

@Composable
fun ArrowAndMenuCLI(
    title: String,
    navController: NavHostController,
    viewModel: EditSqlCliViewModel,
    textState: MutableList<MutableState<String>>,
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 40.dp,
                end = 40.dp,
                bottom = 10.dp
            ),
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                tint = Color.Black,
                contentDescription = "",
            )
            Modifier.width(10.dp)
            SelectDBTitle(title)
            Modifier.width(25.dp)
        }
        CLIButtonPack(viewModel = viewModel, textState = textState)

    }
}