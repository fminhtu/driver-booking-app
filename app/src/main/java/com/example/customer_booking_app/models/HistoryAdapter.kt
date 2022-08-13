package com.example.customer_booking_app.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customer_booking_app.R

class HistoryAdapter(var context: Context): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    var onItemClick:((Int) -> Unit)? = null
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var date_book_car: TextView = listItemView.findViewById(R.id.date_book_car)
        var time_book_car: TextView = listItemView.findViewById(R.id.time_book_car)
        var pick_me_address: TextView = listItemView.findViewById(R.id.pick_me_address)
        var destinaton_address: TextView = listItemView.findViewById(R.id.destinaton_address)
        init {
            listItemView.setOnClickListener { onItemClick?.invoke(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.history_item, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date_book_car.text = "20/11/2022"
//        holder.time_book_car.text = serviceList.get(position).servicePhoneNumber
//        holder.pick_me_address.text = serviceList.get(position).servicePhoneNumber
//        holder.destinaton_address.text = serviceList.get(position).servicePhoneNumber
    }

    override fun getItemCount(): Int {
        return 5
    }



}