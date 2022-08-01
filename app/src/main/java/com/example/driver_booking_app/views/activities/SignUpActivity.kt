package com.example.driver_booking_app.views.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.driver_booking_app.R
import com.example.driver_booking_app.ultils.Utils

class SignUpActivity : AppCompatActivity() {
    private var loginBtn: Button? = null
    private var emailText: TextView? = null
    private var passwordText:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initComponent()

        loginBtn?.setOnClickListener {
            Utils.email = emailText?.text.toString()
            Utils.password = emailText?.text.toString()



            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun initComponent(){
        loginBtn = findViewById(R.id.signup_confirm_button)
        emailText = findViewById(R.id.signin_email_edit_text)
        passwordText = findViewById(R.id.signin_password_edit_text)
    }
}

//        val url1  = "http://10.0.2.2:5000"
//        val url2 = "https://reqres.in/api/products/3"

//        val message1 = HttpGet.httpGet(url1)
//        val message2 = HttpPost.httpPost(url1)
//        Log.d("m1", message1)
//        Log.d("m2", message2)