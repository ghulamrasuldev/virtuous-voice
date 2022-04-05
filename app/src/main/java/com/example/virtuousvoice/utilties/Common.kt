package com.example.virtuousvoice.utilties

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.virtuousvoice.Views.WelcomeScreen
import com.example.virtuousvoice.database.userTable
import com.example.virtuousvoice.database.userViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_signup.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object Common {
    const val APP_NAME: String = "virtuous-voice"
    const val USER_TYPE: String = "userType"
    const val USER_TYPE_PARENT: String = "parent"
    const val USER_TYPE_CHILD: String = "child"
    const val USER_NAME= "userName"
    const val USER_EMAIL = "eMail"
    const val TOKEN = "token"
    const val LOGIN_STATUS = "LoginStatus"
    const val LOGGED_IN = "LoggedIn"
    const val NOT_LOGGED_IN = "NotLoggedIn"
    const val USER_PHONE="phoneNumber"
    const val DEVICE_TOKEN = "deviceToken"
    const val PARENT_EMAIL = "parentEmail"
    const val ACTIVE_STATUS = "ActiveStatus"
    const val CHILD_NAME ="childName"
    const val DATE ="date"
    const val DAY = "day"
    const val USER_COLLECTION = "users"
    const val LINKED_CHILDS = "linkedChilds"
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
    const val CONTINUE_WITH_PHONE = "login with phone"
    const val CONTINUE_WITH_EMAIL = "login with email"
    var continueWith: String = ""
    var userType: String = ""
    var userEmail: String = ""
    var userName: String = ""
    var userPhone: String = ""
    var status: Boolean = false
    val db = Firebase.firestore
    val sample_audio = "https://firebasestorage.googleapis.com/v0/b/virtuousvoice-7efd1.appspot.com/o/toxicData%2Fhello%40gmail.com%2Fghulamrasuldev%2Fsample.mp3?alt=media&token=b7813aa8-32e3-43c5-a537-130962fe6a47"



    fun sleep(){
        Handler(Looper.getMainLooper()).postDelayed({
        },2000)
    }
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


    fun sendEmail(parentEmail: String, childName: String, body: String, subject: String){
        Log.d("Email Status: ","Sending")
        //authentication info
        val username = "ghulamrasool.testing@yahoo.com"
        val password = "vtmjjqhvheybeeso"
        val fromEmail = "ghulamrasool.testing@yahoo.com"
        val toEmail = parentEmail
        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.mail.yahoo.com"
        properties["mail.smtp.port"] = "587"

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
        //Start our mail message
        //Start our mail message
        val msg = MimeMessage(session)
        Thread{
            try {
                msg.setFrom(InternetAddress(fromEmail))
                msg.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
                msg.subject = "$subject"
                val emailContent: Multipart = MimeMultipart()

                //Text body part
                val textBodyPart = MimeBodyPart()
                textBodyPart.setText("$body")

                //Attach body parts
                emailContent.addBodyPart(textBodyPart)

                //Attach multipart to message
                msg.setContent(emailContent)
                Transport.send(msg)
                Log.d("Email Status: ","Sent")
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun updateUser(mNumber: String, mUserStatus: Boolean, mUserType: String, mUserEmail: String, mUserName: String, mOwner: ViewModelStoreOwner){
        userPhone = mNumber
        status = mUserStatus
        userType = mUserType
        userEmail = mUserEmail
        userName = mUserName
        val mUserViewModel = ViewModelProvider(mOwner).get(userViewModel::class.java)
        mUserViewModel.updateUser(
            userTable(
                0,
                USER_TYPE_CHILD,
                userEmail,
                userName,
                userPhone,
                true
            )
        )
    }
}