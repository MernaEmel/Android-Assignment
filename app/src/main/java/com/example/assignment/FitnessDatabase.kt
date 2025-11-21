package com.example.assignment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Fitness::class], version = 1)
abstract class FitnessDatabase : RoomDatabase() {

    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            if (INSTANCE == null) {
                synchronized(FitnessDatabase::class) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            FitnessDatabase::class.java,
                            "fitness_db"
                        ).build()
                    }
                }

            return INSTANCE!!
        }
    }
}