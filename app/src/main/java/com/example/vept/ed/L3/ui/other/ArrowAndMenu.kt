package com.example.vept.ui.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

// 단일 화살표
@Composable
fun ArrowAndTitle(
    navController: NavHostController,
    title: String = "",
    destination: String = ""
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 40.dp,
                end = 40.dp,
                bottom = 5.dp
            ),
    ) {
        Icon(
            modifier = Modifier.size(25.dp).clickable { if(destination != "") navController.navigate(destination)},
            painter = painterResource(R.drawable.arrow_back),
            tint = Color.Black,
            contentDescription = "",
        )
        SelectDBTitle(title)
    }
}

@Composable
fun ArrowAndMenuWithTitle(
    title: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    destination: String = ""
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
                modifier = Modifier.size(25.dp).clickable { if(destination != "") navController.navigate(destination)},
                painter = painterResource(R.drawable.arrow_back),
                tint = Color.Black,
                contentDescription = "",
            )
            SelectDBTitle(title)
        }

        Row {
            Modifier.width(25.dp)
            EditMainButtonPack(title, navController = navController)
            Button(
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .offset()
                ,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonColors(
                    contentColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                onClick = {
                    navController.navigate("ai")
                }
            ) {
                Image(
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(R.drawable.gemma),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun ArrowAndMenuCLI(
    title: String,
    navController: NavHostController,
    viewModel: EditSqlCliViewModel,
    textState: MutableList<MutableState<String>>,
    destination: String
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
                modifier = Modifier.size(25.dp).clickable { if(destination != "") navController.navigate(destination)},
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