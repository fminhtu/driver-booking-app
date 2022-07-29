package com.example.driver_booking_app.views.fragments

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment



class MapFragment : SupportMapFragment(), OnMapReadyCallback {
    private var gmap: GoogleMap? = null
    override fun onMapReady(p0: GoogleMap) {
        gmap = p0

        gmap!!.setOnMapClickListener { latLng ->
            gmap!!.clear()
            gmap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
        }

    }
}