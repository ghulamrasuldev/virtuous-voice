package com.example.virtuousvoice.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.virtuousvoice.utilties.Common

@Entity(tableName = "users")
data class userTable (
    @PrimaryKey
    val id: Int,
    val userType: String,
    val userEmail: String,
    val userName: String,
    val userPhone: String,
    val LoginStatus: Boolean,
    val FUID: String
)



