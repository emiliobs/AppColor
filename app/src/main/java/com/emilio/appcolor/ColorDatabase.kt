package com.emilio.appcolor

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Color::class], version = 1)
abstract class ColorDatabase : RoomDatabase()
{
    abstract  fun  colorDao(): ColorDao
}