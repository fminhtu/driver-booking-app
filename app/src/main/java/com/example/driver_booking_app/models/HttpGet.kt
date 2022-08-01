package com.example.driver_booking_app.models

import android.os.StrictMode
import android.util.Log
import com.github.kittinunf.fuel.httpGet

object HttpGet  {
    init {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun get(myURL: String?) {
        val (_, _, result) = "http://httpbin.org/get"
            .httpGet(listOf("name" to "John Doe", "occupation" to "gardener"))
            .responseString()
        Log.d("get message:", result.toString())
    }

}