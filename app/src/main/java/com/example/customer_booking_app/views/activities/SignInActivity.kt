package com.example.customer_booking_app.views.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.customer_booking_app.MainActivity
import com.example.customer_booking_app.R

class SignInActivity: AppCompatActivity()  {
    private lateinit var signInButton: Button
    private lateinit var signUpBtn: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initComponent()
//        val url1  = "http://10.0.2.2:5000"
//        val url2 = "https://reqres.in/api/products/3"

//        val message1 = HttpGet.httpGet(url1)
//        val message2 = HttpPost.httpPost(url1)
//        Log.d("m1", message1)
//        Log.d("m2", message2)

        signInButton.setOnClickListener {
            finishAffinity();
            startActivity(Intent(this, MainActivity::class.java))
        }
        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun initComponent(){
        signUpBtn = findViewById(R.id.signup_prompt)
        signInButton = findViewById(R.id.signin_confirm_button)
    }




}