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
import com.example.virtuousvoice.utilties.Common.CHILD_NAME
import com.example.virtuousvoice.utilties.Common.PARENT_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SettingFragment(userType: String, userName: String, userEmail: String, userPhone: String) :
    Fragment() {
    private var userType: String = userType
    private var userName: String = userName
    private var userEmail: String = userEmail
    private var userPhone: String = userPhone
    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        _test_email.setOnClickListener {
            db.collection(USER_COLLECTION)
                .whereEqualTo(USER_PHONE, userPhone)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.data[USER_TYPE].toString() == USER_TYPE_PARENT) {
                            val intent = Intent(activity, RecordAudio::class.java)
                            intent.putExtra(PARENT_EMAIL, document.data[USER_EMAIL].toString())
                            intent.putExtra(CHILD_NAME, userName)
                            startActivity(intent)
                        }
                    }
                }
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
