package com.example.virtuousvoice.Fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.fragment_setting.*
import com.example.virtuousvoice.Views.RecordAudio
import com.example.virtuousvoice.Views.WelcomeScreen
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.CHILD_NAME
import com.example.virtuousvoice.utilties.Common.PARENT_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_signup.*


class SettingFragment() :
    Fragment() {
    private lateinit var userType: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        //Listeners
//        _test_email.setOnClickListener {
//            db.collection(USER_COLLECTION)
//                .whereEqualTo(USER_PHONE, userPhone)
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        if (document.data[USER_TYPE].toString() == USER_TYPE_PARENT) {
//                            val intent = Intent(activity, RecordAudio::class.java)
//                            intent.putExtra(PARENT_EMAIL, document.data[USER_EMAIL].toString())
//                            intent.putExtra(CHILD_NAME, userName)
//                            startActivity(intent)
//                        }
//                    }
//                }
//        }

        _logout.setOnClickListener{
            image.isVisible = true
            removeData()
            val intent = Intent(activity, WelcomeScreen::class.java)
            startActivity(intent)
            finishAffinity(this.activity as Activity)
        }
    }

    private fun getData() {
        val sharedPrefFile = Common.APP_NAME
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedPref: SharedPreferences.Editor = sharedPreferences.edit()

        sharedPref.remove(USER_EMAIL)
        sharedPref.remove(USER_TYPE)
        sharedPref.remove(USER_NAME)
        sharedPref.apply()
        this.userType = Common.userType
        this.userEmail = Common.userEmail
        this.userName = Common.userName
        this.userPhone = Common.userPhone
    }


    private fun removeData() {
        Common.userType = ""
        Common.userEmail = ""
        Common.userName = ""
        Common.userPhone = ""
        updateUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


    private fun updateUser(){
        var mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.updateUser(
            userTable(
                0,
                "",
                "",
                "",
                "",
                false,
                ""
            )
        )
        Common.userPhone = ""
        Common.status = false
        Common.userType = ""
        Common.userEmail = ""
        Common.userName = ""
    }


    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.BLACK)
        }
        view.draw(canvas)
        return bitmap
    }

}
