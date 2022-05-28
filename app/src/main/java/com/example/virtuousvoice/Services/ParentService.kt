package com.example.virtuousvoice.Services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.app.NotificationManagerCompat
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Views.TabbedActivity
import com.example.virtuousvoice.utilties.Common.ACTIVE_STATUS
import com.example.virtuousvoice.utilties.Common.CHILD_NAME
import com.example.virtuousvoice.utilties.Common.LAST_NOTIFIED
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.MAX_GAP_TIME
import com.example.virtuousvoice.utilties.Common.NEW_TO_SERVICE
import com.example.virtuousvoice.utilties.Common.PARENT_CHECK_TIME
import com.example.virtuousvoice.utilties.Common.TOXIC_AUDIO_COLLECTION
import com.example.virtuousvoice.utilties.Common.TOXIC_DATA
import com.example.virtuousvoice.utilties.Common.TOXIC_STATUS
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.childData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager


const val TAG2 = "Child Service"
class ParentService: Service() {

    var childs = ArrayList<childData>()
    val db = FirebaseFirestore.getInstance()
    private val channelId = "VirtuousVoice"
    var parent_name = userName


    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val description = "Test notification"


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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        parmanentNotification(parent_name)
        Thread{
            while (true){
                checkStatus()
                checkNewToxicData()
                Thread.sleep(PARENT_CHECK_TIME)
            }
        }.start()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNewToxicData() {
        db.collection(TOXIC_AUDIO_COLLECTION)
            .whereEqualTo(NEW_TO_SERVICE, true)
            .get()
            .addOnSuccessListener { documents->
                for(document in documents){
                    if (document.data[USER_PHONE]==userPhone &&document.data[TOXIC_STATUS]!=null && document.data[NEW_TO_SERVICE]==true){
                        generateNotification(parent_name,
                            "Toxicity Found in your child's (${document.data[USER_NAME]}) call.",
                            (9999999..99999999).random())
                        db.collection(TOXIC_AUDIO_COLLECTION).document(document.id).update(
                            NEW_TO_SERVICE, false)
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                    Log.d("TAG", document.data[LAST_NOTIFIED].toString())
                    if ((calendar.timeInMillis - (document.data[LAST_NOTIFIED] as Long)) > 300000 && diff > MAX_GAP_TIME){
                        val notificationText = "Your child ${document.data[USER_NAME]} is not active\n"
                        generateNotification(parent_name, notificationText, (1000..9999999).random())
                        db.collection(LINKED_CHILDS).document(document.id).update(
                            LAST_NOTIFIED, calendar.timeInMillis)
                    }
                    Log.d("TAG", "${document.data[USER_NAME]} is added to List")
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateNotification(parent_name: String, notificationText:String, id: Int) {
        val intent = Intent(this, TabbedActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hi $parent_name")
                .setContentText(notificationText)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(id, builder.build())





//        val intent = Intent(this, TabbedActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.icon_child)
//            .setContentTitle("Hi $parent_name")
//            .setContentText(notificationText)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//        with(NotificationManagerCompat.from(this)) {
//            notify(id, notification.build())
//        }
    }


    private fun parmanentNotification(parent_name: String) {

        val intent = Intent(this, TabbedActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, "Hello")
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentText("Running Virtuous Voice")
            .setSmallIcon(R.drawable.icon_child)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("Hello",
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        startForeground(9999, notification)
    }



}