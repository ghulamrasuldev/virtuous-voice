package com.example.virtuousvoice.Adaptors

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.virtuousvoice.Adaptors.CapturedAudiosAdaptor.*
import com.example.virtuousvoice.DataClasses.CapturedVoiceData
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.captured_voice_card.view.*


class CapturedAudiosAdaptor(private val voiceList: List<CapturedVoiceData>): RecyclerView.Adapter<CapturedAudiosAdaptor.CaptureAudiosHolder>() {
    class CaptureAudiosHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val userName = itemView._user_name
        val date = itemView._captured_date
        val day = itemView._captured_day
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaptureAudiosHolder {
        val itemView = LayoutInflater.from(parent.context ).inflate(R.layout.captured_voice_card,parent,false)
        return CaptureAudiosHolder(itemView)
    }

    override fun onBindViewHolder(holder: CaptureAudiosHolder, position: Int) {
        val currentItem = voiceList[position]
        holder.userName.text = currentItem.userName
        holder.date.text = currentItem.date
        holder.day.text = currentItem.day
    }

    override fun getItemCount() = voiceList.size

}

//class CapturedAudiosAdaptor(private val CapturedVoiceList: List<CapturedVoiceData>): RecyclerView.Adapter<CapturedAudiosAdaptor.CapturedVoiceViewHolder>() {
//
//    private lateinit var mp: MediaPlayer
//    private var totalTime: Int = 0
//
//    class CapturedVoiceViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
//        val userName : TextView = itemview._user_name
//        val date: TextView = itemview._captured_date
//        val day: TextView = itemview._captured_day
////        val positionBar:SeekBar = itemview._positionBar
////        val playButton = itemview._play_button
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapturedVoiceViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.captured_voice_card,parent,false)
//        return CapturedVoiceViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: CapturedVoiceViewHolder, position: Int) {
//        val currentVoice = CapturedVoiceList[position]
//        holder.userName.text = currentVoice.userName
//        holder.date.text = currentVoice.date
//        holder.day.text = currentVoice.day
//
////        //testing
////        val audio = currentVoice.capturedAudio
////        mp = MediaPlayer.create(holder.itemView.context, audio)
////        mp.setVolume(0.5f, 0.5f)
////        totalTime = mp.duration
////        holder.itemView._play_button.setOnClickListener{
////            if (mp.isPlaying) {
////                // Stop
////                mp.pause()
////                holder.playButton.setBackgroundResource(R.drawable.icon_play)
////
////            } else {
////                // Start
////                mp.start()
////                holder.playButton.setBackgroundResource(R.drawable.icon_pause)
////            }
////        }
////
////        //position bar
////        holder.positionBar.max = totalTime
////        holder.positionBar.setOnSeekBarChangeListener(
////            object : SeekBar.OnSeekBarChangeListener {
////                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
////                    if (fromUser) {
////                        mp.seekTo(progress)
////                    }
////                }
////                override fun onStartTrackingTouch(p0: SeekBar?) {
////                }
////                override fun onStopTrackingTouch(p0: SeekBar?) {
////                }
////            }
////        )
////
////        // Thread
////        Thread(Runnable {
////            while (mp != null) {
////                try {
////                    var msg = Message()
////                    msg.what = mp.currentPosition
////                    Thread.sleep(1000)
////                } catch (e: InterruptedException) {
////                }
////            }
////        }).start()
////
////        @SuppressLint("HandlerLeak")
////        var handler = object : Handler() {
////            override fun handleMessage(msg: Message) {
////                var currentPosition = msg.what
////
////                // Update positionBar
////                holder.positionBar.progress = currentPosition
////            }
////        }
//    }
//
//    override fun getItemCount(): Int {
//        return CapturedVoiceList.size
//    }
//}