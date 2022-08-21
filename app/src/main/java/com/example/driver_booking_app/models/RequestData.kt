package com.example.driver_booking_app.models

data class Account (
    var username:String = "",
    var password:String? = "",
    var phone:String = "",
    var email: String = "",
    var seed: String = "",
    var licence_plate: String = "",
    var role:String = "driver"
)

data class LoginAccount(
    var username:String,
    var password:String
)

