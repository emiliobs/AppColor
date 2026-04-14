package com.emilio.appcolor

import android.R
import android.os.Bundle
import android.service.quicksettings.Tile
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.room.util.TableInfo
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

// Add color slider
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddColorScreen(onSave: (String, String) -> Unit, onCancel: () -> Unit)
{
    // state varaibles for RGB slider and text input
    var red by remember { mutableStateOf(0f) }
    var green by remember { mutableStateOf(0f) }
    var blue by remember { mutableStateOf(0f) }
    var colorName by remember { mutableStateOf("") }

    // Dynamically calculate the Hex string and compose color
    val currentHex = String.format("#%02X%02X%02X", red.toInt(), green.toInt(), blue.toInt())
    val previewColor = ComposeColor(red.toInt(), green.toInt(), blue.toInt())

    Scaffold(
        topBar =
            {
                TopAppBar(
                 title = { Text("Add New Color") },
                 colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ){
       padding -> Column(
           modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
           horizontalAlignment = Alignment.CenterHorizontally
       )
    {
          // Live preview Box
           Box(
               modifier = Modifier.fillMaxWidth().height(150.dp).background(previewColor),
               contentAlignment = Alignment.Center){
               Text(text = currentHex, color =  ComposeColor.White, fontWeight = FontWeight.Bold)
           }

            Spacer(modifier = Modifier.height(24.dp))

           // RGB Sliders
        Text("R: ${red.toInt()}")
        Slider(value =  red, onValueChange = {red = it}, valueRange = 0f..255f, colors = SliderDefaults.colors(thumbColor = ComposeColor.Red, activeTrackColor = ComposeColor.Red))

        Text("G ${green.toInt()}")
        Slider(value =  green, onValueChange = {green = it}, valueRange = 0f..255f, colors = SliderDefaults.colors(thumbColor = ComposeColor.Green, activeTrackColor = ComposeColor.Green))

        Text("G ${blue.toInt()}")
        Slider(value =  blue, onValueChange = {blue = it}, valueRange = 0f..255f, colors = SliderDefaults.colors(thumbColor = ComposeColor.Blue, activeTrackColor = ComposeColor.Blue))

        Spacer(modifier = Modifier.height(24.dp))

        // Name Input
        OutlinedTextField(
            value = colorName,
            onValueChange = {colorName = it},
            label = { Text("Color Name (e.g Red)")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Button(
            onClick = {onSave(currentHex, colorName)},
            modifier = Modifier.fillMaxWidth(),
            enabled = colorName.isNotBlank() // Prevent saving without a name
        ) {
            Text("Add Color")
        }

        TextButton(onClick = onCancel) {Text("Cancel") }
       }
    }

}
