package com.example.pengsql.Navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pengsql.SelectDB.SelectDB
import com.example.pengsql.Table.Table

@Composable
fun PageNav(){

    // dependecy 추가
    //implementation(libs.androidx.navigation.compose)

    // 아래의 변수로 화면간 이동(네비게이션)
    // ex) navController.home
    val navController = rememberNavController()

    // 또한 데이터를 remember 형태로 전달하면, A화면의 데이터를 B화면에서도 수정가능

    // 메인 화면에는 아래의 화면이 뿌려지게 되고,
    // NavHost 안에 있는 모든 Composable 들은 navcontroller만 있다면 제한없이 이동할 수 있습니다.

    // 'Table.kt' 파일의 118번 째줄로 가시면 주석으로 사용 흐름을 정리했습니다. 천천히 따라가주세여.
    Column {
        NavHost(navController = navController, startDestination = "table"){
            composable("table") { Table(navController = navController) }
            composable("selectDB") { SelectDB(navController = navController) }
        }
    }
}