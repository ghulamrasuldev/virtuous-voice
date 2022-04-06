package com.example.virtuousvoice.Services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.ACTIVE_STATUS
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.MAX_GAP_TIME
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.userPhone
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

const val TAG2 = "Child Service"
class ParentService: Service() {

    val db = FirebaseFirestore.getInstance()
    override fun onBind(intent: Intent?): IBinder? = null

    //OnBind Function Implementation
    init {
        Log.d(TAG2, "Started Service!")
    }

    //OnStartCommand Override
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread{
            while (true){
                checkStatus()
                Thread.sleep(Common.CHILD_UPDATE_TIME)
            }
        }.start()
        return START_STICKY
    }

    private fun checkStatus() {
        val calendar: Calendar = Calendar.getInstance()
        lateinit var list: ArrayList<String>
        db.collection(LINKED_CHILDS)
            .whereEqualTo(USER_PHONE, userPhone)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val startTime: Long = calendar.getTimeInMillis()
                    val diff = startTime - (document.data[ACTIVE_STATUS] as Long)
                    if (diff> MAX_GAP_TIME){
                        list.add(document.data[USER_NAME].toString())
                    }
                }
            }

        if (list.isNotEmpty()){
            for (name in list){
                Log.d("TAG: ", "$name is not active")
            }
        }
    }
}