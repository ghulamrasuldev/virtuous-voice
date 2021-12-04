package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //taking user to Parent signup screen
        _sign_up_as_parent.setOnClickListener{
            val intent = Intent(this, ParentSignup::class.java)
            startActivity(intent)
            finish()
        }
    }
}