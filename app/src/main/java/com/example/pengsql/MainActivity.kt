package com.example.pengsql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pengsql.CLI.CLI
import com.example.pengsql.Diagram.Diagram
import com.example.pengsql.Home.Home
import com.example.pengsql.Navigation.PageNav
import com.example.pengsql.SelectDB.SelectDB
import com.example.pengsql.Table.Table
import com.example.pengsql.ui.theme.PengsQLTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Diagram(navController)
        }
    }
}

