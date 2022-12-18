package com.example.befit.models

class User {
    var email: String? = null
    var age: Int? = null
    var height: Double? = null
    var weight: Double? = null
    var goal: String? = null
    var dailyActivity: String? = null
    var changeSpeed: Double? = null
    var numberOfTrainings: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User::class.java)
    }

    constructor(email: String?, age: Int?, height: Double?, weight: Double?, goal: String?, dailyActivity: String?, changeSpeed: Double?, numberOfTrainings: String?) {
        this.email = email
        this.age = age
        this.height = height
        this.weight = weight
        this.goal = goal
        this.dailyActivity = dailyActivity
        this.changeSpeed = changeSpeed
        this.numberOfTrainings = numberOfTrainings
    }
    constructor(email: String?) {
        this.email = email
        this.age = null
        this.height = null
        this.weight = null
        this.goal = null
        this.dailyActivity = null
        this.changeSpeed = null
        this.numberOfTrainings = null
    }
}
