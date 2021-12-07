package com.example.virtuousvoice

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.virtuousvoice.Adaptors.ViewPagerAdaptor
import com.example.virtuousvoice.Fragments.ChildernFragment
import com.example.virtuousvoice.Fragments.HomeFragment
import com.example.virtuousvoice.Fragments.SettingFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_tabbed.*

class TabbedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)
        var user = intent.getStringExtra("user")
        createTabs(user)
    }

    fun createTabs(user: String?){
        if (user == "parent"){
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
        else if (user == "child"){
            val adaptor = ViewPagerAdaptor(supportFragmentManager)
            adaptor.addFragment(HomeFragment(), "")
            adaptor.addFragment(SettingFragment(),"")
            _view_pager.adapter = adaptor
            _tabs.setupWithViewPager(_view_pager)
            _tabs.getTabAt(0)!!.setIcon(R.drawable.icon_home)
            _tabs.getTabAt(1)!!.setIcon(R.drawable.icon_setting)
        }
        else if (user == "individual"){
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
