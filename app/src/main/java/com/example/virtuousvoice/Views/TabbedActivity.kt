package com.example.virtuousvoice.Views


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.virtuousvoice.Adaptors.ViewPagerAdaptor
import com.example.virtuousvoice.Fragments.ChildernFragment
import com.example.virtuousvoice.Fragments.HomeFragment
import com.example.virtuousvoice.Fragments.SettingFragment
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Services.FloatingService
import com.example.virtuousvoice.Services.ParentService
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import kotlinx.android.synthetic.main.activity_tabbed.*
import java.io.File

class TabbedActivity : AppCompatActivity() {

    companion object {
        private const val DRAW_OVER_OTHER_APP_PERMISSION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        Intent(this, ParentService::class.java).also {
            Log.d("Starting: ", "Parent Service")
            startService(it)
        }

        askForPermissions()
        checkAndCreateDirectory()

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

    private fun checkAndCreateDirectory() {
        val file = File(FloatingService.FOLDER_PATH)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    private fun askForPermissions() {
        // system overlay permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION)
        }

        val permissionNeeded = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsNotGranted = ArrayList<String>()

        for (permission in permissionNeeded) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNotGranted.add(permission)
            }
        }

        val array = arrayOfNulls<String>(permissionsNotGranted.size)

        if (array.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNotGranted.toArray(array), 0
            )
        }
    }
}
