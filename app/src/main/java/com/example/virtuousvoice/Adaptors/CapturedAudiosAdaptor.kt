package com.example.virtuousvoice.Adaptors

import android.annotation.SuppressLint
import android.media.AudioManager
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
import com.example.virtuousvoice.utilties.Common
import kotlinx.android.synthetic.main.captured_voice_card.view.*


class CapturedAudiosAdaptor(private val voiceList: List<CapturedVoiceData>): RecyclerView.Adapter<CapturedAudiosAdaptor.CaptureAudiosHolder>() {
    class CaptureAudiosHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val userName = itemView._user_name
        val date = itemView._captured_date
        val day = itemView._captured_day
        val playButton = itemView._play_button
        val positionBar = itemView.positionBar
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
        var timeDuration = 1


        Thread {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mp.setDataSource(Common.sample_audio)
                mp.prepare()
                flag = true
        }.start()

        holder.playButton.setOnClickListener {
            if (playingStatus == "stopped"){
                holder.playButton.setImageResource(R.drawable.icon_pause)
                mp.start()
                playingStatus = "started"
            }
            else{
                holder.playButton.setImageResource(R.drawable.icon_play)
                mp.pause()
                playingStatus = "stopped"
            }

        }

        // Position Bar
        holder.positionBar.max = timeDuration
        holder.positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )
    }
    override fun getItemCount() = voiceList.size
}