package com.emilio.appcolor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao{
    @Query("Select * From colors")
    fun getAll(): Flow<List<Color>>


    @Insert
    suspend fun insert(color: Color)
}