package com.example.virtuousvoice.Views

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    private val sharedPrefFile = Common.APP_NAME
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var usertype: String
    val db = Firebase.firestore
    lateinit var etEmail: String
    lateinit var etPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        getData()
        _sign_in_as.setText("signing in as " + usertype)


        //SignIn button
        _btn_sign_in.setOnClickListener{
            etEmail = _sign_in_email.text.toString()
            etPassword =_sign_in_password.text.toString()
            AuthenticateUser(etEmail,etPassword)
        }

        //Signup Link

        _sign_in_screen_sign_up_link.setOnClickListener{
            //Creating Intent
            val intent = Intent(this, ParentSignup::class.java)
            startActivity(intent)
        }
    }

    private fun AuthenticateUser(email: String, password: String){
        _progressBar.visibility= View.VISIBLE
        _progressBar.visibility= View.INVISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    db.collection(USER_COLLECTION)
                        .whereEqualTo(USER_EMAIL,email)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents){
                                val intent = Intent(this, TabbedActivity::class.java)
                                intent.putExtra(USER_TYPE, usertype)
                                intent.putExtra(USER_EMAIL, document.data[USER_EMAIL].toString())
                                intent.putExtra(USER_NAME, document.data[USER_NAME].toString())
                                intent.putExtra(USER_PHONE, document.data[USER_PHONE].toString())



                                //Saving UserType in Shared Preferences
                                val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                                val sharedPref: SharedPreferences.Editor = sharedPreferences.edit()
                                //Email
                                sharedPref.putString(USER_EMAIL, document.data[USER_EMAIL].toString())
                                sharedPref.apply()
                                //username
                                sharedPref.putString(USER_NAME, document.data[USER_NAME].toString())
                                sharedPref.apply()
                                //number
                                sharedPref.putString(USER_PHONE, document.data[USER_PHONE].toString())
                                sharedPref.apply()
                                //number
                                sharedPref.putString(Common.LOGIN_STATUS, Common.LOGGED_IN)
                                sharedPref.apply()

                                Common.userName = document.data[USER_NAME].toString()
                                Common.userEmail = document.data[USER_EMAIL].toString()
                                Common.userPhone = document.data[USER_PHONE].toString()

                                startActivity(intent)
                                finish()
                            }
                        }

                } else {
                    _progressBar.visibility= View.INVISIBLE
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed." + (task.getException()?.message
                        ?: ""),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun getData(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedPref: SharedPreferences =  sharedPreferences
        usertype = sharedPref.getString(USER_TYPE, "").toString()
    }
}



