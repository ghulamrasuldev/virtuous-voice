package com.example.virtuousvoice.utilties


import com.google.gson.annotations.SerializedName

data class ToxicApiOutput(
    @SerializedName("result")
    val result: Int,
    @SerializedName("transcription")
    val transcription: String
)