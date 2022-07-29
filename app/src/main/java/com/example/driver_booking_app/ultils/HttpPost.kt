package com.example.driver_booking_app.ultils

import android.os.StrictMode
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL

object HttpPost  {
    init {
            var str = ""

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun httpPost(myUrl: String?): String {
        val url = URL(myUrl)
        val postData = "foo1=bar1&foo2=bar2"
        var str = ""
        val conn = url.openConnection()
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Content-Length", postData.length.toString())

        DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
        BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                println(line)
                str += line
            }
        }

        return str
    }

}