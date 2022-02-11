package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import kotlinx.android.synthetic.main.activity_welcome_screen.*

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        //Signing in as Parent
        _sign_in_as_parent.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra(USER_TYPE, USER_TYPE_PARENT)
            startActivity(intent)
        }

        //Signing in as Child
        _sign_in_as_child.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra(USER_TYPE, USER_TYPE_CHILD)
            startActivity(intent)
        }

        //Taking use to sign up as parent screen
        _sign_up_as_parent.setOnClickListener{
            val intent = Intent(this, ParentSignup::class.java)
            startActivity(intent)
        }

        //Taking use to sign up as Child screen
        _sign_up_as_child.setOnClickListener{
            val intent = Intent(this, ChildSignup::class.java)
            startActivity(intent)
        }

    }
}