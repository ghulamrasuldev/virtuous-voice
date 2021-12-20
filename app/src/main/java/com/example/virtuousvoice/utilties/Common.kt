package com.example.virtuousvoice.utilties

import android.text.TextUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

object Common {
    const val USER_TYPE: String = "usertype"
    const val USER_TYPE_PARENT: String = "parent"
    const val USER_TYPE_CHILD: String = "child"
    const val USER_NAME= "username"
    const val USER_COLLECTION = "users"
    const val INVALID_EMAIL = "The Email provided is invalid!"
    const val INVALID_PHONE = "The Phone Number provided is invalid!"
    const val INVALID_PASSWORD = "The password is too weak\nThe Password must be 8-24 characters " +
                                 "with at 1 character from each category\n" +
                                 "[a-z],[A-Z],[0-9]"
    const val VERIFY_PASSWORD_ERROR = "Please make sure passwords are same"
    const val EMPTY_FIELDS_ERROR ="Some Field(s) are empty"
    const val AUTHENTICATION_FAILED_ERROR = "Authentication failed."
    private const val PASSWORD_PATTERN = "[a-zA-Z0-9]{8,24}"

    //Email Validation
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.isPhoneValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.PHONE.matcher(this).matches()
    }

    fun String.isPasswordValid(): Boolean {
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(this)
        return matcher.matches()
    }
}