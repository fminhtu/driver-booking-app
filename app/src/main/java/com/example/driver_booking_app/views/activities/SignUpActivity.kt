package com.example.driver_booking_app.views.activities
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.Account
import com.example.driver_booking_app.models.RegisterRespond
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

class SignUpActivity : AppCompatActivity() {
    private var loginBtn: Button? = null
    private var signUpBtn: MaterialButton? = null

    private var usernameText: TextInputEditText? = null
    private var usernameLayout: TextInputLayout? = null

    private var emailText: TextInputEditText? = null
    private var emailLayout: TextInputLayout? = null

    private var passwordText: TextInputEditText? = null
    private var passwordLayout: TextInputLayout? = null

    private var confirmPasswordText: TextInputEditText? = null
    private var confirmPasswordLayout: TextInputLayout? = null

    private var phoneNumberText: TextInputEditText? = null
    private var phoneNumberLayout: TextInputLayout? = null

    private var seedText: TextInputEditText? = null
    private var seedLayout: TextInputLayout? = null

    private var licenseNumberText: TextInputEditText? = null
    private var licenseNumberLayout: TextInputLayout? = null

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initComponent()

        initComponent()
        setOnclickListener()
        setOnFocusChangeListener()
    }

    private fun initComponent(){
        loginBtn = findViewById(R.id.signup_confirm_button)
        usernameText = findViewById(R.id.signup_name_edit_text)
        emailText = findViewById(R.id.signup_email_edit_text)
        passwordText = findViewById(R.id.password_edit_text)
        confirmPasswordText = findViewById(R.id.signup_conf_password_edit_text)
        phoneNumberText = findViewById(R.id.signup_phone_number_edit_text)
        seedText = findViewById(R.id.seed_edit_text)
        licenseNumberText = findViewById(R.id.signup_license_plates_edit_text)
        signUpBtn = findViewById(R.id.customer_signup_switch_button)

        usernameLayout = findViewById(R.id.username_input_layout)
        emailLayout = findViewById(R.id.email_input_layout)
        passwordLayout = findViewById(R.id.password_input_layout)
        confirmPasswordLayout = findViewById(R.id.confirm_password_input_layout)
        phoneNumberLayout = findViewById(R.id.phone_number_input_layout)
        seedLayout = findViewById(R.id.seed_input_layout)
        licenseNumberLayout = findViewById(R.id.license_plates_input_layout)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
    }

    private fun setOnclickListener(){
        loginBtn?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        signUpBtn?.setOnClickListener {
            if(isCorrectDataForm()){
                callRegisterApi()
                progressDialog.show()
            }
        }
        usernameText?.setOnClickListener { usernameLayout?.error = null }
        passwordText?.setOnClickListener { passwordLayout?.error = null }
        confirmPasswordText?.setOnClickListener { confirmPasswordLayout?.error = null }
        emailText?.setOnClickListener { emailLayout?.error = null }
        phoneNumberText?.setOnClickListener { phoneNumberLayout?.error = null }
    }

    private fun callRegisterApi(){
        val acc = Account(
            usernameText!!.text.toString(),
            passwordText!!.text.toString(),
            phoneNumberText!!.text.toString(),
            emailText!!.text.toString(),
            seedText!!.text.toString(),
            licenseNumberText!!.text.toString(),
        )
        Log.i("ASD", acc.toString())
        val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
        GlobalScope.launch(Dispatchers.IO){
            val res = api.registerDriver(acc).awaitResponse()
            Log.i("ASD", res.code().toString())
            var toastMessage = "Unexpected server error, please try again"
            if (res.code() == 201){
                toastMessage =  "Register success!"
            } else if(res.code() == 401){
                toastMessage = "Account already exist"
            }
            withContext(Dispatchers.Main) {
                progressDialog.hide()
                Toast.makeText(this@SignUpActivity, toastMessage, Toast.LENGTH_LONG).show()
                if(res.code() == 201){
                    startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                    finish()
                }
            }
        }
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
        if(confirmPasswordText?.text.isNullOrBlank() || confirmPasswordText?.text.isNullOrEmpty()){
            isCorrect = false
            confirmPasswordLayout?.error = "Invalid password confirm"
        }
        if(emailText?.text.isNullOrBlank() || emailText?.text.isNullOrEmpty()
            ||  !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText?.text.toString()).matches()){
            isCorrect = false
            emailLayout?.error = "Invalid email"
        }
        if(phoneNumberText?.text.isNullOrEmpty() ||
            phoneNumberText?.text.toString().length < 6 ||
            phoneNumberText?.text.toString().length > 13 ||
            !android.util.Patterns.PHONE.matcher(phoneNumberText?.text.toString()).matches())
        {
            isCorrect = false
            phoneNumberLayout?.error = "Invalid phone number"
        }

        if(seedText?.text.isNullOrEmpty() ||
            (!seedText?.text.toString().equals("4") &&
            !seedText?.text.toString().equals("7")))
        {
            isCorrect = false
            seedLayout?.error = "Only allow 4 or 7"
        }

        if(licenseNumberText?.text.isNullOrBlank() || licenseNumberText?.text.isNullOrEmpty()){
            isCorrect = false
            licenseNumberLayout?.error = "Invalid license number"
        }

        if(!passwordText?.text.toString().equals(confirmPasswordText?.text.toString())){
            isCorrect = false
            confirmPasswordLayout?.error = "The password confirm doesn't match"
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
        confirmPasswordText?.setOnFocusChangeListener{v, b ->
            if(b){
                confirmPasswordLayout?.error = null
            }
        }
        emailText?.setOnFocusChangeListener { v, b ->
            if (b) {
                emailLayout?.error = null
            }
        }
        phoneNumberText?.setOnFocusChangeListener{v, b ->
            if(b){
                phoneNumberLayout?.error = null
            }
        }
        seedText?.setOnFocusChangeListener{v, b ->
            if(b){
                seedLayout?.error = null
            }
        }
        licenseNumberText?.setOnFocusChangeListener{v, b ->
            if(b){
                licenseNumberLayout?.error = null
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