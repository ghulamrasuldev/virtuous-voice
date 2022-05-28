package com.example.virtuousvoice.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common.CONTINUE_WITH_EMAIL
import com.example.virtuousvoice.utilties.Common.CONTINUE_WITH_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.example.virtuousvoice.utilties.Common.continueWith
import com.example.virtuousvoice.utilties.Common.userType
import kotlinx.android.synthetic.main.activity_phone_email_selection.*

class PhoneEmailSelection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_email_selection)


        _continue_with_email.setOnClickListener{
            continueWith = CONTINUE_WITH_EMAIL
            if (userType == USER_TYPE_PARENT){
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            }
            else if (userType == USER_TYPE_CHILD){
                val intent = Intent(this, LinkChild::class.java)
                startActivity(intent)
            }
        }

        _continue_with_phone.setOnClickListener{
            continueWith = CONTINUE_WITH_PHONE
            if (userType == USER_TYPE_PARENT){
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            }
            else if (userType == USER_TYPE_CHILD){
                val intent = Intent(this, LinkChild::class.java)
                startActivity(intent)
            }
        }
    }
}