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
import com.example.virtuousvoice.utilties.Common.DATE
import com.example.virtuousvoice.utilties.Common.DAY
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_childern.*
import kotlinx.android.synthetic.main.fragment_childern._user_name
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.linked_child_card.*


class ChildernFragment (userType: String, userName: String, userEmail: String, userPhone: String): Fragment() {
    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    private var userType: String = userType
    private var userName: String = userName
    private var userEmail: String = userEmail
    private var userPhone: String = userPhone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _user_name.text = "Hi $userName!"

        db.collection(USER_COLLECTION)
            .whereEqualTo(USER_PHONE, userPhone)
            .get()
            .addOnSuccessListener { documents ->
                var dateArray = ArrayList<String>()
                var dayArray = ArrayList<String>()
                var nameArray = ArrayList<String>()
                for(document in documents){
                    if(document.data[USER_TYPE].toString() == USER_TYPE_CHILD){
                        dateArray.add(document.data[DATE].toString())
                        dayArray.add(document.data[DAY].toString())
                        nameArray.add(document.data[USER_NAME].toString())
                    }
                }
                val ChildList = dummyData(nameArray, dateArray, dayArray)
                if(ChildList.isEmpty()){
                    _notice.visibility = View.VISIBLE
                }
                val LinkedChildView= _linked_childern
                LinkedChildView.adapter = LinkedChildAdaptor(ChildList)
                LinkedChildView.layoutManager =  LinearLayoutManager(activity)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_childern, container, false)
    }

    private fun dummyData (nameArray: ArrayList<String>,dateArray: ArrayList<String>,dayArray: ArrayList<String>): List<LinkedChildData>{
        val list = ArrayList<LinkedChildData>()
        var iterator =0
        for (name in nameArray){
            list.add(LinkedChildData(nameArray[iterator],dateArray[iterator],dayArray[iterator]))
            iterator++
        }
        return list
    }

}