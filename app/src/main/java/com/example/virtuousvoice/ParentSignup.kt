package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_parent_signup.*

class ParentSignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_signup)

        //Taking user to sign in screen
        _sign_in_as_parent.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("user","parent")
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_child.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("user","child")
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_individual.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("user","individual")
            startActivity(intent)
            finish()
        }
    }
}