package com.example.driver_booking_app.views.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.driver_booking_app.MainActivity
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.Account
import com.example.driver_booking_app.ultils.Information
import com.example.driver_booking_app.viewModels.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ProfileFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var usernameTextView: EditText
    private lateinit var emailTextView: EditText
    private lateinit var phoneTextView: EditText
    private lateinit var seedTextView: TextView
    private lateinit var licenseNumberTextView: TextView
    private lateinit var updateBtn: TextView
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        Log.i("ASD", "ONCETAE")
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        initComponent()
        return rootView

    }
    private fun initComponent(){
        usernameTextView = rootView.findViewById(R.id.profile_name_tv)
        emailTextView = rootView.findViewById(R.id.profile_email_tv)
        phoneTextView = rootView.findViewById(R.id.profile_phone_tv)
        seedTextView = rootView.findViewById(R.id.profile_seed_tv)
        licenseNumberTextView = rootView.findViewById(R.id.profile_license_number_tv)
        updateBtn = rootView.findViewById(R.id.profile_update_tv)

        updateBtn.setOnClickListener {
            if(isCorrectData()){
                callApiUpdateProfile()
            }
        }
        usernameTextView.setText(Information.username)
        emailTextView.setText(Information.email)
        phoneTextView.setText(Information.phone)
        licenseNumberTextView.setText(Information.licencePlate)
        seedTextView.setText(Information.seed)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
    }
    private fun isCorrectData():Boolean{
        if(emailTextView.text.isNullOrEmpty() || emailTextView.text.isNullOrBlank()){
            Toast.makeText(requireContext(), "Invalid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if(phoneTextView.text.isNullOrEmpty() || phoneTextView.text.isNullOrBlank()){
            Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun callApiUpdateProfile(){
        val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
        val acc = Account(
            Information.username,
            null,
            phoneTextView.text.toString(),
            emailTextView.text.toString(),
            Information.seed,
            Information.licencePlate
        )
        GlobalScope.launch(Dispatchers.IO){
            val res = api.editProfileDriver( Information.token, acc).awaitResponse()
            val toastMessage:String
            if (res.code() == 200 && res.body()!!.message.equals("Successfully edited.")){
                toastMessage =  "Updated"
                Information.email = acc.email
                Information.phone = acc.phone
                withContext(Dispatchers.Main){
                    (requireActivity() as MainActivity).emailTextView.setText(Information.email)
                }
            } else if(res.code() == 401){
                toastMessage = " Unauthenticated user"
            } else {
                toastMessage = "Unexpected server error, please try again"
            }
            withContext(Dispatchers.Main) {
                progressDialog.hide()
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}