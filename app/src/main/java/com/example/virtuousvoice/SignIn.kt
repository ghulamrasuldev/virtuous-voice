package com.example.virtuousvoice

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val usertype = intent.getStringExtra("usertype")
        auth = Firebase.auth
        var email: String
        var password: String
        _sign_in_as.setText("signing in as " + usertype)


        //Signin button
        _btn_sign_in.setOnClickListener{
            email = _sign_in_email.text.toString()
            password =_sign_in_password.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
            val intent = Intent(this, TabbedActivity::class.java)
            intent.putExtra("usertype","parent")
            startActivity(intent)

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

        //Taking use to sign up as individual screen
        _sign_up_as_individual.setOnClickListener{
            val intent = Intent(this, IndividualSignup::class.java)
            startActivity(intent)
            finish()
        }
    }
}