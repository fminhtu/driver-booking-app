package com.example.driver_booking_app.models

data class RegisterRespond(
    var message: String = ""
)

data class SignInRespond(
    var token: String = "",
    var message: String = ""
)

data class Profile(
    var email: String = "",
    var phone: String = "",
    var username: String = "",
    var seed: String = "",
    var licence_plate: String = ""
)
data class GetProfileRespond(
    var drivers: List<Profile>,
    var message: String = ""
)

data class EditProfileRespond(val message: String = "")