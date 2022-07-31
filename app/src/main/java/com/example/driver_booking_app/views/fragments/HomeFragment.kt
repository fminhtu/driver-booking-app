package com.example.driver_booking_app.views.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.GoogleMap
import com.example.driver_booking_app.viewModels.HomeViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

class HomeFragment : Fragment(){

    private lateinit var rootView:View
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var locationPanel: ConstraintLayout
    private lateinit var bookRidePanel: ConstraintLayout
    private lateinit var pickMeLocation: AppCompatEditText
    private lateinit var destintionLocation: AppCompatEditText
    val AUTOCOMPLETE_REQUEST_CODE = 1
    //location pannel
    private lateinit var confirmLocationBtn: Button

    //bookRide pannel
    private lateinit var bookRideBtn: Button

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
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as GoogleMap
        Places.initialize(requireContext(),"AIzaSyAB70onimU_ofrLKNnrK5VFN3TAUjONoA4")
        locationPanel = rootView.findViewById(R.id.location_confirm_panel)
        bookRidePanel = rootView.findViewById(R.id.book_ride_panel)

        confirmLocationBtn = locationPanel.findViewById(R.id.confirm_location_button)
        bookRideBtn = bookRidePanel.findViewById(R.id.book_ride_btn)
        pickMeLocation = locationPanel.findViewById(R.id.pickUpTextView)
        destintionLocation = locationPanel.findViewById(R.id.destination_location)

        pickMeLocation.setOnClickListener {


            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = Arrays.asList(Place.Field.ID,Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME,
                )
            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("VN")
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        confirmLocationBtn.setOnClickListener {
            locationPanel.visibility = View.GONE
            bookRidePanel.visibility = View.VISIBLE
        }

        bookRideBtn.setOnClickListener {
            locationPanel.visibility = View.VISIBLE
            bookRidePanel.visibility = View.GONE
        }

        mapFragment.getMapAsync{
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i("ERROR", "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("ERROR", status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}