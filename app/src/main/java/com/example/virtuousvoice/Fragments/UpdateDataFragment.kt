package com.example.virtuousvoice.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.USER_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_EMAIL
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.userPhone
import com.example.virtuousvoice.utilties.ObjectListiner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.update_data_fragment.*


class UpdateDataFragment(private val listiner: ObjectListiner) : BottomSheetDialogFragment() {

    private var auth: FirebaseAuth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    var docId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_data_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _update_daata_progress_bar.isVisible = false

        _update_data_email.setOnClickListener {
            _update_data_space.isVisible = true
        }

        _update_data_name.setOnClickListener {
            _update_data_space.isVisible = true
        }

        _btn_update_data.setOnClickListener {
            _update_data_space.isVisible = false
            _update_daata_progress_bar.isVisible = true
            db.collection(USER_COLLECTION).whereEqualTo(USER_PHONE, userPhone)
                .get().addOnSuccessListener { documents->
                    for(document in documents){
                        docId = document.id
                    }
                    if (docId == ""){
                        Log.d("TAG: ", "No user found with phone $userPhone")
                        dismiss()
                    }
                    else{
                        db.collection(USER_COLLECTION)
                            .document(docId).update(USER_NAME, _update_data_name.text.toString(),
                                USER_EMAIL, _update_data_email.text.toString()).addOnSuccessListener {
                                listiner.onItemClicked(_update_data_name.text.toString())
                                _update_daata_progress_bar.isVisible = false
                                dismiss()
                            }
                    }
                }
        }
    }
}
