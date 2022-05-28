package com.example.virtuousvoice.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class userViewModel(application: Application): AndroidViewModel(application){
    val readAllData : LiveData<List<userTable>>
    private val repository : userRepository

    init {
        val userDAO = userDatabase.getDatabase(application).userDAO()
        repository = userRepository(userDAO)
        readAllData = repository.readAllData
    }

    fun saveUser (userTable: userTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(userTable)
        }
    }

    fun updateUser(userTable: userTable){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateUser(userTable)
        }
    }
}