package com.example.driver_booking_app.views.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.driver_booking_app.MainActivity
import com.example.driver_booking_app.R

class SignInActivity: AppCompatActivity()  {
    private lateinit var emailText: TextView
    private lateinit var passwordText:TextView
    private lateinit var signInButton: Button
    private lateinit var signUpBtn: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initComponent()


        signInButton.setOnClickListener {
//            Utils.email = emailText.text.toString()
//            Utils.password = passwordText.text.toString()
//
//            val result = Utils.SignIn()
//
//            if (result) {
//                finishAffinity();
//                startActivity(Intent(this, MainActivity::class.java))
//            }
            startActivity(Intent(this, MainActivity::class.java))
        }

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }



    private fun initComponent(){
        signUpBtn = findViewById(R.id.signup_prompt)
        signInButton = findViewById(R.id.signin_confirm_button)
        emailText = findViewById(R.id.signin_email_edit_text)
        passwordText = findViewById(R.id.signin_password_edit_text)
    }




}