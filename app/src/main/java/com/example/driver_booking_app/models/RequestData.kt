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

data class TripResquest(
    var role: String = "driver",
    var username: String = "",
    var origin_address:String = "",
    var origin_lat:String = "",
    var origin_long:String = "",
    var dest_address:String = "",
    var dest_lat:String = "",
    var dest_long:String = ""
)
