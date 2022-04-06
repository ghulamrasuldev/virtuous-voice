package com.example.virtuousvoice.Services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Views.TabbedActivity
import com.example.virtuousvoice.utilties.Common.ACTIVE_STATUS
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.MAX_GAP_TIME
import com.example.virtuousvoice.utilties.Common.PARENT_CHECK_TIME
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

const val TAG2 = "Child Service"
class ParentService: Service() {

    val db = FirebaseFirestore.getInstance()
    private val channelId = "Notification from Service"
    var parent_name = userName
    override fun onBind(intent: Intent?): IBinder? = null

    //OnBind Function Implementation
    init {
        Log.d(TAG2, "Started Service!")
    }

    //onCreate Method Implementation

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }
    }

    //OnStartCommand Override
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread{
            while (true){
                checkStatus()
                Thread.sleep(PARENT_CHECK_TIME)
            }
        }.start()
        return START_STICKY
    }

    private fun checkStatus() {
        val calendar: Calendar = Calendar.getInstance()
        var list = ArrayList<String>()
        db.collection(LINKED_CHILDS)
            .whereEqualTo(USER_PHONE, userPhone)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val startTime: Long = calendar.getTimeInMillis()
                    val diff = startTime - (document.data[ACTIVE_STATUS] as Long)
                    Log.d("TAG", "Time Difference : $diff")
                    Log.d("TAG", "${document.data[USER_NAME].toString()}")
                    if (diff> MAX_GAP_TIME){
                        Log.d("TAG", "Entered IFF")
                        list.add(document.data[USER_NAME].toString())
                    }
                }

                for (name in list){
                    Log.d("TAG: ", "$name is not active")
                    val notificationIntent = Intent(this, TabbedActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        0, notificationIntent, 0
                    )
                    val notification: Notification = NotificationCompat.Builder(this, channelId)
                        .setContentTitle("Hi $parent_name")
                        .setContentText("Your child $name is not active.")
                        .setSmallIcon(R.drawable.icon_child)
                        .setContentIntent(pendingIntent)
                        .build()
                    startForeground(1, notification)
                }
            }
    }


}