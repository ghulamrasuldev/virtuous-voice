package com.example.virtuousvoice.Fragments

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
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.Common.USER_TYPE_PARENT
import com.example.virtuousvoice.utilties.Common.sample_audio
import com.example.virtuousvoice.utilties.ObjectListiner
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() , ObjectListiner {
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
        getData()


        Log.d("TAG: ", "$userName")


            if (userName != "") {
                _update_info_notification.isVisible = false
            }

        //Listeners
        _update_personal_info.setOnClickListener{
            updateInfoFragment.show(this.childFragmentManager, "Bottom Sheet Update Information")
        }



        Log.d("TAG: ", userName)
        _user_name.text = "Hi $userName!"
        val AudioList = dummyData()
        val CapturedAudioView = _captured_voice_notes
        CapturedAudioView.adapter = CapturedAudiosAdaptor(AudioList)
        CapturedAudioView.layoutManager =  LinearLayoutManager(activity)
    }
    private fun dummyData (): List<CapturedVoiceData>{
        val list = ArrayList<CapturedVoiceData>()
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("samiya ijaz","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("noman khalid","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("samiya ijaz","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("noman khalid","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("samiya ijaz","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("noman khalid","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("samiya ijaz","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("noman khalid","03/02/21", "Tuesday"))
        return list
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