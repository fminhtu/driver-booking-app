package com.example.driver_booking_app.views.activities


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.driver_booking_app.MainActivity
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.Account
import com.example.driver_booking_app.models.LoginAccount
import com.example.driver_booking_app.ultils.Information
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class SignInActivity: AppCompatActivity()  {
    private lateinit var signInButton: Button
    private lateinit var signUpBtn: TextView

    private var usernameText: TextInputEditText? = null
    private var usernameLayout: TextInputLayout? = null

    private var passwordText: TextInputEditText? = null
    private var passwordLayout: TextInputLayout? = null

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initComponent()
        loginIfAlredyLoged()
        setOnclickListener()
        setOnFocusChangeListener()

    }

    private fun loginIfAlredyLoged(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this);
        val username = prefs.getString("username", "defaultUsername")
        val password = prefs.getString("password", "defaultPassword")
        if(!username.equals("defaultUsername") &&  !password.equals("defaultPassword")){
            usernameText!!.setText(username)
            passwordText!!.setText(password)
            callSignInApi()
        }
    }

    private fun initComponent(){
        signUpBtn = findViewById(R.id.signup_prompt)
        signInButton = findViewById(R.id.signin_confirm_button)

        usernameText = findViewById(R.id.username_edit_text)
        passwordText = findViewById(R.id.password_edit_text)

        usernameLayout = findViewById(R.id.username_input_layout)
        passwordLayout = findViewById(R.id.password_input_layout)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
    }


    private fun isCorrectDataForm():Boolean{
        var isCorrect = true
        if(usernameText?.text.isNullOrBlank() || usernameText?.text.isNullOrEmpty()){
            isCorrect = false
            usernameLayout?.error = "Invalid username"
        }
        if(passwordText?.text.isNullOrBlank() || passwordText?.text.isNullOrEmpty()){
            isCorrect = false
            passwordLayout?.error = "Invalid password"
        }
        return isCorrect
    }

    private fun setOnFocusChangeListener(){
        usernameText?.setOnFocusChangeListener{v, b ->
            if(b){
                usernameLayout?.error = null
            }
        }
        passwordText?.setOnFocusChangeListener{v, b ->
            if(b){
                passwordLayout?.error = null
            }
        }
    }

    private fun setOnclickListener(){
        signInButton.setOnClickListener {
            if(isCorrectDataForm()){
                callSignInApi()
                progressDialog.show()
            }
        }
        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun callSignInApi(){
        val acc = LoginAccount(
            usernameText!!.text.toString(),
            passwordText!!.text.toString(),
        )
        val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
        GlobalScope.launch(Dispatchers.IO){
            val resLogIn = api.loginDriver(acc).awaitResponse()
            var toastMessage = "Unexpected server error, please try again"
            if (resLogIn.code() == 401){
                toastMessage =  "Invalid username or password"
                withContext(Dispatchers.Main){
                    progressDialog.hide()
                    Toast.makeText(this@SignInActivity, toastMessage, Toast.LENGTH_LONG).show()
                }
            } else if(resLogIn.code() == 201){
                Information.token = resLogIn.body()!!.token
                Information.username = acc.username
                val bodyData = Account(Information.username, "", "", "")
                val resGetProfile = api.getProfileDriver(Information.token, bodyData).awaitResponse()
                if(resGetProfile.code() == 200) {
                    Information.email = resGetProfile.body()!!.drivers[0].email
                    Information.phone = resGetProfile.body()!!.drivers[0].phone
                    Information.licencePlate = resGetProfile.body()!!.drivers[0].licence_plate
                    Information.seed = resGetProfile.body()!!.drivers[0].seed
                    withContext(Dispatchers.Main){
                        progressDialog.hide()
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@SignInActivity);
                        prefs.edit().putString("username", acc.username).commit()
                        prefs.edit().putString("password", acc.password).commit();
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main){
                        progressDialog.hide()
                        Toast.makeText(this@SignInActivity, toastMessage, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main){
                    progressDialog.hide()
                    Toast.makeText(this@SignInActivity, toastMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(progressDialog.isShowing){
            progressDialog.cancel()
        }
    }
}