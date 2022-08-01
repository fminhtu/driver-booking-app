package com.example.driver_booking_app.models


import android.os.StrictMode
import android.util.Log
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result


object HttpPost  {
    init {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun post(url: String, json: String): String {
//        val user = Account("fminhtu@gmail.com", "fminhtu")
        val (request, response, result) = url.httpPost()
            .jsonBody(json.toString())
            .responseString()

        when (result) {
            is Result.Failure -> {
                Log.d("message", "Failure:${result.get()}")
            }
            is Result.Success -> {
                Log.d("message", "Success:${result.get()}")
            }
            else -> { }
        }

        return result.get()
    }

}