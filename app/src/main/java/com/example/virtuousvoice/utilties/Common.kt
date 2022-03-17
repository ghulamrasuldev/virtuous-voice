package com.example.virtuousvoice.utilties

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

object Common {
    const val APP_NAME: String = "virtuous-voice"
    const val USER_TYPE: String = "userType"
    const val USER_TYPE_PARENT: String = "parent"
    const val USER_TYPE_CHILD: String = "child"
    const val USER_NAME= "userName"
    const val USER_EMAIL = "eMail"
    const val USER_PHONE="phoneNumber"
    const val DEVICE_TOKEN = "deviceToken"
    const val PARENT_EMAIL = "parentEmail"
    const val CHILD_NAME ="childName"
    const val DATE ="date"
    const val DAY = "day"
    const val USER_COLLECTION = "users"
    const val USER_ALREADY_REGISTERED = "A user with this phone number is already registered"
    const val INVALID_EMAIL = "The Email provided is invalid!"
    const val INVALID_PHONE = "The Phone Number provided is invalid!"
    const val INVALID_PASSWORD = "The password is too weak\nThe Password must be 8-24 characters " +
                                 "with at 1 character from each category\n" +
                                 "[a-z],[A-Z],[0-9]"
    const val VERIFY_PASSWORD_ERROR = "Please make sure passwords are same"
    const val EMPTY_FIELDS_ERROR ="Some Field(s) are empty"
    const val AUTHENTICATION_FAILED_ERROR = "Authentication failed."
    const val PASSWORD_PATTERN = "[a-zA-Z0-9]{8,24}"
    const val NO_PARENT_FOUND_ERROR = "No Parent linked with this phone number"
    const val PHONE_REGX = "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}\$|^\\d{11}\$|^\\d{4}-\\d{7}\$"
    private lateinit var auth: FirebaseAuth;
    var userType: String = USER_TYPE_PARENT
    var userEmail: String = ""
    var userName: String = ""
    var userPhone: String = ""
    val db = Firebase.firestore

    //Password Length
    fun String.isPasswordValid(context : Context): Boolean {
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(this)
        return if (matcher.matches()){
            true
        } else{
            Toast.makeText(context, INVALID_PASSWORD, Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun isEmailValid(email: String): Boolean{
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun isValidPhone(phone: String): Boolean{
        return Pattern.compile(PHONE_REGX).matcher(phone).matches()
    }
}