package com.example.virtuousvoice.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.virtuousvoice.Fragments.UpdateEmailFragment
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common.userEmail
import kotlinx.android.synthetic.main.activity_child_dashboard.*

class ChildDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_dashboard)
        val updateEmailFragment = UpdateEmailFragment()

        if (userEmail != ""){
            _update_email_info.isVisible = false
        }
        //Listeners
        _update_email_info.setOnClickListener {
            _update_child_info_notification.isVisible = false
            updateEmailFragment.show(this.supportFragmentManager, "Bottom Sheet Update Email")
        }





    }
}