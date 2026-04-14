package com.emilio.appcolor

import android.os.Bundle
import android.service.quicksettings.Tile
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.emilio.appcolor.ui.theme.AppColorTheme
import androidx.compose.ui.graphics.Color as ComposeColor

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
                    //  Start the navigation wrapper
                    ColorAppNavigation(viewModel = viewModel)
                }

            }
        }
    }
}

//
// Navigation Wrapper

@Composable
fun ColorAppNavigation(viewModel: ColorViewModel) {
    // State to handle screen switching
    var showAddScreen by remember { mutableStateOf(false) }

    if (showAddScreen) {
        AddColorScreen(
            onSave = { hex, name ->
                viewModel.addColor(hex, name)
                showAddScreen = false // Return to list
            },
            onCancel = { showAddScreen = false } // Return to list without saving
        )
    } else {
        ColorListScreen(
            viewModel = viewModel,
            onAddClick = { showAddScreen = true } // Navigate to Add Screen
        )
    }
}


// Screebn 1: Color List
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorListScreen(viewModel: ColorViewModel, onAddClick: () -> Unit)
{
    // Observe database changes
    val colorsList by viewModel.allColors.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
           TopAppBar(
               title = {Text("ColorValue")},
               colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
           )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick){
                Icon(Icons.Filled.Add, contentDescription = "Add Color")
            }
        }
    ){ padding ->
        if(colorsList.isEmpty())
        {
            // Empty state UI
            Box(modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center)
            {
                Text("The list is empty. Click + to add a color!")
            }
        }
        else
        {
            // List of colors
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp))
            {
              items(colorsList){ color ->
                  // parse Hex string to Compase Color safely
                  var cardColor = try
                  {
                      ComposeColor(android.graphics.Color.parseColor(color.hex))
                  }catch (e: Exception)
                  {
                      ComposeColor.Gray // Fallback color
                  }

                  Card(
                      modifier = Modifier.fillMaxWidth().height(80.dp),
                      colors = CardDefaults.cardColors(cardColor)
                  ) {
                      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                          Text(
                              text = "${color.name} (${color.hex})",
                              color = ComposeColor.White,
                              fontWeight = FontWeight.Bold,
                              fontSize = 18.sp
                          )
                      }
                  }

              }


            }
        }
    }


}

@Composable
fun AddColorScreen(onSave: (String, String) -> Unit, onCancel: () -> Unit)
{
    TODO("Not yet implemented")
}