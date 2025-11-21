package com.example.assignment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FitnessDao {
    @Insert
    suspend fun insertActivity(fitness: Fitness)

    @Query("SELECT * FROM fitness")
    suspend fun getAllActivities():List<Fitness>

    @Query("SELECT * FROM fitness WHERE date=:selecteDate")
    suspend fun getActivityByDate(selectedDate:String):List<Fitness>

    @Query("SELECT SUM(duration) FROM fitness WHERE date=:selectedDate")
    suspend fun getTotalDurationByDate(selectedDate: String):Int?
}