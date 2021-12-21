package com.example.virtuousvoice

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.virtuousvoice.utilties.Common.CHILD_NAME
import com.example.virtuousvoice.utilties.Common.PARENT_EMAIL
import kotlinx.android.synthetic.main.activity_record_audio.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import org.json.JSONObject
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class RecordAudio : AppCompatActivity() {
    val RecordAudioRequestCode: Int = 102
    private lateinit var speechRecognizer: SpeechRecognizer
    private val client = OkHttpClient()
    private lateinit var parentEmail: String
    private lateinit var childName: String


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio)
        parentEmail = intent.getStringExtra(PARENT_EMAIL).toString()
        childName = intent.getStringExtra(CHILD_NAME).toString()
        _button_mic.setOnClickListener{
            askSpeechInput()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RecordAudioRequestCode && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            Log.d("Result: ", result.toString())
            _transcription.setText(result?.get(0).toString())
            run(result?.get(0).toString())

         }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun askSpeechInput(){
        if (!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "Speech Recognition is not avaialable", Toast.LENGTH_SHORT).show()
        }
        else{
            val speechRecognizer = Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            speechRecognizer.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...")
            startActivityForResult(speechRecognizer,RecordAudioRequestCode)
        }
    }

    private fun run(transcription: String): Boolean {
        var prediction:Boolean = false
        val thread = Thread {
            try {
                //Your code goes here
                val request = Request.Builder()
                    .url("https://virtuous-api.herokuapp.com/?transcription=$transcription")
                    .build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Log.d("response", "${response.body()}")
                    val resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("response", "$json")
                    Log.d("response value: ", "${json["result"]}")
                    if(json["result"].toString()=="1"){
                        prediction=true
                        Log.d("prediction: ", "$prediction")
                    }
                    if(prediction){
                        sendEmail(parentEmail, childName, transcription)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        return prediction
    }

    fun sendEmail(parentEmail: String, childName: String, toxicData: String){
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
                try {
                    msg.setFrom(InternetAddress(fromEmail))
                    msg.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
                    msg.subject = "Toxicity Detected in $childName's Speech"
                    val emailContent: Multipart = MimeMultipart()

                    //Text body part
                    val textBodyPart = MimeBodyPart()
                    textBodyPart.setText("We found some toxicity in your child's ($childName's Speech). Here is the toxic sentense \"$toxicData\". ")

                    //Attach body parts
                    emailContent.addBodyPart(textBodyPart)

                    //Attach multipart to message
                    msg.setContent(emailContent)
                    Transport.send(msg)
                    Log.d("Email Status: ","Sent")
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
    }
}


