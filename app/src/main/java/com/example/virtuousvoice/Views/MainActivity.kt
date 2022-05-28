package com.example.virtuousvoice.Views

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.R
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.status
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.Common.userType
import kotlinx.android.synthetic.main.activity_child_signup.*
import androidx.core.app.ActivityCompat


import android.app.Activity
import android.content.pm.PackageManager
import com.google.android.gms.security.ProviderInstaller
import javax.net.ssl.SSLEngine

import javax.net.ssl.SSLContext





class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = Common.APP_NAME
    lateinit var mUserViewModel: userViewModel


    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProviderInstaller.installIfNeeded(getApplicationContext());

        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, null, null)
        val engine = sslContext.createSSLEngine()

        verifyStoragePermissions(this)

        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.readAllData.observe(this, androidx.lifecycle.Observer{ users->

            for (user in users){
                userType = user.userType
                userEmail = user.userEmail
                Log.d("TAG: ", user.userName)
                userName = user.userName
                status = user.LoginStatus
                userPhone = user.userPhone
            }

        })

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}