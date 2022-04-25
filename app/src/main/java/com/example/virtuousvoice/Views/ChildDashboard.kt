package com.example.virtuousvoice.Views

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.Fragments.UpdateEmailFragment
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Services.ChildService
import com.example.virtuousvoice.Services.FloatingService
import com.example.virtuousvoice.Services.ParentService
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.FUID
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_dashboard.*
import kotlinx.android.synthetic.main.activity_link_child.*
import java.io.File
import java.util.concurrent.TimeUnit

class ChildDashboard : AppCompatActivity() {

    private val sharedPrefFile = "virtuousVoice"

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var number = ""
    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var sentToken : String = ""
    lateinit var credential: PhoneAuthCredential

    companion object {
        private const val DRAW_OVER_OTHER_APP_PERMISSION = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_dashboard)
        updatePreferences()



        Intent(this, ChildService::class.java).also {
            Log.d("Starting: ", "Service")
            startService(it)
        }

        askForPermissions()
        checkAndCreateDirectory()

        auth = Firebase.auth
        hi_there.setText("Hi $userName")
        val updateEmailFragment = UpdateEmailFragment()

        if (userEmail != ""){
            _update_email_info.isVisible = false
        }


        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d("GFG" , "onVerificationCompleted Success")
                child_dashboard_progress_bar.isVisible = false
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
                child_dashboard_progress_bar.isVisible = false
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                sentToken = verificationId.toString()
                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
                child_dashboard_progress_bar.isVisible = false
                VERIFY_ADD_NUMBER_SECTION.isVisible= false
                CHILD_DASHBOARD_OTP_SECTION.isVisible = true
            }
        }

        //Listeners
        _update_email_info.setOnClickListener {
            _add_child_info_notification.isVisible = false
            updateEmailFragment.show(this.supportFragmentManager, "Bottom Sheet Update Email")
        }

        _btn_logout.setOnClickListener {
            hi_there.isVisible = false
            _add_child_info_notification.isVisible = false
            child_instructions.isVisible = false
            _btn_logout.isVisible = false
            VERIFY_ADD_NUMBER_SECTION.isVisible = true
        }

        child_dashboard_back.setOnClickListener{
            hi_there.isVisible = true
            _btn_logout.isVisible = true
            _add_child_info_notification.isVisible = true
            child_instructions.isVisible = true
            VERIFY_ADD_NUMBER_SECTION.isVisible = false
        }

        _btn_verify_number.setOnClickListener {
            child_dashboard_progress_bar.isVisible = true
            number = "+92"+child_dashboard_number.text.toString()
            sendVerificationCode(number)
        }

        _child_dashboard_verify_otp.setOnClickListener {
            child_dashboard_progress_bar.isVisible = true
            Toast.makeText(this, sentToken, Toast.LENGTH_SHORT).show()
            val credential = PhoneAuthProvider.getCredential(sentToken, _child_dashboard_otp.text.toString())
            signInWithPhoneAuthCredential(credential)
        }

    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = task.result?.user

                    removeUser()
                    Intent(this, ChildService::class.java).also {
                        Log.d("Starting: ", "Service")
                        stopService(it)
                    }
                    val intent = Intent(this, WelcomeScreen::class.java)
                    startActivity(intent)
                    finishAffinity()

                    Log.d("TAG: ", auth.currentUser.toString())
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }



    private fun removeUser(){
        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.updateUser(
            userTable(
                0,
                "",
                "",
                "",
                "",
                false,
                FUID
            )
        )
        Common.userPhone = ""
        Common.status = false
        Common.userType = ""
        Common.userEmail = ""
        Common.userName = ""
    }


    private fun checkAndCreateDirectory() {
        val file = File(FloatingService.FOLDER_PATH)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    private fun askForPermissions() {
        // system overlay permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION)
        }

        val permissionNeeded = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsNotGranted = ArrayList<String>()

        for (permission in permissionNeeded) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNotGranted.add(permission)
            }
        }

        val array = arrayOfNulls<String>(permissionsNotGranted.size)

        if (array.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNotGranted.toArray(array), 0
            )
        }
    }


    fun updatePreferences(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedPref:SharedPreferences.Editor =  sharedPreferences.edit()
        //TOKEN
        sharedPref.putString(USER_TYPE, USER_TYPE_CHILD)
        sharedPref.apply()

        sharedPref.putString(USER_NAME, userName)
        sharedPref.apply()

        sharedPref.putString(USER_PHONE, userPhone)
        sharedPref.apply()
    }

}