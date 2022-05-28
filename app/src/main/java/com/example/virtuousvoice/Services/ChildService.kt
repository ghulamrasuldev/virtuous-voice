package com.example.virtuousvoice.Services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.virtuousvoice.utilties.Common.ACTIVE_STATUS
import com.example.virtuousvoice.utilties.Common.CHILD_UPDATE_TIME
import com.example.virtuousvoice.utilties.Common.FUID
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

const val TAG = "Child Service"
class ChildService: Service() {
    val db = FirebaseFirestore.getInstance()
    val fuid = FUID
    //Nulling onBind
    override fun onBind(intent: Intent?): IBinder? = null

    //OnBind Function Implementation
    init {
        Log.d(TAG, "Started Service!")
    }

    //OnStartCommand Override
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread{
            while (true){
                updateStatus()
                Thread.sleep(CHILD_UPDATE_TIME)
            }
        }.start()
        return START_STICKY
    }

    private fun updateStatus() {
        val calendar: Calendar = Calendar.getInstance()
        val startTime: Long = calendar.getTimeInMillis()
        db.collection(LINKED_CHILDS).document(FUID).update(
            ACTIVE_STATUS, startTime
        ).addOnSuccessListener {
            Log.d("Active Status", "Updated")
        }
    }
}