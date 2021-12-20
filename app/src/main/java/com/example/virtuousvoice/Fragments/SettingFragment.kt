package com.example.virtuousvoice.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.fragment_setting.*
import com.example.virtuousvoice.RecordAudio


class SettingFragment (userType: String, userName: String, userEmail: String, userPhone: String): Fragment() {
    private var userType: String = userType
    private var userName: String = userName
    private var userEmail: String = userEmail
    private var userPhone: String = userPhone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _test_email.setOnClickListener {
            val intent = Intent(activity, RecordAudio::class.java)
            startActivity(intent)
        }

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

}
