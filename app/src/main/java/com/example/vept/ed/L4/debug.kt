package com.example.vept.ed.L4

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable //sql, list, mod -> main 이동
fun ReturnToMainButton(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Button(
            onClick = { navController.navigate("main") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(100.dp)
                .height(36.dp),
            shape = MaterialTheme.shapes.medium // 사각형 버튼
        ) {
            Text("메인", fontSize = 12.sp)
        }
    }
}


@Composable //main -> sql, list, mod 이동
fun DebugDropdown(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp),
                shape = MaterialTheme.shapes.medium // 사각형 버튼
            ) {
                Text("디버그", fontSize = 12.sp)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("sqlcli") },
                    onClick = {
                        navController.navigate("sql")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Table list") },
                    onClick = {
                        navController.navigate("list")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Table mod") },
                    onClick = {
                        // 새 테이블 추가 모드로 진입 (itemName 없음)
                        navController.navigate("mod?name=&type=테이블")
                        expanded = false
                    }
                )
            }
        }
    }
}
