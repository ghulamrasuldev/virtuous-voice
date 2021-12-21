package com.example.virtuousvoice

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_parent_signup.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.AUTHENTICATION_FAILED_ERROR
import com.example.virtuousvoice.utilties.Common.DATE
import com.example.virtuousvoice.utilties.Common.DAY
import com.example.virtuousvoice.utilties.Common.EMPTY_FIELDS_ERROR
import com.example.virtuousvoice.utilties.Common.USER_ALREADY_REGISTERED
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
import java.time.LocalDate
import java.util.*

class ParentSignup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val TAG = "testTag"
    private lateinit var etUserName: String
    private lateinit var etEmail: String
    private lateinit var etNumber: String
    private lateinit var etPassword: String
    private lateinit var etVerifyPassword: String
    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_signup)
        auth = Firebase.auth
        //Signing up user
        _btn_sign_up.setOnClickListener {
            createAccount()
        }

        //Taking user to sign in screen
        _sign_in_as_parent.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra(USER_TYPE, USER_TYPE_PARENT)
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_child.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra(USER_TYPE, USER_TYPE_CHILD)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAccount(){
        etEmail = _sign_up_email.text.toString()
        etUserName = _sign_up_username.text.toString()
        etNumber = _sign_up_number.text.toString()
        etPassword = _sign_up_password.text.toString()
        etVerifyPassword = _sign_up_verify_password.text.toString()
        _progressBar.visibility = View.VISIBLE
        if (etEmail.isNotEmpty() && etUserName.isNotEmpty()
            && etNumber.isNotEmpty() && etPassword.isNotEmpty()) {
            //Validation Rules
            //Checking Email
            if (etEmail.isEmailValid(this)) {
                //Checking Password Strength
                if (etPassword.isPasswordValid(this)) {
                    //Verifying Password
                    db.collection(USER_COLLECTION)
                        .whereEqualTo(USER_PHONE, etNumber)
                        .get()
                        .addOnSuccessListener { documents ->
                            var parenAlreadyRegisetered = false
                            for (document in documents){
                                if (document.data[USER_PHONE].toString() == etNumber){
                                    parenAlreadyRegisetered = true
                                }
                            }
                            if(!parenAlreadyRegisetered){
                                if (etPassword == etVerifyPassword) {
                                    auth.createUserWithEmailAndPassword(etEmail, etPassword)
                                        .addOnCompleteListener(this) { task ->
                                            if (task.isSuccessful) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "createUserWithEmail:success")
                                                Log.d(TAG, "Saving data to FireStore")
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
                                    Toast.makeText(this, VERIFY_PASSWORD_ERROR, Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                _progressBar.visibility = View.INVISIBLE
                                Toast.makeText(this, USER_ALREADY_REGISTERED, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

        } else {
            _progressBar.visibility = View.INVISIBLE
            Toast.makeText(this, EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show()
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToFirestore(userEmail: String, userName: String, userPhone: String) {

        val thread = Thread {
            try {
                //Your code goes here
                val user = hashMapOf(
                    USER_TYPE to USER_TYPE_PARENT,
                    USER_EMAIL to userEmail,
                    USER_NAME to userName,
                    USER_PHONE to userPhone,
                    DATE to LocalDate.now().toString(),
                    DAY to LocalDate.now().dayOfWeek.toString()
                )
                db.collection(USER_COLLECTION).add(user).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    //Moving to Next Screen
                    val intent = Intent(this, TabbedActivity::class.java)
                    intent.putExtra(USER_TYPE, USER_TYPE_PARENT)
                    intent.putExtra(USER_NAME, userName)
                    intent.putExtra(USER_EMAIL,userEmail)
                    intent.putExtra(USER_PHONE,userPhone)
                    startActivity(intent)
                    finish()

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
        Log.d("Thread status: ", "Started")
    }

}




