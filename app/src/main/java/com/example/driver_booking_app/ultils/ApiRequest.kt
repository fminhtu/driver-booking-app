package com.example.driver_booking_app.ultils

import com.example.driver_booking_app.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiRequest {
    @POST("/register")
    fun registerDriver(@Body acc: Account): Call<RegisterRespond>

    @POST("/login")
    fun loginDriver(@Body acc:LoginAccount): Call<SignInRespond>

    @POST("/get-profile")
    fun getProfileDriver(@Header("x-access-token") authToken: String, @Body information: Account): Call<GetProfileRespond>

    @POST("/edit-profile")
    fun editProfileDriver(@Header("x-access-token") authToken: String, @Body information: Account): Call<EditProfileRespond>

    @POST("/trip-request")
    fun findStrip(@Header("x-access-token") authToken: String, @Body tripResquest: TripResquest): Call<TripResponse>

    @POST("/update-gps")
    fun updateGps(@Header("x-access-token") authToken: String, @Body updateGpsRequest: UpdateGpsRequest): Call<UpdateGpsResponse>

    @POST("/trip-information")
    fun getTripInformation(@Header("x-access-token") authToken: String, @Body tripInfomationRequest: TripInformationRequest): Call<CurrentInformation>

    @POST("/get-profile")
    fun getProfilePassenger(@Header("x-access-token") authToken: String, @Body information: Account): Call<GetProfilePassengerResponse>

    @POST("/leave-trip")
    fun leaveTrip(@Header("x-access-token") authToken: String, @Body information: Account): Call<TripResponse>

    @POST("/end-trip")
    fun endTrip(@Header("x-access-token") authToken: String, @Body information: Account): Call<TripResponse>
}

object Retrofit{
    fun createApi():ApiRequest{
        val api = Retrofit.Builder()
            .baseUrl(URL.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)
        return api
    }
}