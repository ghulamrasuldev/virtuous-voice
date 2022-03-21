package com.example.virtuousvoice.Views


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.virtuousvoice.Adaptors.ViewPagerAdaptor
import com.example.virtuousvoice.Fragments.ChildernFragment
import com.example.virtuousvoice.Fragments.HomeFragment
import com.example.virtuousvoice.Fragments.SettingFragment
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import kotlinx.android.synthetic.main.activity_tabbed.*

class TabbedActivity : AppCompatActivity() {
    private lateinit var userType: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        userType = intent.getStringExtra(USER_TYPE).toString()
        userName = intent.getStringExtra(USER_NAME).toString()
        userEmail = intent.getStringExtra(USER_EMAIL).toString()
        userPhone = intent.getStringExtra(USER_PHONE).toString()
        Log.d(USER_TYPE, userType)
        Log.d(USER_NAME, userName)
        Log.d(USER_EMAIL, userEmail)
        Log.d(USER_PHONE, userPhone)
        var data: Bundle = Bundle()
        data.putString(USER_NAME,userName)
        data.putString(USER_EMAIL,userEmail)
        data.putString(USER_PHONE,userPhone)
        createTabs(userType)
    }

    private fun createTabs(usertype: String?){
        if (Common.userType == USER_TYPE_PARENT){
            val adaptor = ViewPagerAdaptor(supportFragmentManager)
            adaptor.addFragment(HomeFragment(), "")
            adaptor.addFragment(ChildernFragment(userType, userName, userEmail, userPhone), "")
            adaptor.addFragment(SettingFragment(userType, userName, userEmail, userPhone),"")
            _view_pager.adapter = adaptor
            _tabs.setupWithViewPager(_view_pager)
            _tabs.getTabAt(0)!!.setIcon(R.drawable.icon_home)
            _tabs.getTabAt(1)!!.setIcon(R.drawable.icon_child)
            _tabs.getTabAt(2)!!.setIcon(R.drawable.icon_setting)

            if (_tabs.getTabAt(0)!!.isSelected){

            }
        }
        else if (Common.userType == USER_TYPE_CHILD){
            val adaptor = ViewPagerAdaptor(supportFragmentManager)
            adaptor.addFragment(HomeFragment(), "")
            adaptor.addFragment(SettingFragment(userType, userName, userEmail, userPhone),"")
            _view_pager.adapter = adaptor
            _tabs.setupWithViewPager(_view_pager)
            _tabs.getTabAt(0)!!.setIcon(R.drawable.icon_home)
            _tabs.getTabAt(1)!!.setIcon(R.drawable.icon_setting)
        }
        else{

        }
    }
}
