package com.example.virtuousvoice.Adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.virtuousvoice.DataClasses.LinkedChildData
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.linked_child_card.view.*

class LinkedChildAdaptor(private val childList: List<LinkedChildData>): RecyclerView.Adapter<LinkedChildAdaptor.LinkedChildHolder>() {
    class LinkedChildHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username = itemView._user_name
        val date = itemView._captured_date
        val day = itemView._captured_day
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkedChildHolder {
        val itemView = LayoutInflater.from(parent.context ).inflate(R.layout.linked_child_card,parent,false)
        return LinkedChildHolder(itemView)
    }

    override fun onBindViewHolder(holder: LinkedChildHolder, position: Int) {
        val currentItem = childList[position]
        holder.username.text = currentItem.username
        holder.date.text = currentItem.date
        holder.day.text = currentItem.day
    }

    override fun getItemCount() = childList.size
}