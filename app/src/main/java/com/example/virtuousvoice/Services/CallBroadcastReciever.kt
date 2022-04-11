package com.example.virtuousvoice.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallBroadcastReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, FloatingService::class.java)

        when (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                context?.startService(serviceIntent)
            }
            TelephonyManager.EXTRA_STATE_IDLE -> {
                context?.stopService(serviceIntent)
            }
        }

    }
}