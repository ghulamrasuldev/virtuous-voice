package com.example.virtuousvoice.Views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.AUTHENTICATION_FAILED_ERROR
import com.example.virtuousvoice.utilties.Common.DATE
import com.example.virtuousvoice.utilties.Common.DAY
import com.example.virtuousvoice.utilties.Common.INVALID_EMAIL
import com.example.virtuousvoice.utilties.Common.NO_PARENT_FOUND_ERROR
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.example.virtuousvoice.utilties.Common.VERIFY_PASSWORD_ERROR
import com.example.virtuousvoice.utilties.Common.isEmailValid
import com.example.virtuousvoice.utilties.Common.isPasswordValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_signup.*
import java.time.LocalDate

class ChildSignup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val TAG = "testTag"
    private lateinit var etUserName: String
    private lateinit var etEmail: String
    private lateinit var etNumber: String
    private lateinit var etPassword: String
    private lateinit var etVerifyPassword: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_signup)
        auth = Firebase.auth

        //Linking Parent Email
        _btn_sign_up.setOnClickListener {
            _child_signup_verify_layout.isVisible = true
            verifyParentEmail()
        }

        //Verifying Email
        _btn_sign_up_verify.setOnClickListener{
            linkChild()
        }
    }

    private fun verifyParentEmail() {

    }

    private fun linkChild() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createAccount(){
        etEmail = _sign_up_email.text.toString()
        etUserName = "_sign_up_username.text.toString()"
        etPassword = "_sign_up_password.text.toString()"
        etNumber = "_sign_up_phone.text.toString()"
        etVerifyPassword = "_sign_up_verify_password.text.toString()"

        _progressBar.visibility = View.VISIBLE
        if (etEmail.isNotEmpty() && etUserName.isNotEmpty() && etUserName.isNotEmpty()
            && etPassword.isNotEmpty() && etVerifyPassword.isNotEmpty()) {
            //Validation Rules
            // Checking Email
            if (isEmailValid(etEmail)) {
                //Checking Password Strength
                if (etPassword.isPasswordValid(this)) {
                    //Checking if parent exist
                    db.collection(USER_COLLECTION)
                        .whereEqualTo(USER_PHONE, etNumber)
                        .get()
                        .addOnSuccessListener { documents ->
                            var parentFoundFlag = false
                            for (document in documents) {
                                if(document.data[USER_TYPE].toString() == USER_TYPE_PARENT){
                                    parentFoundFlag=true
                                    Log.d("Test", "${document.id} => ${document.data[USER_TYPE]}")
                                    Log.d("Test", "${document.id} => ${document.data[USER_NAME]}")
                                    Log.d("Test", "${document.id} => ${document.data[USER_EMAIL]}")
                                    Log.d("Test", "${document.id} => ${document.data[USER_PHONE]}")
                                    //Verifying Password
                                    if (etPassword.isNotEmpty() && etPassword == etVerifyPassword) {
                                        auth.createUserWithEmailAndPassword(etEmail, etPassword)
                                            .addOnCompleteListener(this) { task ->
                                                if (task.isSuccessful) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d(TAG, "createUserWithEmail:success")
                                                    Toast.makeText(
                                                        baseContext, "Account Created Successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    saveToFirestore(
                                                        etEmail.trim(),
                                                        etUserName.trim(),
                                                        etNumber.trim()
                                                    )
                                                } else {
                                                    _progressBar.visibility = View.INVISIBLE
                                                    // If sign in fails, display a message to the user.
                                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                                    Toast.makeText(
                                                        baseContext,
                                                        AUTHENTICATION_FAILED_ERROR + (task.getException()?.message
                                                            ?: ""),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        _progressBar.visibility = View.INVISIBLE
                                        Toast.makeText(this, VERIFY_PASSWORD_ERROR, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                            if (!parentFoundFlag){
                                _progressBar.visibility = View.INVISIBLE
                                Toast.makeText(this, NO_PARENT_FOUND_ERROR, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            _progressBar.visibility = View.INVISIBLE
                            Log.w("Test", "Error getting documents: ", exception)
                        }
                }
            }
            else{
                Toast.makeText(this, INVALID_EMAIL, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, Common.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToFirestore(userEmail: String, userName: String, userPhone: String) {
        val currentDate = LocalDate.now()
        val thread = Thread {
            try {
                //Your code goes here
                val user = hashMapOf(
                    USER_TYPE to USER_TYPE_CHILD,
                    USER_EMAIL to userEmail,
                    USER_NAME to userName,
                    USER_PHONE to userPhone,
                    DATE to LocalDate.now().toString(),
                    DAY to LocalDate.now().dayOfWeek.toString()
                )
                db.collection(Common.USER_COLLECTION).add(user).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    //Moving to Next Screen
                    val intent = Intent(this, TabbedActivity::class.java)
                    intent.putExtra(USER_TYPE, USER_TYPE_CHILD)
                    intent.putExtra(USER_NAME, userName)
                    intent.putExtra(USER_EMAIL,userEmail)
                    intent.putExtra(USER_PHONE,userPhone)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        Log.d("Thread status: ", "Started")
    }
}