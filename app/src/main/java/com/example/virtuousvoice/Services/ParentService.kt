package com.example.virtuousvoice.Services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Views.MainActivity
import com.example.virtuousvoice.Views.TabbedActivity
import com.example.virtuousvoice.utilties.Common.ACTIVE_STATUS
import com.example.virtuousvoice.utilties.Common.CHILD_NAME
import com.example.virtuousvoice.utilties.Common.LAST_NOTIFIED
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.MAX_GAP_TIME
import com.example.virtuousvoice.utilties.Common.PARENT_CHECK_TIME
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.childData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

const val TAG2 = "Child Service"
class ParentService: Service() {

    var childs = ArrayList<childData>()
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

    }

    //OnStartCommand Override
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        parmanentNotification(parent_name)
        Thread{
            while (true){
                checkStatus()
                Thread.sleep(PARENT_CHECK_TIME)
            }
        }.start()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkStatus() {
        var i = 0
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
                    Log.d("TAG", document.data[USER_NAME].toString())
                    if ((calendar.timeInMillis - document.data[LAST_NOTIFIED] as Long ) > 300000 && diff > MAX_GAP_TIME){
                        val notificationText = "Your child ${document.data[USER_NAME]} is not active\n"
                        generateNotification(parent_name, notificationText, (1000..9999999).random())
                        db.collection(LINKED_CHILDS).document(document.id).update(
                            LAST_NOTIFIED, calendar.timeInMillis)
                    }
                    Log.d("TAG", "${document.data[USER_NAME]} is added to List")
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateNotification(parent_name: String, notificationText:String, id: Int) {
        val intent = Intent(this, TabbedActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon_child)
            .setContentTitle("Hi $parent_name")
            .setContentText(notificationText)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(id, notification.build())
        }
    }


    private fun parmanentNotification(parent_name: String) {
        val notification=NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.icon_child)
            .setContentTitle("Hi $parent_name")
            .setContentText("Application service running in the background")
            .build()
        startForeground(1,notification)
    }

}