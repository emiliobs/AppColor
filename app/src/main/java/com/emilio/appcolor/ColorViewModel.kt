package com.emilio.appcolor

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// The viewModel manages the data for the UI
class ColorViewModel(private val repository: ColorRepository) : ViewModel()
{
    // THe UI will observe this flow to get the color list
    var allColors = repository.getAllColors()

    // Function to add a new color without blocking the main UI
    fun addColor(hex: String, name: String)
    {
        // viewModelScope to add a new color without blocking in the background
        viewModelScope.launch {
            val newColor = Color(hex = hex, name = name)
            repository.insertColor(newColor)
        }
    }
}

// Factory to create the ViewMOdel (Needed because we pass the Repository)
class ColorViewModelFactory(private val repository: ColorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ColorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ColorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}