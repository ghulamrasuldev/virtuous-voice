package com.example.virtuousvoice.Fragments

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.virtuousvoice.Adaptors.CapturedAudiosAdaptor
import com.example.virtuousvoice.DataClasses.CapturedVoiceData
import com.example.virtuousvoice.R
import com.example.virtuousvoice.TabbedActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
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
        val AudioList = dummyData()
        val CapturedAudioView = _captured_voice_notes
        CapturedAudioView.adapter = CapturedAudiosAdaptor(AudioList)
        CapturedAudioView.layoutManager =  LinearLayoutManager(activity)
    }
    private fun dummyData (): List<CapturedVoiceData>{
        val list = ArrayList<CapturedVoiceData>()
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(CapturedVoiceData("ghulamrasool","03/02/21", "Tuesday"))
        return list
    }
}