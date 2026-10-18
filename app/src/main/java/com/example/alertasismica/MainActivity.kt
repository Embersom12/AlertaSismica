package com.example.alertasismica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.alertasismica.navigation.AppNavigation
import com.example.alertasismica.ui.theme.AlertaSismicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlertaSismicaTheme {
                AppNavigation()
            }
        }
    }
}
