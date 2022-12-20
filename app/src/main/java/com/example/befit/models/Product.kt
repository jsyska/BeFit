package com.example.befit.models

import androidx.room.*
import com.squareup.moshi.Json

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val date: String,
    val Carbohydrates100g: Double,
    val EnergyKcal100g: Double,
    val Fat100g: Double,
    val Proteins100g: Double,
    val weight: Int,
    val userID: String,
    val kcal: Double,
    val fat: Double,
    val protein: Double,
    val carb: Double
    )

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Query("SELECT * FROM product WHERE date = :date and userID = :userID")
    fun getProductsForDate(date: String, userID: String): MutableList<Product>

    @Delete
    fun delete(product: Product)
}