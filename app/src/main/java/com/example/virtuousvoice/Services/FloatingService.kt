package com.example.virtuousvoice.Services

import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.virtuousvoice.Interfaces.RetrofitInstance
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common.AUDIO_LINK
import com.example.virtuousvoice.utilties.Common.DATE
import com.example.virtuousvoice.utilties.Common.DAY
import com.example.virtuousvoice.utilties.Common.NEW_TO_DASHBOARD
import com.example.virtuousvoice.utilties.Common.NEW_TO_SERVICE
import com.example.virtuousvoice.utilties.Common.TOXIC_AUDIO_COLLECTION
import com.example.virtuousvoice.utilties.Common.TOXIC_DATA
import com.example.virtuousvoice.utilties.Common.TOXIC_STATUS
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.USER_TYPE
import com.example.virtuousvoice.utilties.Common.USER_TYPE_CHILD
import com.example.virtuousvoice.utilties.ToxicApiInput
import com.example.virtuousvoice.utilties.ToxicApiOutput
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

val sample = "FILE_NAME.mp3"
val first = "https://firebasestorage.googleapis.com/v0/b/virtuousvoice-7efd1.appspot.com/o/toxicData%2F%2B92"
val last= "?alt=media"

var usertype = ""
var username = ""
var userphone = ""

class FloatingService : Service() {

    private val sharedPrefFile = "virtuousVoice"

    val db = Firebase.firestore

    companion object {
        val FOLDER_PATH = Environment.getExternalStorageDirectory().absolutePath.toString() + "/CallRecordings"
    }

    lateinit var mWindowManager: WindowManager
    lateinit var mOverlayView: View
    private val recorder = MediaRecorder()
    private lateinit var rootLayout: LinearLayout
    private lateinit var textView: TextView
    private lateinit var recordButton: ImageButton
    private lateinit var closeButton: ImageButton
    private lateinit var recordingLogo: ImageButton
    private lateinit var recordingMessage: TextView
    private var isRecording = false
    private lateinit var storageRef: StorageReference
    var finalPath = ""
    var fileName = ""

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedPref: SharedPreferences =  sharedPreferences
        usertype = sharedPref.getString(USER_TYPE, "").toString()
        username = sharedPref.getString(USER_NAME, "").toString()
        userphone = sharedPref.getString(USER_PHONE, "").toString()



        // difference in layout params devices greater than O
        Log.d("TAG","$FOLDER_PATH")
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        mOverlayView = LayoutInflater.from(this).inflate(R.layout.layout_overlay, null)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        //Specify the view position
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 8
        params.y = 8

        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager.addView(mOverlayView, params)

        if (usertype == USER_TYPE_CHILD){
            Log.d("Floating Service!", "Started Recording")
            startRecording()
        }
        else{
            Log.d("Floating Service!", "Not Recording")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mWindowManager.removeView(mOverlayView)
        if (usertype == USER_TYPE_CHILD){
            stopRecorder()
        }
    }


    private fun startRecording() {
        fileName = generateFileName()+".mp3"
        val path = "$FOLDER_PATH/$fileName"
        finalPath = path
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder.setOutputFile(path)
        recorder.prepare()
        recorder.start()
    }

    private fun stopRecorder() {
        Log.d("Stop Recorder; ", "True")
        if (isRecording) {
            recorder.stop()
            recorder.reset()
        }
        recorder.release()

        UploadAudio()
    }

    private fun UploadAudio() {
        Log.d("Upload Audio Function; ", "True")
        storageRef = FirebaseStorage.getInstance().reference
        var file = File(finalPath)
        var ref = storageRef.child("toxicData").child(userphone).child(username).child(fileName)
        Log.d("TAG", "$file")
        Log.d("TAG", finalPath)
        ref.putFile(Uri.fromFile(file)).addOnSuccessListener {TaskSnapshot->
            val url  = generateURL()
            //Get Date
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val c = Calendar.getInstance()
            val date = sdf.format(c.time)

            //Get Day
            val time = c.getTime()
            val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(time.getTime())

            val audioSample = hashMapOf(
                USER_NAME to username,
                USER_PHONE to userphone,
                AUDIO_LINK to url,
                NEW_TO_SERVICE to true,
                NEW_TO_DASHBOARD to true,
                TOXIC_DATA to "",
                DATE to date,
                DAY to day,
                TOXIC_STATUS to null
            )

            db.collection(TOXIC_AUDIO_COLLECTION).add(audioSample).addOnSuccessListener {
                Log.d("Firestore", "Audio Added Successfully!")
                Log.d("Firestore", "url")
                CallApi(url, it.id, ref)
            }
        }
    }

    private fun CallApi(link: String, id: String, ref: StorageReference) {
        Log.d("Api Call; ", "True")
        runBlocking{
            var response: Response<ToxicApiOutput>
            try {
                response  = RetrofitInstance.api.MakeApiCall(
                    //Make Api call
                    ToxicApiInput(
                        "$link"
                    )
                )
            } catch (e: IOException){
                Log.e(ContentValues.TAG, "IOException: You might not have internet connection! $e")
                return@runBlocking
            }catch (e: HttpException){
                Log.e(ContentValues.TAG, "IOException: Unexpected Response! $e")
                return@runBlocking
            }
            if(response.isSuccessful && response.body()!=null){
                Log.d("TAG", response.body()!!.transcription)

                if (response.body()!!.result == 1){
                    Log.d("File Name", fileName)
                    ref.delete()
                    db.collection(TOXIC_AUDIO_COLLECTION)
                        .document(id)
                        .update(
                            TOXIC_STATUS, true, TOXIC_DATA,
                            response.body()!!.transcription
                        )
                    Log.d("Declared", "Toxic")

                }else if (response.body()!!.result == 0){
                    Log.d("Doument is Non-Toxic", "Deleting")
                    db.collection(TOXIC_AUDIO_COLLECTION).document(id).delete()
                    Log.d("Declared", "Non - Toxic")
                }
            }
            else{
                Toast.makeText(baseContext, "not working", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun generateURL() = first + userphone.substring(3) + "%2F" + "$username" + "%2F" + fileName + last

    private fun generateFileName() = System.currentTimeMillis().toString()
}