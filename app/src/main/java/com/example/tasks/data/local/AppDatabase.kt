package com.example.tasks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasks.data.model.PriorityModel

@Database(entities = [PriorityModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun priorityDao(): PriorityDAO
}