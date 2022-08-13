package com.example.customer_booking_app.ultils

import android.os.StrictMode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object HttpGet  {
    init {
        var str = ""

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun httpGet(myURL: String?): String {
        var str = ""

        val url = URL(myURL)
        val connection = url.openConnection()
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
            var line: String?
            while (inp.readLine().also { line = it } != null) {
//                println(line)
                str += line
            }
        }

        return str
    }

}