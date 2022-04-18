package com.example.virtuousvoice.Views


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.virtuousvoice.Adaptors.ViewPagerAdaptor
import com.example.virtuousvoice.Fragments.ChildernFragment
import com.example.virtuousvoice.Fragments.HomeFragment
import com.example.virtuousvoice.Fragments.SettingFragment
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Services.ParentService
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import kotlinx.android.synthetic.main.activity_tabbed.*





class TabbedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        Intent(this, ParentService::class.java).also {
            Log.d("Starting: ", "Parent Service")
            startService(it)
        }

        createTabs()
    }

    private fun createTabs(){
        if (Common.userType == USER_TYPE_PARENT){
            val adaptor = ViewPagerAdaptor(supportFragmentManager)
            adaptor.addFragment(HomeFragment(), "")
            adaptor.addFragment(ChildernFragment(), "")
            adaptor.addFragment(SettingFragment(),"")
            _view_pager.adapter = adaptor
            _tabs.setupWithViewPager(_view_pager)
            _tabs.getTabAt(0)!!.setIcon(R.drawable.icon_home)
            _tabs.getTabAt(1)!!.setIcon(R.drawable.icon_child)
            _tabs.getTabAt(2)!!.setIcon(R.drawable.icon_setting)

        }
        else if (Common.userType == USER_TYPE_CHILD){
            val adaptor = ViewPagerAdaptor(supportFragmentManager)
            adaptor.addFragment(HomeFragment(), "")
            adaptor.addFragment(SettingFragment(),"")
            _view_pager.adapter = adaptor
            _tabs.setupWithViewPager(_view_pager)
            _tabs.getTabAt(0)!!.setIcon(R.drawable.icon_home)
            _tabs.getTabAt(1)!!.setIcon(R.drawable.icon_setting)
        }
        else{

        }
    }
}
