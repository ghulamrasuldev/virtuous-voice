package com.example.virtuousvoice.Fragments


import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.virtuousvoice.R
import com.google.common.net.HttpHeaders.FROM
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.sun.mail.imap.SortTerm.FROM
import kotlinx.android.synthetic.main.fragment_setting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import java.util.regex.Pattern
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlin.random.Random.Default.nextInt
import java.io.IOException

import android.widget.Toast

import android.util.Base64.encodeToString

import android.R.attr.path
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import com.example.virtuousvoice.Interfaces.FileUploadService

import com.example.virtuousvoice.MainActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader

import java.io.InputStream





class SettingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _test_email.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
            Log.d("Uri",": ${intent.data}")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            Log.d("Uri", ": ${selectedFile}")

            callMachineLearningApi(selectedFile)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    fun sendFile(){

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
            textBodyPart.setText("Test Notification")

            //Attach body parts
            emailContent.addBodyPart(textBodyPart)

            //Attach multipart to message
            msg.setContent(emailContent)
            Transport.send(msg)
            println("Sent message")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    fun callMachineLearningApi(fileUri: Uri??): Boolean{
        val uuid: RequestBody = RequestBody.create(MultipartBody.FORM, "-jx-1")
        val alarmType: RequestBody = RequestBody.create(MultipartBody.FORM, "1")
        val timeDuration: RequestBody = RequestBody.create(MultipartBody.FORM, "10")

        val originalAudioFile: File = File(fileUri!!.path)
        val AudioFile: RequestBody = RequestBody.create(MediaType.parse(
            this.requireContext().contentResolver.getType(fileUri)),
            originalAudioFile
        )

        val files : MultipartBody.Part = MultipartBody.Part.createFormData("files", originalAudioFile.name, AudioFile)

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://virtuous-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())

        var gson = Gson()
        var AudioFiles = gson.toJson(originalAudioFile)
        val JSON = MediaType.get("application/json; charset=utf-8")
        val AudioFiless: RequestBody = RequestBody.create(JSON,AudioFiles)
        val AudioFilesss: MultipartBody.Part = MultipartBody.Part.createFormData("AudioFile", originalAudioFile.name, AudioFiless)
//        var AudioFiles: JSONObject = JSONObject("AudioFile", originalAudioFile)

        val retrofit: Retrofit = builder.build()
        val fileUpload: FileUploadService = retrofit.create(FileUploadService::class.java)
        val call: Call<ResponseBody> = fileUpload.callApi(AudioFilesss)
        val respons = call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Response: ", response.toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Response: ", t.toString())
            }

        })

        return false
    }

    //generate random output
    fun randomOutput(): Int{
        var result: Int
        result = 1
        return result
    }


}
