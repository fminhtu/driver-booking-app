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

data class TripResponse(
    var driver: String = "",
    var message: String = "",
    var passenger: String = "",
)

data class UpdateGpsResponse(
    var message: String = ""
)

data class ProfilePassenger(
    var email: String = "",
    var phone: String = "",
    var username: String = ""
)
data class GetProfilePassengerResponse(
    var passengers: List<ProfilePassenger>,
    var message: String = ""
)

data class TripInfo(
    var dest_address:String = "",
    var dest_lat:String = "",
    var dest_long:String = "",
    var origin_address:String = "",
    var origin_lat:String = "",
    var origin_long:String = "",
    var role: String = "",
    var username: String = ""
)

data class CurrentInformation(
    var information:TripInfo?,
    var lat:String = "",
    var long:String = "",
    var message: String = "",
)

