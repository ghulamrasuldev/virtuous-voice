package com.example.virtuousvoice.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.virtuousvoice.R
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.LINKED_CHILDS
import com.example.virtuousvoice.utilties.Common.userEmail
import com.example.virtuousvoice.utilties.Common.userName
import com.example.virtuousvoice.utilties.Common.userPhone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.update_email_fragment.*


class UpdateEmailFragment : BottomSheetDialogFragment() {

    private var auth: FirebaseAuth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    var docId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_email_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _update_email_progress_bar.isVisible = false

        _update_email.setOnClickListener {
            _update_eamil_space.isVisible = true
        }

        _btn_update_email.setOnClickListener {
            _update_eamil_space.isVisible = false
            _update_email_progress_bar.isVisible = true
            db.collection(LINKED_CHILDS).whereEqualTo(Common.USER_PHONE, userPhone)
                .get().addOnSuccessListener { documents->
                    for(document in documents){
                        docId = document.id
                    }
                    if (docId == ""){
                        Log.d("TAG: ", "No user found with phone ${Common.userPhone}")
                        dismiss()
                    }
                    else{
                        Log.d("TAG", "Saving to Store")
                        db.collection(LINKED_CHILDS)
                            .document(docId)
                            .update(Common.USER_EMAIL, _update_email.text.toString())
                            .addOnSuccessListener {
                                userEmail = _update_email.text.toString()
                                _update_email_progress_bar.isVisible = false
                                updateUser()
                                dismiss()
                            }
                    }
                }
        }
    }

    private fun updateUser(){
        Common.userPhone = userPhone
        Common.status = true
        Common.userType = Common.USER_TYPE_CHILD
        userEmail = _update_email.text.toString()
        Common.userName = userName
        val mUserViewModel = ViewModelProvider(this).get(userViewModel::class.java)
        mUserViewModel.updateUser(
            userTable(
                0,
                Common.USER_TYPE_CHILD,
                userEmail,
                Common.userName,
                Common.userPhone,
                true
            )
        )
    }
}