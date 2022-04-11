package com.example.virtuousvoice.Services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.virtuousvoice.R
import java.text.SimpleDateFormat
import java.util.*

class FloatingService : Service() {

    companion object {
        val FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + "/CallRecordings"
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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        setTheme(R.style.CustomTabStyle)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // difference in layout params devices greater than O
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
        params.x = 50
        params.y = 50

        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager.addView(mOverlayView, params)

        startRecording()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mWindowManager.removeView(mOverlayView)
        stopRecorder()
    }


    private fun startRecording() {
        val fileName = "REC-${getCurrentDateTime()}.mp3"
        val path = "$FOLDER_PATH/$fileName"
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder.setOutputFile(path)
        recorder.prepare()
        recorder.start()
    }

    private fun stopRecorder() {
        if (isRecording) {
            recorder.stop()
            recorder.reset()
        }
        recorder.release()
    }

    private fun getCurrentDateTime() =
        SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss-Sa", Locale.ENGLISH).format(Date())


}