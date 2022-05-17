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
import kotlinx.android.synthetic.main.captured_voice_card.view.*


class CapturedAudiosAdaptor(private val voiceList: List<CapturedVoiceData>): RecyclerView.Adapter<CapturedAudiosAdaptor.CaptureAudiosHolder>() {
    class CaptureAudiosHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val userName = itemView._user_name
        val date = itemView._captured_date
        val day = itemView._captured_day
        val stopButton = itemView._stop
        val downloadButton = itemView._download
        val playButton = itemView._play
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
        var mp = MediaPlayer()
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
        var timeDuration = 1





        holder.downloadButton.setOnClickListener {
            holder.downloadButton.isVisible = false
            holder.playButton.isVisible = true
            Log.d("LINK",
                currentItem.link)
            mp.setDataSource(currentItem.link + "?alt=media&token=b7813aa8-32e3-43c5-a537-130962fe6a47")
            mp.prepare()
            flag = true
        }

        holder.playButton.setOnClickListener {
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