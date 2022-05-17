package com.example.virtuousvoice.Fragments

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtuousvoice.Adaptors.CapturedAudiosAdaptor
import com.example.virtuousvoice.DataClasses.CapturedVoiceData
import com.example.virtuousvoice.R
import com.example.virtuousvoice.Services.ParentService
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.AUDIO_LINK
import com.example.virtuousvoice.utilties.Common.DATE
import com.example.virtuousvoice.utilties.Common.DAY
import com.example.virtuousvoice.utilties.Common.NEW_TO_DASHBOARD
import com.example.virtuousvoice.utilties.Common.TOXIC_AUDIO_COLLECTION
import com.example.virtuousvoice.utilties.Common.TOXIC_STATUS
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.example.virtuousvoice.utilties.Common.sample_audio
import com.example.virtuousvoice.utilties.ObjectListiner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() , ObjectListiner {

    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    private lateinit var userType: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String

    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val updateInfoFragment = UpdateDataFragment(this)

        var capturedAudios = ArrayList<CapturedVoiceData>()
        getData()


        Log.d("TAG: ", "$userName")

            if (userName != "") {
                _update_info_notification.isVisible = false
            }

        db.collection(TOXIC_AUDIO_COLLECTION)
            .whereEqualTo(USER_PHONE, userPhone)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    if (document.data[TOXIC_STATUS] == true){
                        capturedAudios.add(
                            CapturedVoiceData(
                                document.data[USER_NAME].toString(),
                                document.data[DATE].toString(),
                                document.data[DAY].toString(),
                                document.data[AUDIO_LINK].toString(),
                                document.data[NEW_TO_DASHBOARD] as Boolean
                            )
                        )
                    }
                }

                val CapturedAudioView = _captured_voice_notes
                CapturedAudioView.adapter = CapturedAudiosAdaptor(capturedAudios)
                CapturedAudioView.layoutManager =  LinearLayoutManager(activity)
            }


        //Listeners
        _update_personal_info.setOnClickListener{
            updateInfoFragment.show(this.childFragmentManager, "Bottom Sheet Update Information")
        }

        Log.d("TAG: ", userName)
        _user_name.text = "Hi $userName!"
    }

    private fun getData() {
        userType = Common.userType
        userEmail = Common.userEmail
        userName = Common.userName
        userPhone = Common.userPhone
    }


     override fun onItemClicked(name: String) {
         _update_info_notification.isVisible = false
         _user_name.text = "Hi $name!"
    }

}