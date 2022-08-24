package com.example.customer_booking_app.views.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.customer_booking_app.R
import com.example.customer_booking_app.models.Account
import com.example.customer_booking_app.models.GoogleMap
import com.example.customer_booking_app.models.TripInformationRequest
import com.example.customer_booking_app.models.TripResquest
import com.example.customer_booking_app.ultils.DriverProfileObj
import com.example.customer_booking_app.ultils.Information
import com.example.customer_booking_app.ultils.TripInformation
import com.example.customer_booking_app.viewModels.HomeViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import java.util.concurrent.TimeUnit

enum class PANEL{
    LOCATION, BOOK_RIDE, WAITING_DRIVER, INFORMATION_DRIVER_PANEL ,HIDE_ALL
}

enum class SEED {
    FOUR, SEVEN
}

class HomeFragment : Fragment(){

    private lateinit var rootView:View
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var locationPanel: ConstraintLayout
    private lateinit var bookRidePanel: ConstraintLayout
    private lateinit var waitingDriverPanel: ConstraintLayout
    private lateinit var informationDriverPanel: MaterialCardView
    val AUTOCOMPLETE_REQUEST_CODE = 1
    //location pannel
    private lateinit var confirmLocationBtn: Button
    private lateinit var pickMeLocation: AppCompatEditText
    private lateinit var destintionLocation: AppCompatEditText

    //bookRide pannel
    private lateinit var bookRideBtn: Button

    //watingDriver panel
    private lateinit var cancelFindingDriverBtn: Button
    private lateinit var fourSeedLabel: ImageButton
    private lateinit var sevenSeedLabel: ImageButton
    private var isCancle: Boolean = false

    //information_driver_panel
    private lateinit var driverNameTextView: TextView
    private lateinit var driverLicenseNumberTextView: TextView

    // This property is only valid between onCreateView and

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        initComponent()
        return rootView
    }

    private fun initComponent(){
        Places.initialize(requireContext(),"AIzaSyD-HPqZ6715o4r5STSx5mGtlx8vqjTLZNc")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as GoogleMap
        locationPanel = rootView.findViewById(R.id.location_confirm_panel)
        bookRidePanel = rootView.findViewById(R.id.book_ride_panel)
        waitingDriverPanel = rootView.findViewById(R.id.waiting_driver_panel)
        informationDriverPanel = rootView.findViewById(R.id.information_driver_panel)

        confirmLocationBtn = locationPanel.findViewById(R.id.confirm_location_button)
        bookRideBtn = bookRidePanel.findViewById(R.id.book_ride_btn)
        fourSeedLabel = bookRidePanel.findViewById(R.id.four_seeder_label)
        sevenSeedLabel = bookRidePanel.findViewById(R.id.seven_seeder_label)
        pickMeLocation = locationPanel.findViewById(R.id.pickUpTextView)
        destintionLocation = locationPanel.findViewById(R.id.destination_location)
        driverNameTextView = informationDriverPanel.findViewById(R.id.driver_name_text_view)
        driverLicenseNumberTextView = informationDriverPanel.findViewById(R.id.driver_license_number_textview)
        cancelFindingDriverBtn = rootView.findViewById(R.id.cancel_finding_driver_btn)

        setOnclickLisner()
        getMapAsync()
    }
    private fun isTrueDataLocation():Boolean{
        if(pickMeLocation.text!!.isNullOrEmpty() || destintionLocation.text!!.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Please enter correct location", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun setOnclickLisner(){
        var findingDriver: Job? = null
//        pickMeLocation.setOnClickListener { getAutoCompletePlace() }
        confirmLocationBtn.setOnClickListener {
            if(isTrueDataLocation()){
                activePanel(PANEL.BOOK_RIDE)
            }
        }

        bookRideBtn.setOnClickListener {
            activePanel(PANEL.WAITING_DRIVER)
            callRequestTripApi()
        }
        cancelFindingDriverBtn.setOnClickListener {
            isCancle = true
            activePanel(PANEL.LOCATION)
        }
        fourSeedLabel.setOnClickListener { activeSeed(SEED.FOUR) }
        sevenSeedLabel.setOnClickListener { activeSeed(SEED.SEVEN) }

    }

    private fun callRequestTripApi(){
        isCancle = false
        val api = com.example.customer_booking_app.ultils.Retrofit.createApi()
        val req = TripResquest(
            "passenger",
            Information.username,
            pickMeLocation.text.toString(),
            "20",
            "30",
            destintionLocation.text.toString(),
            "50",
            "70"
        )
        GlobalScope.launch(Dispatchers.IO) {
            try{
                var isHaveATrip = false
                while(!isHaveATrip){
                    if(isCancle){
                        break
                    }
                    val res = api.findStrip( Information.token, req).awaitResponse()
                    if(res.code() == 200){
                        Log.i("ASD", res.body().toString())
                        if(!res.body()!!.driver.equals("") && !res.body()!!.passenger.isNullOrEmpty()){
                            isHaveATrip = true
                            callGetTripInformationApi(res.body()!!.driver)
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                    Log.i("ASD",res.code().toString())

                }
            } catch (e: Exception){
                Log.i("ASD", e.message.toString())
                var isHaveATrip = false
                while(!isHaveATrip){
                    if(isCancle){
                        break
                    }
                    val res = api.findStrip( Information.token, req).awaitResponse()
                    if(res.code() == 200){
                        Log.i("ASD", res.body().toString())
                        if(!res.body()!!.driver.equals("") && !res.body()!!.passenger.isNullOrEmpty()){
                            isHaveATrip = true
                            callGetTripInformationApi(res.body()!!.driver)
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                    Log.i("ASD",res.code().toString())

                }
            }
        }
    }

    private fun callGetTripInformationApi(driverName:String){
        var countWaitingTime = 0
        val api = com.example.customer_booking_app.ultils.Retrofit.createApi()
        val tripInfoReq = TripInformationRequest(
            "driver",
            driverName
        )
        GlobalScope.launch(Dispatchers.IO) {
            try{
                var isDriverAccept = false
                while(!isDriverAccept && countWaitingTime <= 5){
                    val tripInfoResponse = api.getTripInformation(Information.token, tripInfoReq).awaitResponse()
                    if(tripInfoResponse.code() == 200){
                        if(!tripInfoResponse.body()!!.lat.isNullOrEmpty()){
                            isDriverAccept = true
                            if(TripInformation.driver.equals("")){
                                TripInformation.driver = driverName
                                TripInformation.passenger = Information.username
                                TripInformation.destAddress = tripInfoResponse.body()!!.information!!.dest_address
                                TripInformation.originAddress = tripInfoResponse.body()!!.information!!.origin_address
                                TripInformation.originLat = tripInfoResponse.body()!!.information!!.origin_lat
                                TripInformation.originLong = tripInfoResponse.body()!!.information!!.origin_long
                                TripInformation.destLat = tripInfoResponse.body()!!.information!!.dest_lat
                                TripInformation.destLong = tripInfoResponse.body()!!.information!!.dest_long
                                TripInformation.isStart = "false"
                                callGetDriverProfile(driverName)
                                callCheckStartDriver(tripInfoResponse.body()!!.lat, driverName)
                                callCheckIsEndStrip()
                            }
                            Log.i("ASD", "HAVE A TRIP")
                        }
                    }
                    countWaitingTime+=1
                    delay(TimeUnit.SECONDS.toMillis(2))
                }
            } catch (e: Exception){
                var isDriverAccept = false
                while(!isDriverAccept && countWaitingTime <= 5){
                    val tripInfoResponse = api.getTripInformation(Information.token, tripInfoReq).awaitResponse()
                    if(tripInfoResponse.code() == 200){
                        if(!tripInfoResponse.body()!!.lat.isNullOrEmpty()){
                            isDriverAccept = true
                            if(TripInformation.driver.equals("")){
                                TripInformation.driver = driverName
                                TripInformation.passenger = Information.username
                                TripInformation.destAddress = tripInfoResponse.body()!!.information!!.dest_address
                                TripInformation.originAddress = tripInfoResponse.body()!!.information!!.origin_address
                                TripInformation.originLat = tripInfoResponse.body()!!.information!!.origin_lat
                                TripInformation.originLong = tripInfoResponse.body()!!.information!!.origin_long
                                TripInformation.destLat = tripInfoResponse.body()!!.information!!.dest_lat
                                TripInformation.destLong = tripInfoResponse.body()!!.information!!.dest_long
                                TripInformation.isStart = "false"
                                callGetDriverProfile(driverName)
                                callCheckStartDriver(tripInfoResponse.body()!!.lat, driverName)
                                callCheckIsEndStrip()
                            }
                            Log.i("ASD", "HAVE A TRIP")
                        }
                    }
                    countWaitingTime+=1
                    delay(TimeUnit.SECONDS.toMillis(2))
                }
            }
        }
    }

    private fun callCheckStartDriver(lat:String, driverName: String){
        var isStart = false
        val api = com.example.customer_booking_app.ultils.Retrofit.createApi()
        val tripInfoReq = TripInformationRequest(
            "driver",
            driverName
        )
        GlobalScope.launch(Dispatchers.IO) {
            try{
                while(!isStart){
                    val tripInfoResponse = api.getTripInformation(Information.token, tripInfoReq).awaitResponse()
                    if(tripInfoResponse.code() == 200){
                        if(!tripInfoResponse.body()!!.lat.equals(lat)){
                            isStart = true
                            withContext(Dispatchers.Main){
                                activePanel(PANEL.HIDE_ALL)
                                Toast.makeText(requireContext(), "Start your trip", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                }
            } catch (e: Exception){
                while(!isStart){
                    val tripInfoResponse = api.getTripInformation(Information.token, tripInfoReq).awaitResponse()
                    if(tripInfoResponse.code() == 200){
                        if(!tripInfoResponse.body()!!.lat.equals(lat)){
                            isStart = true
                            withContext(Dispatchers.Main){
                                activePanel(PANEL.HIDE_ALL)
                                Toast.makeText(requireContext(), "Start your trip", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                }
            }

        }
    }

    private fun callCheckIsEndStrip(){
        val api = com.example.customer_booking_app.ultils.Retrofit.createApi()
        val tripInfoReq = TripInformationRequest(
            "driver",
            TripInformation.driver
        )
        var isEndtrip = false
        GlobalScope.launch(Dispatchers.IO) {
            try{
                while(!isEndtrip){
                    val tripInfoResponse = api.getTripInformation(Information.token, tripInfoReq).awaitResponse()
                    Log.i("ASD", "End trip")
                    Log.i("ASD", tripInfoResponse.code().toString())
                    Log.i("ASD", tripInfoResponse.body().toString())
                    if(tripInfoResponse.code() == 200) {
                        if(tripInfoResponse.body()!!.message.equals("driver is not found")){
                            isEndtrip = true
                            withContext(Dispatchers.Main){
                                TripInformation.driver = ""
                                isCancle = true
                                Log.i("ASD", "End trip")
                                findNavController().navigate(R.id.action_nav_home_to_nav_thanks)
                            }
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                }
            } catch (e: Exception){
                while(!isEndtrip){
                    val tripInfoResponse = api.getTripInformation(Information.token, tripInfoReq).awaitResponse()
                    Log.i("ASD", "End trip")
                    Log.i("ASD", tripInfoResponse.code().toString())
                    Log.i("ASD", tripInfoResponse.body().toString())

                    if(tripInfoResponse.code() == 200) {
                        isEndtrip = true
                        if(tripInfoResponse.body()!!.message.equals("driver is not found"))
                            withContext(Dispatchers.Main){
                                isCancle = true
                                TripInformation.driver = ""
                                Log.i("ASD", "End trip")
                                findNavController().navigate(R.id.action_nav_home_to_nav_thanks)
                            }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                }
            }

        }
    }

    private fun callGetDriverProfile(driverName: String){
        val api = com.example.customer_booking_app.ultils.Retrofit.createApi()
        val acc = Account(
            driverName,
            "",
            "",
            "",
            "driver"
        )
        GlobalScope.launch(Dispatchers.IO) {
            val resGetProfile = api.getProfileDriver(Information.token, acc).awaitResponse()
            if(resGetProfile.code() == 200) {
                DriverProfileObj.username = resGetProfile.body()!!.drivers[0].username
                DriverProfileObj.email = resGetProfile.body()!!.drivers[0].email
                DriverProfileObj.phone = resGetProfile.body()!!.drivers[0].phone
                DriverProfileObj.licencePlate = resGetProfile.body()!!.drivers[0].licence_plate
                DriverProfileObj.seed = resGetProfile.body()!!.drivers[0].seed
                withContext(Dispatchers.Main){
                    driverNameTextView.setText(DriverProfileObj.username)
                    driverLicenseNumberTextView.setText(DriverProfileObj.licencePlate)
                    activePanel(PANEL.INFORMATION_DRIVER_PANEL)
                }

            }
        }
    }

    private fun activeSeed(seed: SEED){
        when(seed){
            SEED.FOUR -> {
                fourSeedLabel.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.red_600),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                sevenSeedLabel.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            SEED.SEVEN -> {
                fourSeedLabel.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                sevenSeedLabel.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.red_600),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun getMapAsync(){
        mapFragment.getMapAsync {
            try{
                val success = it.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.uber_maps_style))
                if(!success){
                    Log.e("MapError", "Style parse error")
                }
            } catch (e: Resources.NotFoundException){
                Log.e("MapError", e.message!!)
            }
        }
    }

    private fun activePanel(panel: PANEL){
        when (panel){
            PANEL.BOOK_RIDE -> {
                locationPanel.visibility = View.GONE
                bookRidePanel.visibility = View.VISIBLE
                waitingDriverPanel.visibility = View.GONE
                informationDriverPanel.visibility = View.GONE
            }
            PANEL.LOCATION -> {
                locationPanel.visibility = View.VISIBLE
                waitingDriverPanel.visibility = View.GONE
                bookRidePanel.visibility = View.GONE
                informationDriverPanel.visibility = View.GONE
            }
            PANEL.WAITING_DRIVER -> {
                waitingDriverPanel.visibility = View.VISIBLE
                locationPanel.visibility = View.GONE
                bookRidePanel.visibility = View.GONE
                informationDriverPanel.visibility = View.GONE
            }
            PANEL.INFORMATION_DRIVER_PANEL -> {
                locationPanel.visibility = View.GONE
                bookRidePanel.visibility = View.GONE
                waitingDriverPanel.visibility = View.GONE
                informationDriverPanel.visibility = View.VISIBLE
            }
            PANEL.HIDE_ALL -> {
                waitingDriverPanel.visibility = View.GONE
                locationPanel.visibility = View.GONE
                bookRidePanel.visibility = View.GONE
                informationDriverPanel.visibility = View.GONE
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    data?.let {
//                        val place = Autocomplete.getPlaceFromIntent(data)
//                        Log.i("ERROR", "Place: ${place.name}, ${place.id}")
//                    }
//                }
//                AutocompleteActivity.RESULT_ERROR -> {
//                    // TODO: Handle the error.
//                    data?.let {
//                        val status = Autocomplete.getStatusFromIntent(data)
//                        Log.i("ERROR", status.statusMessage ?: "")
//                    }
//                }
//                Activity.RESULT_CANCELED -> {
//                    // The user canceled the operation.
//                }
//            }
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
}