package com.example.befit.models

class User {
    var email: String? = null
    var age: String? = null
    var height: String? = null
    var weight: String? = null
    var goal: String? = null
    var gender: String? = null
    var dailyActivity: String? = null
    var changeSpeed: String? = null
    var trainingsPerWeek: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User::class.java)
    }

    constructor(email: String?, age: String?, height: String?, weight: String?, goal: String?, dailyActivity: String?, changeSpeed: String?, trainingsPerWeek: String?, gender: String?) {
        this.email = email
        this.age = age
        this.height = height
        this.weight = weight
        this.goal = goal
        this.dailyActivity = dailyActivity
        this.changeSpeed = changeSpeed
        this.trainingsPerWeek = trainingsPerWeek
        this.gender = gender
    }
    constructor(email: String?) {
        this.email = email
        this.age = null
        this.height = null
        this.weight = null
        this.goal = null
        this.dailyActivity = null
        this.changeSpeed = null
        this.trainingsPerWeek = null
        this.gender = null
    }

    fun calculateCaloricIntake(): Int {

        val physicalActivityLevel: Double = if(this.dailyActivity.toString() == "Low"){
            1.5
        } else if(this.dailyActivity.toString() == "Medium") {
            1.8
        } else {
            2.1
        }

        val BMR: Double = if (this.gender!!.toString() == "Male") {
            66.47 + (13.75 * this.weight!!.toDouble()) + (5.003 * this.height!!.toDouble()) - (6.755 * this.age!!.toInt())
        } else {
            655.1 + (9.563 * this.weight!!.toDouble()) + (1.85 * this.height!!.toDouble()) - (4.676 * this.age!!.toInt())
        }

        val caloricIntake = BMR * physicalActivityLevel

        val weightChangePerDay = this.changeSpeed!!.toDouble() / 7.0

        var caloricAdjustment: Double = weightChangePerDay * 7700.0 / 0.45359237

        when (this.goal.toString()) {
            "Lose weight" -> caloricAdjustment *= -1
            "Maintain weight" -> caloricAdjustment = 0.0
        }

        val trainingAdjustment = this.trainingsPerWeek!!.toInt() * 500.0 / 7
        return (caloricIntake + caloricAdjustment + trainingAdjustment).toInt()
    }


}
