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
}
