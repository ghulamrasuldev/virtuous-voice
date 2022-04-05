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
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.R
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.status
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.Common.userType
import kotlinx.android.synthetic.main.activity_child_signup.*

class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = Common.APP_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.readAllData.observe(this, androidx.lifecycle.Observer{ producuts->

            for (product in producuts){
                userType = product.userType
                userEmail = product.userEmail
                Log.d("TAG: ", product.userName)
                userName = product.userName
                status = product.LoginStatus
                userPhone = product.userPhone
            }

        })

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}