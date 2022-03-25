package com.example.virtuousvoice.Views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.R
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_signup.*
import java.time.LocalDate

class LinkChild : AppCompatActivity() {


    private val sharedPrefFile = Common.APP_NAME
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val TAG = "testTag"
    private lateinit var code: String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_child)

        auth = Firebase.auth

        //Linking Parent Email
        _btn_link_child.setOnClickListener {
            verifyParentEmail()
        }

        //Verifying Email
        _btn_sign_up_verify.setOnClickListener{
            linkChild()
        }

        _child_sign_up_parent_link.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }


    }

    private fun saveUser(){
        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.saveUser(
            userTable(
                0,
                USER_TYPE_CHILD,
                _link_child_email.text.toString(),
                _link_child_name.text.toString(),
                "hello",
                true
            )
        )
        Common.userPhone = "sakjhdgghas"
        Common.status = true
        Common.userType = Common.USER_TYPE_CHILD
        Common.userEmail = _link_child_email.text.toString()
        Common.userName = _link_child_name.text.toString()
    }

    private fun updateUser(){
        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.updateUser(
            userTable(
                0,
                USER_TYPE_CHILD,
                _link_child_email.text.toString(),
                _link_child_name.text.toString(),
                "hello",
                true
            )
        )
        Common.userPhone = "sakjhdgghas"
        Common.status = true
        Common.userType = Common.USER_TYPE_CHILD
        Common.userEmail = _link_child_email.text.toString()
        Common.userName = _link_child_name.text.toString()
    }

    private fun saveInSharedPreferences() {
        //Saving UserType in Shared Preferences
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedPref: SharedPreferences.Editor = sharedPreferences.edit()
        //Email
        sharedPref.putString(Common.USER_EMAIL, _link_child_email.text.toString())
        sharedPref.apply()
        //username
        sharedPref.putString(Common.USER_NAME, _link_child_name.text.toString())
        sharedPref.apply()
        //number
        sharedPref.putString(Common.USER_PHONE, "sajgdasa")
        sharedPref.apply()
        //userType
        sharedPref.putString(Common.USER_TYPE, Common.USER_TYPE_CHILD)
        sharedPref.apply()
        //Logged in Status
        sharedPref.putString(Common.LOGIN_STATUS, Common.LOGGED_IN)
        sharedPref.apply()


        Common.userPhone = "sakjhdgghas"
        Common.userType = Common.USER_TYPE_CHILD
        Common.userEmail = _link_child_email.text.toString()
        Common.userName = _link_child_name.text.toString()

        Toast.makeText(this, sharedPreferences.getString(Common.USER_EMAIL, ""), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, sharedPreferences.getString(Common.USER_NAME, ""), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, sharedPreferences.getString(Common.USER_TYPE, ""), Toast.LENGTH_SHORT).show()
    }

    private fun verifyParentEmail() {
        var flag = false
        code =(100000..999999).random().toString()
        Log.d("TAG:", code)
        Toast.makeText(this, code, Toast.LENGTH_LONG).show()

        auth.fetchSignInMethodsForEmail(_link_child_email.text.toString()).addOnSuccessListener(this) { task ->
            if (!task.signInMethods?.isEmpty()!!){
                db.collection(Common.LINKED_CHILDS)
                    .whereEqualTo(Common.USER_EMAIL,_link_child_email.text.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents){
                            if (document[Common.USER_NAME] == _link_child_name.text.toString()){
                                flag = true
                            }
                        }

                        if (flag == true){
                            Toast.makeText(this, "This child is already linked with this parent!\nPlease verify code to continue", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Common.sendEmail(
                            _link_child_email.text.toString(),
                            _link_child_name.text.toString(),
                            code,
                            "Email Verification Code"
                        )
                        _child_signup_verify_layout.isVisible = true
                    }
            }
            else{
                Toast.makeText(this, "No Such parent found", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun linkChild() {
        if (_sign_up_six_digit_code.text.toString() == code){
            saveToCloud()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToCloud() {
        saveUser()
        updateUser()
//        saveInSharedPreferences()
            try {
                //Your code goes here
                val user = hashMapOf(
                    Common.USER_TYPE to Common.USER_TYPE_CHILD,
                    Common.USER_EMAIL to _link_child_email.text.toString(),
                    Common.USER_NAME to _link_child_name.text.toString(),
                    Common.TOKEN to "" ,
                    Common.DATE to LocalDate.now().toString(),
                    Common.DAY to LocalDate.now().dayOfWeek.toString()
                )

                db.collection(Common.LINKED_CHILDS).add(user).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    //Moving to Next Screen
                    val intent = Intent(this, TabbedActivity::class.java)
//                    intent.putExtra(USER_TYPE, USER_TYPE_CHILD)
                    startActivity(intent)
//                    finishAffinity()
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        Log.d("Thread status: ", "Started")
    }




}