package com.example.befit.models

import androidx.room.*
import com.squareup.moshi.Json

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var name: String,
    var date: String,
    var Carbohydrates100g: Double,
    var EnergyKcal100g: Double,
    var Fat100g: Double,
    var Proteins100g: Double,
    var weight: Int,
    var userID: String,
    var kcal: Double,
    var fat: Double,
    var protein: Double,
    var carb: Double
    )

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: MutableList<Product>)

    @Query("SELECT * FROM product WHERE date = :date and userID = :userID")
    fun getProductsForDate(date: String, userID: String): MutableList<Product>

    @Delete
    fun delete(product: Product)
}