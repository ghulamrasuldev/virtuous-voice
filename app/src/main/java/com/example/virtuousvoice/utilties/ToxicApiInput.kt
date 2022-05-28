package com.example.virtuousvoice.utilties


import com.google.gson.annotations.SerializedName

data class ToxicApiInput(
    @SerializedName("link")
    val link: String
)