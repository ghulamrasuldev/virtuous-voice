package com.example.virtuousvoice.Views

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.example.virtuousvoice.utilties.Common.sleep
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.Common.userType
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_parent_signup.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.OTP_SECTION
import kotlinx.android.synthetic.main.activity_sign_in._progressBar
import java.time.LocalDate
import java.util.concurrent.TimeUnit


class SignIn : AppCompatActivity() {
    private val sharedPrefFile = Common.APP_NAME
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var usertype: String
    val db = Firebase.firestore
    lateinit var etEmail: String
    lateinit var etPassword: String
    var number : String =""
    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var sentToken : String = ""
    lateinit var credential: PhoneAuthCredential

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        _sign_in_as.setText("signing in as Parent")
        auth.setLanguageCode("ur")


        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d("GFG" , "onVerificationCompleted Success")
                _progressBar.isVisible = false
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
                _progressBar.isVisible = false
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
                _progressBar.isVisible = false
                SIGN_IN_SECTION.isVisible= false
                OTP_SECTION.isVisible = true
            }
        }




        //SignIn button
        _btn_sign_in.setOnClickListener{
            _progressBar.isVisible = true
            AuthenticateWithPhone()
        }

        //Signup Link
        _sign_in_go_as_child.setOnClickListener{

            userType = USER_TYPE_CHILD
            //Creating Intent
            val intent = Intent(this, LinkChild::class.java)
            startActivity(intent)
            finishAffinity()
        }

        //Resend OTP
        _sign_in_back.setOnClickListener{
            SIGN_IN_SECTION.isVisible= true
            OTP_SECTION.isVisible = false
        }


        //Verify OTP

        _btn_sign_in_confirm_otp.setOnClickListener {
            Toast.makeText(this, sentToken, Toast.LENGTH_SHORT).show()
            val credential = PhoneAuthProvider.getCredential(sentToken, _sign_in_entered_otp.text.toString())
            signInWithPhoneAuthCredential(credential)
        }

    }

    private fun AuthenticateWithPhone() {
        number = _sign_in_phone.text.toString()
        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty()){
            number = "+92$number"
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


    private fun saveUser(){
        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.saveUser(
            userTable(
                0,
                USER_TYPE_PARENT,
                "",
                "",
                number,
                true
            )
        )
        Common.userPhone = "sakjhdgghas"
        Common.status = true
        Common.userType = Common.USER_TYPE_PARENT
        Common.userEmail = Common.userEmail
        Common.userName = Common.userName
    }


    private fun updateUser(){
        db.collection(USER_COLLECTION)
        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.updateUser(
            userTable(
                0,
                USER_TYPE_PARENT,
                userEmail,
                userName,
                userPhone,
                true
            )
        )
        Common.userPhone = number
        Common.status = true
        Common.userType = Common.USER_TYPE_PARENT
        Common.userEmail = Common.userEmail
        Common.userName = Common.userName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user

                    saveUserInCLoud()
                    Log.d("TAG: ", auth.currentUser.toString())
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserInCLoud(){
        _progressBar.isVisible = true
        var userExist = false
        db.collection(USER_COLLECTION)
            .whereEqualTo("phoneNumber",number)
            .get().addOnSuccessListener { documents->
                Log.d("TAG", "User Found")
                for (documment in documents){
                    if(documment.data["phoneNumber"].toString()==number){
                        userExist = true
                        userType = documment.data[USER_TYPE].toString()
                        userEmail = documment.data[USER_EMAIL].toString()
                        userPhone = documment.data[USER_PHONE].toString()
                        userName = documment.data[USER_NAME].toString()
                    }
                }
                if (userExist){
                    updateUser()
                    Log.d("TAG: ", "User Already Exist")
                    val intent = Intent(this, TabbedActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }else{
                    val thread = Thread {
                        try {
                            //Your code goes here
                            val user = hashMapOf(
                                Common.USER_TYPE to USER_TYPE_PARENT,
                                USER_EMAIL to "",
                                USER_NAME to "",
                                USER_PHONE to number,
                                Common.DATE to LocalDate.now().toString(),
                                Common.DAY to LocalDate.now().dayOfWeek.toString()
                            )
                            db.collection(USER_COLLECTION).add(user).addOnSuccessListener { documentReference ->
                                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                //Moving to Next Screen
                                saveUser()

                                val intent = Intent(this, TabbedActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }.addOnFailureListener { e ->
                                _progressBar.visibility = View.INVISIBLE
                                Log.w(TAG, "Error adding document", e)
                            }

                        } catch (e: Exception) {
                            _progressBar.visibility = View.INVISIBLE
                            e.printStackTrace()
                        }
                    }
                    thread.start()
                }
        }
    }

}



