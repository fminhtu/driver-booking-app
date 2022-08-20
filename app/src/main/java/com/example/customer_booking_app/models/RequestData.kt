package com.example.customer_booking_app.models

data class Account (
    var username:String = "",
    var password:String? = "",
    var phone:String = "",
    var email: String = "",
    var role:String = "passenger"
)

data class LoginAccount(
    var username:String,
    var password:String
)

