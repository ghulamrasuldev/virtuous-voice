package com.example.virtuousvoice

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var usertype: String
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        usertype = intent.getStringExtra(USER_TYPE).toString()
        var etEmail: String
        var etPassword: String
        _sign_in_as.setText("signing in as " + usertype)


        //Signin button
        _btn_sign_in.setOnClickListener{
            etEmail = _sign_in_email.text.toString()
            etPassword =_sign_in_password.text.toString()
            AuthenticateUser(etEmail,etPassword)
        }

        //Taking use to sign up as parent screen
        _sign_up_as_parent.setOnClickListener{
            val intent = Intent(this, ParentSignup::class.java)
            startActivity(intent)
            finish()
        }

        //Taking use to sign up as child screen
        _sign_up_as_child.setOnClickListener{
            val intent = Intent(this, ChildSignup::class.java)
            startActivity(intent)
            finish()
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
}



