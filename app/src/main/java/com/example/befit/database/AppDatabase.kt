package com.example.befit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.befit.models.Product
import com.example.befit.models.ProductDao

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}