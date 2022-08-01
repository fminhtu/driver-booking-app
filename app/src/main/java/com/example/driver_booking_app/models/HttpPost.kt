package com.example.driver_booking_app.models


import android.os.StrictMode
import android.util.Log
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

data class User(var email: String, var password: String)

object HttpPost  {
    init {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun post(url: String) {
        val user = User("fminhtu@gmail.com", "fminhtu")

        val (_, _, result) = url.httpPost()
            .jsonBody(Gson().toJson(user).toString())
            .responseString()
        Log.d("message", result.toString())

    }

}