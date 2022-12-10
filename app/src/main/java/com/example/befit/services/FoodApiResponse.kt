package com.example.befit.services

import com.squareup.moshi.Json

data class FoodApiResponse(
    @Json(name="code")
    val Code: String?,
    @Json(name="product")
    val Product: Product?,
    @Json(name="status")
    val Status: Long?
)

data class Product(
    @Json(name="nutriments") val Nutriments: Nutriments
)

data class Nutriments(
    @Json(name="carbohydrates_100g") val Carbohydrates100g: Double,
    @Json(name="energy-kcal_100g") val EnergyKcal100g: Long,
    @Json(name="fat_100g") val Fat100g: Double,
    @Json(name="proteins_100g") val Proteins100g: Double
)