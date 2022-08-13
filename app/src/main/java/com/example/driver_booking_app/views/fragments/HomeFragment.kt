package com.example.driver_booking_app.views.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.GoogleMap
import com.example.driver_booking_app.viewModels.HomeViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.chip.Chip
import com.mikhaellopez.circularprogressbar.CircularProgressBar


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
    //receive_trip_panel
    private lateinit var receiveTripPanel: CardView
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var acceptBtn: Button

    //control_trip_panel
    private lateinit var controlTripPanel: CardView
    private lateinit var startBtn: Button
    // This property is only valid between onCreateView and
    val AUTOCOMPLETE_REQUEST_CODE = 1

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
        //receive_trip_panel
        receiveTripPanel = rootView.findViewById(R.id.receive_trip_panel)
        circularProgressBar = receiveTripPanel.findViewById(R.id.circularProgressBar)
        acceptBtn = receiveTripPanel.findViewById(R.id.accept_btn)
        circularProgressBar.apply {
            setProgressWithAnimation(100f, 5000)
        }

        //control_trip_panel
        controlTripPanel = rootView.findViewById(R.id.control_trip_panel)
        startBtn = controlTripPanel.findViewById(R.id.start_btn)
        setOnClickListener()
        setProgressChange(false)
        getMapAsync()
    }

    private fun setProgressChange(isNull: Boolean){
        if(isNull){
            circularProgressBar.onProgressChangeListener = null
            return
        }
        circularProgressBar.onProgressChangeListener = { progressBar ->
            if(progressBar.equals(100f)){
                Toast.makeText(requireContext(), "CANCLE", Toast.LENGTH_SHORT).show()
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
            controlPanel(ViewControl.DISPLAY_CONTROL_TRIP)
            setProgressChange(true)
        }

        declineBtn.setOnClickListener {
            controlPanel(ViewControl.HIDE_ALL)
            setProgressChange(true)
        }

        startBtn.setOnClickListener {
            controlPanel(ViewControl.HIDE_ALL)
            Toast.makeText(requireContext(), "Start your uber", Toast.LENGTH_SHORT).show()
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