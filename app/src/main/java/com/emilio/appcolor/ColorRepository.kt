package com.emilio.appcolor

import kotlinx.coroutines.flow.Flow

class ColorRepository(private val colorDao: ColorDao)
{
    // Gets the stream of colors from the DAO
    fun getAllColors(): Flow<List<Color>> {
        return colorDao.getAll() as Flow<List<Color>>
    }

    // Insert a new color using the DAO
    suspend fun insertColor(color: Color)
    {
      colorDao.insert(color)
    }
}