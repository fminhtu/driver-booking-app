package com.example.driver_booking_app.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.HttpPost


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


//        val url1  = "http://172.17.0.2:3000"
//        val message1 = httpPost(url1)
//        Log.d("m1", message1)
        HttpPost.post("http://10.0.2.2:5000/login")

        // Wait 3 seconds on the splash screen before continuing.
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
//            Toast.makeText(this@SplashScreenActivity, "Done", Toast.LENGTH_SHORT).show()
            finish()
        }, 3000)
    }
}