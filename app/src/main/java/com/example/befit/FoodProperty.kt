package com.example.befit

import com.squareup.moshi.Json

data class FoodProperty(
    @Json(name = "fat_100g")
    val fat100G: Double,

    @Json(name = "fat_serving")
    val fatServing: Long,

    @Json(name = "proteins_100g")
    val proteins100G: Double,

    @Json(name = "proteins_serving")
    val proteinsServing: Long,

    @Json(name = "carbohydrates_100g")
    val carbohydrates100G: Double,

    @Json(name = "carbohydrates_serving")
    val carbohydratesServing: Long,

    @Json(name = "serving_quantity")
    val servingQuantity: String,

    @Json(name = "serving_size")
    val servingSize: String,

    @Json(name = "product_quantity")
    val productQuantity: String,

    val code: String

    )
