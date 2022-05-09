package com.example.virtuousvoice.utilties

import com.google.gson.annotations.SerializedName

data class childData(
    @SerializedName("name")
    val name: String,

    @SerializedName("addedTime")
    val lastNotified: Long,

    @SerializedName("documentID")
    val documentID: String
)