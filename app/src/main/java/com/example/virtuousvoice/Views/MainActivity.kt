package com.example.virtuousvoice.Views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userType

class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = Common.APP_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedPref: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPref.apply()
        //UserType

        userType = sharedPreferences.getString(USER_TYPE, "").toString()
        userEmail = sharedPreferences.getString(USER_EMAIL, "").toString()
        userName = sharedPreferences.getString(USER_NAME, "").toString()

        Toast.makeText(this, sharedPreferences.getString(USER_TYPE, "1"), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, sharedPreferences.getString(USER_EMAIL, "2"), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, sharedPreferences.getString(USER_NAME, "3"), Toast.LENGTH_SHORT).show()

        if (userType == "" || userEmail == "" || userName ==""){
            userType = ""
            userEmail = ""
            userName = ""
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, WelcomeScreen::class.java)
                startActivity(intent)
                finish()
            },2000)
        }
        else{
            val intent = Intent(this, TabbedActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}