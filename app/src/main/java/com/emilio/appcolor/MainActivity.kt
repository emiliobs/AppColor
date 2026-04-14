package com.emilio.appcolor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.emilio.appcolor.ui.theme.AppColorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Database -> Repository -> ViewMode - UI (Mi engine work perfect!)
        val database = Room.databaseBuilder(
            applicationContext,
            ColorDatabase::class.java,
            "color-db"
        ).build()

        // 2. Initialize the Repository
        val repository = ColorRepository(database.colorDao())

        // 3. Initialize the Factory
        val factory = ColorViewModelFactory(repository)

        // 4. Initialize the ViewModel using the Factory
        val viewModel: ColorViewModel by viewModels { factory }

        enableEdgeToEdge()
        setContent {
            AppColorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 5. Pass the ViewModel to our UI
                    ColorAppScreen(viewModel = viewModel)
                }

            }
        }
    }
}

//
// Navigation Wrapper

@Composable
fun ColorAppScreen(viewModel: ColorViewModel) {

    // State to handle screen swiching

    var showAddScreen by remember { mutableStateOf(false) }



}