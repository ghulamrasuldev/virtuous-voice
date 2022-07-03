package com.example.virtuousvoice.Adaptors

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.virtuousvoice.Adaptors.CapturedAudiosAdaptor.*
import com.example.virtuousvoice.DataClasses.CapturedVoiceData
import com.example.virtuousvoice.R
import com.example.virtuousvoice.utilties.Common
import com.example.virtuousvoice.utilties.Common.AUDIO_LINK
import com.example.virtuousvoice.utilties.Common.NEW_TO_DASHBOARD
import com.example.virtuousvoice.utilties.Common.TOXIC_AUDIO_COLLECTION
import com.example.virtuousvoice.utilties.Common.USER_NAME
import com.example.virtuousvoice.utilties.Common.USER_PHONE
import com.example.virtuousvoice.utilties.Common.db
import kotlinx.android.synthetic.main.captured_voice_card.view.*


class CapturedAudiosAdaptor(private val voiceList: List<CapturedVoiceData>): RecyclerView.Adapter<CapturedAudiosAdaptor.CaptureAudiosHolder>() {
    class CaptureAudiosHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val userName = itemView._user_name
        val date = itemView._captured_date
        val day = itemView._captured_day
        val stopButton = itemView._stop
        val downloadButton = itemView._download
        val playButton = itemView._play
        val newToDashboard= itemView._new_to_dashboard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaptureAudiosHolder {
        val itemView = LayoutInflater.from(parent.context ).inflate(R.layout.captured_voice_card,parent,false)
        return CaptureAudiosHolder(itemView)
    }

    override fun onBindViewHolder(holder: CaptureAudiosHolder, position: Int) {
        val currentItem = voiceList[position]
        var flag = false
        var playingStatus = "stopped"
        holder.userName.text = currentItem.userName
        holder.date.text = currentItem.date
        holder.day.text = currentItem.day
        if (currentItem.newToDashboard){
            holder.newToDashboard.isVisible = currentItem.newToDashboard
            db.collection(TOXIC_AUDIO_COLLECTION)
                .whereEqualTo(USER_NAME, currentItem.userName)
                .get()
                .addOnSuccessListener { documuents->
                for (document in documuents){
                    if (currentItem.link == document.data[AUDIO_LINK]){
                        db.collection(TOXIC_AUDIO_COLLECTION).document(document.id).update(
                            NEW_TO_DASHBOARD, false)
                    }
                }
            }
        }

        var mp = MediaPlayer()
        //mp.setAudioStreamType(AudioManager.MODE_NORMAL)
        var timeDuration = 1


        holder.downloadButton.setOnClickListener {
            holder.downloadButton.isVisible = false
            holder.playButton.isVisible = true
            Log.d("LINK",
                currentItem.link)
            mp.setDataSource(currentItem.link)
            mp.prepare()
            flag = true
        }

        holder.playButton.setOnClickListener {
            holder.newToDashboard.isVisible = false
            holder.playButton.isVisible = false
            holder.stopButton.isVisible = true
            mp.start()
        }

        holder.stopButton.setOnClickListener {
            holder.playButton.isVisible = true
            holder.stopButton.isVisible = false
            mp.pause()
        }
    }
    override fun getItemCount() = voiceList.size
}