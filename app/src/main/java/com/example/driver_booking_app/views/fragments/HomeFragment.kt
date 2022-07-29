package com.example.driver_booking_app.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.driver_booking_app.R
import com.example.driver_booking_app.viewModels.ProfileViewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        //initComponent()
        return rootView
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}