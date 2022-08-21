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