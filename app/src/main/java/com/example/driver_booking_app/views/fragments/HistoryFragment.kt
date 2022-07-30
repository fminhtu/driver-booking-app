package com.example.driver_booking_app.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.driver_booking_app.R
import com.example.driver_booking_app.models.HistoryAdapter
import com.example.driver_booking_app.viewModels.HistoryViewModel

class HistoryFragment : Fragment() {
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        val rootView = inflater.inflate(R.layout.fragment_history, container, false)

        val recyclerView_history = rootView.findViewById<RecyclerView>(R.id.history_recycler_view)
//        emptyServiceTextView = rootView.findViewById(R.id.service_empty_text_view)
//        serviceViewModel.setServiceList()
        val adapter = HistoryAdapter(requireContext())
        recyclerView_history.adapter = adapter
        adapter.onItemClick = {position ->
            val bundle = Bundle()
//            bundle.putString("serviceID", serviceViewModel.selectedServiceList.value!!.get(position).serviceID)
            findNavController().navigate(R.id.action_nav_history_to_nav_detail_transport, bundle)
        }
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}