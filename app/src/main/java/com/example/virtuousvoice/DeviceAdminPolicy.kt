package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DeviceAdminPolicy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_admin_policy)

        val intent = Intent(this, WelcomeScreen::class.java)
        startActivity(intent)
        finish()

    }
}