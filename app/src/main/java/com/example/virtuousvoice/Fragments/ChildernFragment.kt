package com.example.virtuousvoice.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtuousvoice.Adaptors.CapturedAudiosAdaptor
import com.example.virtuousvoice.Adaptors.LinkedChildAdaptor
import com.example.virtuousvoice.DataClasses.CapturedVoiceData
import com.example.virtuousvoice.DataClasses.LinkedChildData
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.fragment_childern.*


class ChildernFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ChildList = dummyData()
        val LinkedChildView= _linked_childern
        LinkedChildView.adapter = LinkedChildAdaptor(ChildList)
        LinkedChildView.layoutManager =  LinearLayoutManager(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_childern, container, false)
    }

    private fun dummyData (): List<LinkedChildData>{
        val list = ArrayList<LinkedChildData>()
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))
        list.add(LinkedChildData("ghulamrasool","03/02/21", "Tuesday"))

        return list
    }

}