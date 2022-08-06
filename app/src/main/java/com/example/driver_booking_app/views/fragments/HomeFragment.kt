package com.example.driver_booking_app.views.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
enum class PANEL{
    LOCATION, BOOK_RIDE, WAITING_DRIVER
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
        Places.initialize(requireContext(),"AIzaSyAB70onimU_ofrLKNnrK5VFN3TAUjONoA4")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as GoogleMap
        locationPanel = rootView.findViewById(R.id.location_confirm_panel)
        bookRidePanel = rootView.findViewById(R.id.book_ride_panel)
        waitingDriverPanel = rootView.findViewById(R.id.waiting_driver_panel)

        confirmLocationBtn = locationPanel.findViewById(R.id.confirm_location_button)
        bookRideBtn = bookRidePanel.findViewById(R.id.book_ride_btn)
        fourSeedLabel = bookRidePanel.findViewById(R.id.four_seeder_label)
        sevenSeedLabel = bookRidePanel.findViewById(R.id.seven_seeder_label)
        pickMeLocation = locationPanel.findViewById(R.id.pickUpTextView)
        destintionLocation = locationPanel.findViewById(R.id.destination_location)
        cancelFindingDriverBtn = rootView.findViewById(R.id.cancel_finding_driver_btn)

        setOnclickLisner()
        getMapAsync()
    }

    private fun setOnclickLisner(){
        pickMeLocation.setOnClickListener { getAutoCompletePlace() }
        confirmLocationBtn.setOnClickListener { activePanel(PANEL.BOOK_RIDE) }
        bookRideBtn.setOnClickListener { activePanel(PANEL.WAITING_DRIVER) }
        cancelFindingDriverBtn.setOnClickListener { activePanel(PANEL.LOCATION) }
        fourSeedLabel.setOnClickListener { activeSeed(SEED.FOUR) }
        sevenSeedLabel.setOnClickListener { activeSeed(SEED.SEVEN) }

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

    private fun getAutoCompletePlace(){
        val fields = Arrays.asList(Place.Field.ID,Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME,)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountry("VN")
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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
            }
            PANEL.LOCATION -> {
                locationPanel.visibility = View.VISIBLE
                waitingDriverPanel.visibility = View.GONE
                bookRidePanel.visibility = View.GONE
            }
            PANEL.WAITING_DRIVER -> {
                waitingDriverPanel.visibility = View.VISIBLE
                locationPanel.visibility = View.GONE
                bookRidePanel.visibility = View.GONE
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