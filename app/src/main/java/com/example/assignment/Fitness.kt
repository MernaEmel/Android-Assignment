package com.example.assignment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fitness")
data class Fitness(
    @PrimaryKey(autoGenerate =true) val id : Int=0,
    val activityName :String,
    val duration: Int,
    val date :String

)