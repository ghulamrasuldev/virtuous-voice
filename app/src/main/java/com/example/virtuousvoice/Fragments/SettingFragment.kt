package com.example.virtuousvoice.Fragments

import android.net.http.HttpResponseCache
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.toolbox.HttpResponse
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class SettingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _test_email.setOnClickListener{
            val url = "https://virtuous-api.herokuapp.com/"
            sendEmail()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    fun sendEmail(){

        //authentication info
        val username = "ghulamrasool.testing@yahoo.com"
        val password = "vtmjjqhvheybeeso"
        val fromEmail = "ghulamrasool.testing@yahoo.com"
        val toEmail = "ghulam.rasool.uni@gmail.com"

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
        try {
            msg.setFrom(InternetAddress(fromEmail))
            msg.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
            msg.subject = "Testing Notification"
            val emailContent: Multipart = MimeMultipart()

            //Text body part
            val textBodyPart = MimeBodyPart()
            textBodyPart.setText("First Notification")

            //Attachment body part.
//            val pdfAttachment = MimeBodyPart()
//            pdfAttachment.attachFile("/home/parallels/Documents/docs/javamail.pdf")

            //Attach body parts
            emailContent.addBodyPart(textBodyPart)
//            emailContent.addBodyPart(pdfAttachment)

            //Attach multipart to message
            msg.setContent(emailContent)
            Transport.send(msg)
            println("Sent message")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
//        catch (e: IOException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }

    }
}
