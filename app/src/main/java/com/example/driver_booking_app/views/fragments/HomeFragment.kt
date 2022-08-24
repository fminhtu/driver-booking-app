package com.example.driver_booking_app.views.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.*
import com.example.driver_booking_app.ultils.ApiRequest
import com.example.driver_booking_app.ultils.Information
import com.example.driver_booking_app.ultils.TripInformation
import com.example.driver_booking_app.viewModels.HomeViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.chip.Chip
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import java.util.concurrent.TimeUnit


enum class ViewControl{
    DISPLAY_RECEIVE_TRIP,
    DISPLAY_CONTROL_TRIP,
    HIDE_ALL
}
class HomeFragment : Fragment(){

    private lateinit var rootView:View
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var declineBtn: Chip
    private lateinit var onOffStripBtn: Chip
    //receive_trip_panel
    private lateinit var receiveTripPanel: CardView
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var destinationReceiveTexView: TextView
    private lateinit var acceptBtn: Button

    //control_trip_panel
    private lateinit var controlTripPanel: CardView
    private lateinit var passengerNameTextView: TextView
    private lateinit var passengerPhoneNumberTextView: TextView
    private lateinit var pickUpLocationTextview: TextView
    private lateinit var destinationLocationTextView: TextView
    private lateinit var startBtn: Button
    // This property is only valid between onCreateView and
    val AUTOCOMPLETE_REQUEST_CODE = 1
    var isOnStrip = false
    var isRecieveStrip = false
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
        Places.initialize(requireContext(),"AIzaSyAB70onimU_ofrLKNnrK5VFN3TAUjONoA4")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as GoogleMap
        declineBtn  = rootView.findViewById(R.id.decline_btn)
        onOffStripBtn = rootView.findViewById(R.id.start_find_trip_btn)

        //receive_trip_panel
        receiveTripPanel = rootView.findViewById(R.id.receive_trip_panel)
        circularProgressBar = receiveTripPanel.findViewById(R.id.circularProgressBar)
        acceptBtn = receiveTripPanel.findViewById(R.id.accept_btn)
        destinationReceiveTexView = receiveTripPanel.findViewById(R.id.destination_address_text)

        //control_trip_panel
        controlTripPanel = rootView.findViewById(R.id.control_trip_panel)
        passengerNameTextView = controlTripPanel.findViewById(R.id.passenger_name_text_view)
        passengerPhoneNumberTextView = controlTripPanel.findViewById(R.id.passenger_phone_number_text_view)
        pickUpLocationTextview = controlTripPanel.findViewById(R.id.pick_me_address)
        destinationLocationTextView = controlTripPanel.findViewById(R.id.destinaton_address)
        startBtn = controlTripPanel.findViewById(R.id.start_btn)

        setOnClickListener()
        setProgressChange(false)
        getMapAsync()
    }
    private fun waitingStripLogic(){
        val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
        val req = TripResquest(
           "driver",
            Information.username,
        )
        GlobalScope.launch(Dispatchers.IO){
            try{
                var isHaveATrip = false
                while(!isHaveATrip && isOnStrip){
                    val res = api.findStrip( Information.token, req).awaitResponse()
                    if(res.code() == 200){
                        Log.i("ASD", res.body().toString())
                        val tripRequest = TripInformationRequest("driver", Information.username)
                        val tripInfoResponse = api.getTripInformation(Information.token, tripRequest).awaitResponse()
                        if(!res.body()!!.driver.equals("") && !res.body()!!.passenger.isNullOrEmpty()){
                            isHaveATrip = true
                            withContext(Dispatchers.Main){
                                if(TripInformation.driver.equals("")){
                                    TripInformation.driver = Information.username
                                    TripInformation.passenger = res.body()!!.passenger
                                    TripInformation.destAddress = tripInfoResponse.body()!!.information!!.dest_address
                                    TripInformation.originAddress = tripInfoResponse.body()!!.information!!.origin_address
                                    TripInformation.destLat = tripInfoResponse.body()!!.information!!.dest_lat
                                    TripInformation.destLong = tripInfoResponse.body()!!.information!!.dest_long
                                    TripInformation.originLat = tripInfoResponse.body()!!.information!!.origin_lat
                                    TripInformation.originLong = tripInfoResponse.body()!!.information!!.origin_long
                                    destinationReceiveTexView.setText(tripInfoResponse.body()!!.information!!.dest_address)
                                    controlPanel(ViewControl.DISPLAY_RECEIVE_TRIP)
                                    circularProgressBar.apply {
                                        setProgressWithAnimation(100f, 5000)
                                    }
                                } else if(TripInformation.isStart.equals("false")){
                                    controlPanel(ViewControl.DISPLAY_CONTROL_TRIP)
                                }
                                Log.i("ASD", "HAVE A TRIP")
                            }
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                    Log.i("ASD",res.code().toString())
                }
            } catch (e: Exception){
                Log.i("ASD", e.message.toString())
                var isHaveATrip = false
                while(!isHaveATrip && isOnStrip){
                    val res = api.findStrip( Information.token, req).awaitResponse()
                    if(res.code() == 200){
                        Log.i("ASD", res.body().toString())
                        val tripRequest = TripInformationRequest("driver", Information.username)
                        val tripInfoResponse = api.getTripInformation(Information.token, tripRequest).awaitResponse()
                        if(!res.body()!!.driver.equals("") && !res.body()!!.passenger.isNullOrEmpty()){
                            isHaveATrip = true
                            withContext(Dispatchers.Main){
                                if(TripInformation.driver.equals("")){
                                    TripInformation.driver = Information.username
                                    TripInformation.passenger = res.body()!!.passenger
                                    TripInformation.destAddress = tripInfoResponse.body()!!.information!!.dest_address
                                    TripInformation.originAddress = tripInfoResponse.body()!!.information!!.origin_address
                                    TripInformation.destLat = tripInfoResponse.body()!!.information!!.dest_lat
                                    TripInformation.destLong = tripInfoResponse.body()!!.information!!.dest_long
                                    TripInformation.originLat = tripInfoResponse.body()!!.information!!.origin_lat
                                    TripInformation.originLong = tripInfoResponse.body()!!.information!!.origin_long
                                    destinationReceiveTexView.setText(tripInfoResponse.body()!!.information!!.dest_address)
                                    controlPanel(ViewControl.DISPLAY_RECEIVE_TRIP)
                                    circularProgressBar.apply {
                                        setProgressWithAnimation(100f, 5000)
                                    }
                                } else if(TripInformation.isStart.equals("false")){
                                    controlPanel(ViewControl.DISPLAY_CONTROL_TRIP)
                                }
                                Log.i("ASD", "HAVE A TRIP")
                            }
                        }
                    }
                    delay(TimeUnit.SECONDS.toMillis(2))
                    Log.i("ASD",res.code().toString())
                }
            }


        }
    }
    private fun setProgressChange(isNull: Boolean){
        if(isNull){
            circularProgressBar.onProgressChangeListener = null
            return
        }
        circularProgressBar.onProgressChangeListener = { progressBar ->
            if(progressBar.equals(100f)){
                isRecieveStrip = false
                TripInformation.driver = ""
                TripInformation.passenger = ""
                val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
                val leftTrip = Account(
                    Information.username,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "driver",
                )
                GlobalScope.launch(Dispatchers.IO) {
                    val leftRes = api.leaveTrip(Information.token, leftTrip).awaitResponse()
                    if(leftRes.code() == 200){
                        if(leftRes.body()!!.message.equals("driver leaved")){
                            withContext(Dispatchers.Main){
                                Toast.makeText(requireContext(), "CANCEL", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                controlPanel(ViewControl.HIDE_ALL)
            }
        }
    }

    private fun controlPanel(view: ViewControl){
        when(view){
            ViewControl.DISPLAY_RECEIVE_TRIP ->  {
                displayReceiveTrip()
                controlTripPanel.visibility = View.GONE
            }

            ViewControl.HIDE_ALL -> {
                hideReceiveTrip()
                controlTripPanel.visibility = View.GONE
            }

            ViewControl.DISPLAY_CONTROL_TRIP -> {
                controlTripPanel.visibility = View.VISIBLE
                hideReceiveTrip()
            }
        }
    }

    private fun displayReceiveTrip(){
        receiveTripPanel.visibility = View.VISIBLE
        declineBtn.visibility = View.VISIBLE
    }

    private fun hideReceiveTrip(){
        receiveTripPanel.visibility = View.GONE
        declineBtn.visibility = View.GONE
    }

    private fun setOnClickListener(){
        acceptBtn.setOnClickListener {
            isRecieveStrip = true
            startBtn.setText("Start")
            val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
            val updateGpsReq = UpdateGpsRequest(
                Information.username,
                "driver",
            )
            controlPanel(ViewControl.DISPLAY_CONTROL_TRIP)
            GlobalScope.launch(Dispatchers.IO) {
                val updateGpsRes = api.updateGps(Information.token, updateGpsReq).awaitResponse()
                if(updateGpsRes.code() == 200){
                    if(updateGpsRes.body()!!.message.equals("Updated")){
                        var acc = Account(
                            TripInformation.passenger,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "passenger"
                        )
                        var passengerInfo = api.getProfilePassenger(Information.token, acc).awaitResponse()
                        Log.i("ASD", passengerInfo.body().toString())
                        Log.i("ASD", TripInformation.destAddress .toString())
                        withContext(Dispatchers.Main){
                            passengerNameTextView.setText(passengerInfo.body()!!.passengers[0].username)
                            passengerPhoneNumberTextView.setText(passengerInfo.body()!!.passengers[0].phone)
                            destinationLocationTextView.setText(TripInformation.destAddress)
                            pickUpLocationTextview.setText(TripInformation.originAddress)
                        }
                    }
                }
            }
            setProgressChange(true)
        }

        declineBtn.setOnClickListener {
            TripInformation.driver = ""
            TripInformation.passenger = ""
            isRecieveStrip = false
            val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
            val leftTrip = Account(
                Information.username,
                "",
                "",
                "",
                "",
                "",
                "driver",
            )
            GlobalScope.launch(Dispatchers.IO) {
                val leftRes = api.leaveTrip(Information.token, leftTrip).awaitResponse()
                if(leftRes.code() == 200){
                    if(leftRes.body()!!.message.equals("driver leaved")){
                        withContext(Dispatchers.Main){
                            Toast.makeText(requireContext(), "CANCEL", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            controlPanel(ViewControl.HIDE_ALL)
            setProgressChange(true)
        }

        onOffStripBtn.setOnClickListener {
            if(isOnStrip){
                if(isRecieveStrip){
                    Toast.makeText(requireContext(), "You are having a trip", Toast.LENGTH_SHORT).show()
                } else {
                    onOffStripBtn.setText("Click to on")
                    isOnStrip = false
                }

            } else {
                if(!isRecieveStrip){
                    isOnStrip = true
                    onOffStripBtn.setText("Click to off")
                    waitingStripLogic()
                } else {
                    Toast.makeText(requireContext(), "You are having a trip", Toast.LENGTH_SHORT).show()
                }

            }
        }

        startBtn.setOnClickListener {
            //controlPanel(ViewControl.HIDE_ALL)
            if(startBtn.text.equals("Complete trip")) {
                val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
                var acc = Account(
                    Information.username,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "driver"
                )
                GlobalScope.launch(Dispatchers.IO) {
                    val endTripRes = api.endTrip(Information.token, acc).awaitResponse()
                    if(endTripRes.code() == 200){
                        if(endTripRes.body()!!.message.equals("end trip")){
                            withContext(Dispatchers.Main){
                                isOnStrip = false
                                onOffStripBtn.setText("Click to on")
                                TripInformation.driver = ""
                                isRecieveStrip = false
                                findNavController().navigate(R.id.action_nav_home_to_nav_thanks)
                            }
                        }
                    }
                }
            } else {
                startBtn.setText("Complete trip")
                val api = com.example.driver_booking_app.ultils.Retrofit.createApi()
                val updateGpsReq = UpdateGpsRequest(
                    Information.username,
                    "driver",
                    "999",
                    "100"
                )
                Toast.makeText(requireContext(), "Start your uber", Toast.LENGTH_SHORT).show()
                GlobalScope.launch(Dispatchers.IO) {
                    val updateGpsRes = api.updateGps(Information.token, updateGpsReq).awaitResponse()
                    if(updateGpsRes.code() == 200){
                        if(updateGpsRes.body()!!.message.equals("Updated")){
                            var acc = Account(
                                TripInformation.passenger,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "passenger"
                            )
                        }
                    }
                }
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

}