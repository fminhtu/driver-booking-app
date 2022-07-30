package com.example.driver_booking_app.views.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.driver_booking_app.R

class SignUpActivity : AppCompatActivity() {
    private var loginBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initComponent()

        loginBtn?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun initComponent(){
        loginBtn = findViewById(R.id.signup_confirm_button)
    }
}