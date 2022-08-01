package com.example.driver_booking_app.ultils

import com.example.driver_booking_app.models.HttpPost
import com.google.gson.Gson

class Account(var email: String, var password: String)

class Utils {
    companion object {
        lateinit var email: String
        lateinit var password: String
        lateinit var token: String


        fun SignIn(): Boolean  {
            val user = Account(email, password)
            val json = Gson().toJson(user)
            token = HttpPost.post("http://10.0.2.2:5000/login", json)

            if (token == "")
                return false

            return true
        }
    }
}

