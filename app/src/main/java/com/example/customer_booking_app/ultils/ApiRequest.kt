package com.example.customer_booking_app.ultils

import com.example.customer_booking_app.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiRequest {
    @POST("/register")
    fun registerCustomer(@Body acc:Account): Call<RegisterRespond>

    @POST("/login")
    fun loginCustomer(@Body acc:LoginAccount): Call<SignInRespond>

    @POST("/get-profile")
    fun getProfileCustomer(@Header("x-access-token") authToken: String, @Body information: Account): Call<GetProfileRespond>

    @POST("/edit-profile")
    fun editProfileCustomer(@Header("x-access-token") authToken: String, @Body information: Account): Call<EditProfileRespond>

    @POST("/trip-request")
    fun findStrip(@Header("x-access-token") authToken: String, @Body tripResquest: TripResquest): Call<TripResponse>

    @POST("/trip-information")
    fun getTripInformation(@Header("x-access-token") authToken: String, @Body tripInfomationRequest: TripInformationRequest): Call<CurrentInformation>

    @POST("/get-profile")
    fun getProfileDriver(@Header("x-access-token") authToken: String, @Body information: Account): Call<GetDriverProfileRespond>

}

object Retrofit{
    fun createApi():ApiRequest{
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()

        val api = Retrofit.Builder()
            .baseUrl(URL.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)
        return api
    }
}