package com.example.virtuousvoice.database

import androidx.lifecycle.LiveData


class userRepository (private val userInterfaceDAO: userInterfaceDAO){

    val readAllData : LiveData<List<userTable>> = userInterfaceDAO.readAllData()

    suspend fun addUser(userTable: userTable){
        userInterfaceDAO.addUser(userTable)
    }

    suspend fun updateUser(userTable: userTable){
        userInterfaceDAO.updateuser(userTable)
    }
}