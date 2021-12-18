package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_parent_signup.*
import android.util.Log
import android.widget.Toast
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.random.Random


class ParentSignup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private val TAG = "testTag"
    private lateinit var etUserName: String
    private lateinit var etEmail: String
    private lateinit var etNumber: String
    private lateinit var etPassword: String
    private var flag: Boolean = false
    val db = Firebase.firestore

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
        }

        //Taking user to sign in screen
        _sign_in_as_child.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra(USER_TYPE, USER_TYPE_CHILD)
            startActivity(intent)
        }
    }

    private fun createAccount(): Boolean {
        var flag: Boolean = false
        etEmail = _sign_up_email.text.toString()
        etUserName = _sign_up_username.text.toString()
        etNumber = _sign_up_number.text.toString()
        etPassword = _sign_up_password.text.toString()

        if (etEmail.isNotEmpty() && etUserName.isNotEmpty()
            && etNumber.isNotEmpty() && etPassword.isNotEmpty()
        ) {

            flag = true
            auth.createUserWithEmailAndPassword(etEmail, etPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Log.d(TAG, "Saving data to FireStore")
                        saveToFirestore(etEmail.trim(), etUserName.trim(), etNumber.trim())
                    } else {
                        flag = false
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        } else {
            Toast.makeText(this, "Some Fields are empty", Toast.LENGTH_SHORT).show()
        }
        return flag
    }

    private fun saveToFirestore(email: String, userName: String, number: String) {

        val thread = Thread {
            try {
                //Your code goes here
                val user = hashMapOf(
                    "userType" to "parent",
                    "eMail" to email,
                    "userName" to userName,
                    "phoneNumber" to number
                )

                db.collection(USER_COLLECTION).add(user).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                    //Moving to Next Screen
                    val intent = Intent(this, SignIn::class.java)
                    intent.putExtra(USER_TYPE, USER_TYPE_PARENT)
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
        thread.interrupt()
        Log.d("Thread status: ", "Stopped")



    }
}