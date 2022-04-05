package com.example.virtuousvoice.Views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.APP_NAME
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.example.virtuousvoice.utilties.Common.userType
import kotlinx.android.synthetic.main.activity_welcome_screen.*

class WelcomeScreen : AppCompatActivity() {

    private val sharedPrefFile = APP_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)



        if (!Common.status || Common.userType == "" || Common.userPhone == ""){
            Common.userType = ""
            Common.userEmail = ""
            Common.userName = ""

        }
        else{
            val intent = Intent(this, TabbedActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        //Continue as Parent
        _sign_in_as_parent.setOnClickListener{


            userType = USER_TYPE_PARENT
            //Creating Intent
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        //Continue as Child
        _welcome_as_child.setOnClickListener{


            userType = USER_TYPE_CHILD
            //Creating Intent
            val intent = Intent(this, LinkChild::class.java)
            startActivity(intent)
        }
    }
}