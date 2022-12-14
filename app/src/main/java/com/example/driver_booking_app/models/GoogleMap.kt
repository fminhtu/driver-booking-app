package com.example.driver_booking_app.models


import android.graphics.Color
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.driver_booking_app.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class GoogleMap : SupportMapFragment(), OnMapReadyCallback {
    private lateinit var  gmap: GoogleMap
    private val api_key = "AIzaSyD-HPqZ6715o4r5STSx5mGtlx8vqjTLZNc"
    override fun onMapReady(p0: GoogleMap) {
        gmap = p0
        gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.uber_maps_style))


        val hcmus = LatLng(10.76253285, 106.68228373754832)
        val from: LatLng = LatLng(10.7970171,106.7031929)
        val to: LatLng = LatLng(10.87038795,106.77833429198822)
        markerFrom(from, "UEF")
        markerTo(to, "UEL")

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 11.0f))


        gmap.setOnMapClickListener { latLng ->
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
        }

//        route(from, to)
    }

    private fun markerFrom(location: LatLng, address: String) {
        val marker = gmap.addMarker(MarkerOptions()
            .position(location)
            .title(address))

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11.5f))
    }

    private fun markerTo(location: LatLng, address: String) {
        var marker = gmap.addMarker(MarkerOptions()
            .position(location)
            .title(address)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

    }
    //"https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=<YOUR_API_KEY>"
    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    private fun route(from: LatLng, to: LatLng) {
        val path: MutableList<List<LatLng>> = ArrayList()
        val urlDirections = getDirectionURL(from, to, api_key)

        val directionsRequest = object : StringRequest(Method.GET, urlDirections, Response.Listener<String> {
                response ->
            val jsonResponse = JSONObject(response)
            // Get routes
            val routes = jsonResponse.getJSONArray("routes")
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val steps = legs.getJSONObject(0).getJSONArray("steps")
            for (i in 0 until steps.length()) {
                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                path.add(PolyUtil.decode(points))
            }
            for (i in 0 until path.size) {
                this.gmap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
            }
        }, Response.ErrorListener {
                _ ->
        }){}

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(directionsRequest)
        Log.d("message", directionsRequest.toString())
    }



    init {
        getMapAsync(this)
    }
}