package com.example.virtuousvoice.Adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.virtuousvoice.DataClasses.LinkedChildData
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common.DATE
import com.example.virtuousvoice.utilties.Common.DAY
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.linked_child_card.view.*

class LinkedChildAdaptor(private val childList: List<LinkedChildData>): RecyclerView.Adapter<LinkedChildAdaptor.LinkedChildHolder>() {
    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore
    class LinkedChildHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username = itemView._user_name
        val date = itemView._captured_date
        val day = itemView._captured_day
        val delete_button = itemView._delete_child
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


        holder.delete_button.setOnClickListener {
            db.collection(LINKED_CHILDS).whereEqualTo(USER_NAME, currentItem.username).get().addOnSuccessListener { documents->
                for (document in documents){
                    if (document.data[DATE] == currentItem.date && currentItem.day == document.data[DAY]){
                        db.collection(LINKED_CHILDS).document(document.id).delete()
                    }
                }

            }
        }
    }

    override fun getItemCount() = childList.size
}