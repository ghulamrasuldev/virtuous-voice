package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_pin.*

class CreatePin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)

        _btn_confirm_pin.setOnClickListener{
            val intent = Intent(this, DeviceAdminPolicy::class.java)
            startActivity(intent)
            finish()
        }
    }
}