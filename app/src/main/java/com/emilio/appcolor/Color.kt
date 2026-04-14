package com.emilio.appcolor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colors")
data class Color(
    @PrimaryKey val hex: String,
    val name:  String
)
