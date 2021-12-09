package com.example.virtuousvoice.Adaptors

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.virtuousvoice.Adaptors.CapturedAudiosAdaptor.*
import com.example.virtuousvoice.DataClasses.CapturedVoiceData
import com.example.virtuousvoice.R
import kotlinx.android.synthetic.main.captured_voice_card.view.*

class CapturedAudiosAdaptor(private val CapturedVoiceList: List<CapturedVoiceData>): RecyclerView.Adapter<CapturedAudiosAdaptor.capturedVoiceViewHolder>() {

    private lateinit var mp: MediaPlayer

    class capturedVoiceViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        val userName : TextView = itemview._user_name
        val date: TextView = itemview._captured_date
        val day: TextView = itemview._captured_day
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): capturedVoiceViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.captured_voice_card,parent,false)
        return capturedVoiceViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: capturedVoiceViewHolder, position: Int) {
        val currentVoice = CapturedVoiceList[position]
        holder.userName.text = currentVoice.userName
        holder.date.text = currentVoice.date
        holder.day.text = currentVoice.day
        mp = MediaPlayer.create(this,currentVoice.capturedAudio)
        holder.itemView._play_button.setOnClickListener{
            if (mp.isPlaying) {
                // Stop
                mp.pause()
                holder.itemView._play_button.setBackgroundResource(R.drawable.icon_play)

            } else {
                // Start
                mp.start()
                holder.itemView._play_button.setBackgroundResource(R.drawable.icon_pause)
            }
        }
    }

    override fun getItemCount(): Int {
        return CapturedVoiceList.size
    }

}