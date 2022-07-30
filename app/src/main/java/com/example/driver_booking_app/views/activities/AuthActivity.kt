package com.example.driver_booking_app.views.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.driver_booking_app.MainActivity
import com.example.driver_booking_app.R

class AuthActivity: AppCompatActivity()  {
    private lateinit var signInButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

//        val url1  = "http://10.0.2.2:5000"
//        val url2 = "https://reqres.in/api/products/3"

//        val message1 = HttpGet.httpGet(url1)
//        val message2 = HttpPost.httpPost(url1)
//        Log.d("m1", message1)
//        Log.d("m2", message2)

        signInButton = findViewById(R.id.signin_confirm_button)
        signInButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }



    }




}