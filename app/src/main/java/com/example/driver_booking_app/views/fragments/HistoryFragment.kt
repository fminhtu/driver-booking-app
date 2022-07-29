package com.example.driver_booking_app.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.driver_booking_app.R
import com.example.driver_booking_app.databinding.FragmentHistoryBinding
import com.example.driver_booking_app.models.HistoryAdapter
import com.example.driver_booking_app.viewModels.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textSlideshow
        historyViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }

        val recyclerView = root.findViewById<RecyclerView>(R.id.history_recycler_view)
        val adapter = HistoryAdapter(requireContext())
        recyclerView.adapter = adapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}