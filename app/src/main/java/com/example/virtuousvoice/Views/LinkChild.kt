package com.example.virtuousvoice.Views

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.R
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.ACTIVE_STATUS
import com.example.virtuousvoice.utilties.Common.FUID
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.status
import com.example.virtuousvoice.utilties.Common.trimNumber
import com.example.virtuousvoice.utilties.Common.updateUser
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.Common.userType
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_signup.*
import kotlinx.android.synthetic.main.activity_child_signup._btn_link_child
import kotlinx.android.synthetic.main.activity_child_signup._btn_sign_up_verify
import kotlinx.android.synthetic.main.activity_child_signup._child_sign_up_parent_link
import kotlinx.android.synthetic.main.activity_child_signup._child_signup_verify_layout
import kotlinx.android.synthetic.main.activity_child_signup._link_child_name
import kotlinx.android.synthetic.main.activity_child_signup._sign_up_six_digit_code
import kotlinx.android.synthetic.main.activity_link_child.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class LinkChild : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var number = ""


    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var sentToken : String = ""
    lateinit var credential: PhoneAuthCredential


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_child)

        auth = Firebase.auth

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d("GFG" , "onVerificationCompleted Success")
                _progressBar_link_child.isVisible = false
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
                _progressBar_link_child.isVisible = false
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
                _progressBar_link_child.isVisible = false
                LINK_CHILD_SECTION.isVisible= false
                LINK_CHILD_OTP_SECTION.isVisible = true
            }
        }

        //Linking Parent Email
        _btn_link_child.setOnClickListener {
            _progressBar_link_child.isVisible = true
            AuthenticateWithPhone()
        }

        //Verify OTP
        _btn_link_child_verify_otp.setOnClickListener {
            Toast.makeText(this, sentToken, Toast.LENGTH_SHORT).show()
            val credential = PhoneAuthProvider.getCredential(sentToken, _link_child_otp.text.toString())
            signInWithPhoneAuthCredential(credential)
        }


        _child_sign_up_parent_link.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finishAffinity()
        }

    }

    private fun AuthenticateWithPhone() {
        number = _link_child_parent_phone.text.toString()
        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty()){
            number = "+92${trimNumber(number)}"
            sendVerificationCode(number)
        }else{
            Toast.makeText(this,"Enter mobile number", Toast.LENGTH_SHORT).show()
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
                    saveUserInCLoud()
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

    private fun saveUserInCLoud() {
        _progressBar_link_child.isVisible = true
        var userExist = false
        db.collection(LINKED_CHILDS)
            .whereEqualTo(USER_PHONE, number)
            .get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    if (document.data[USER_NAME] == _link_child_name.text.toString()){
                        userExist = true
                        userType = USER_TYPE_CHILD
                        userName = document.data[USER_NAME].toString()
                        userEmail = document.data[USER_EMAIL].toString()
                        userPhone = document.data[USER_PHONE].toString()
                        status = true
                        FUID = document.id
                    }
                }

                if (userExist){
                    updateUser(
                        userPhone,
                        true,
                        USER_TYPE_CHILD,
                        userEmail,
                        userName,
                        this,
                        FUID
                    )
                    val intent = Intent(this, ChildDashboard::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                else{
                    val thread = Thread {
                        //Get Date
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val c = Calendar.getInstance()
                        val date = sdf.format(c.time)

                        //Get Day
                        val time = c.getTime()
                        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(time.getTime())

                        try {
                            //Your code goes here
                            val calendar: Calendar = Calendar.getInstance()
                            val user = hashMapOf(
                                Common.USER_TYPE to Common.USER_TYPE_CHILD,
                                USER_EMAIL to "",
                                USER_NAME to _link_child_name.text.toString(),
                                USER_PHONE to number,
                                ACTIVE_STATUS to calendar.getTimeInMillis(),
                                Common.DATE to date,
                                Common.DAY to day
                            )
                            db.collection(LINKED_CHILDS).add(user).addOnSuccessListener { documentReference ->
                                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                //Moving to Next Screen
                                updateUser(
                                    number,
                                    true,
                                    USER_TYPE_CHILD,
                                    "",
                                    _link_child_name.text.toString(),
                                    this,
                                    documentReference.id
                                )
                                userType = USER_TYPE_CHILD
                                userName = _link_child_name.text.toString()
                                userEmail = ""
                                userPhone = number
                                status = true
                                FUID = documentReference.id

                                val intent = Intent(this, ChildDashboard::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }.addOnFailureListener { e ->
                                _progressBar_link_child.visibility = View.INVISIBLE
                                Log.w(ContentValues.TAG, "Error adding document", e)
                            }
                        } catch (e: Exception) {
                            _progressBar_link_child.visibility = View.INVISIBLE
                            e.printStackTrace()
                        }
                    }
                    thread.start()
                }
            }
    }
}