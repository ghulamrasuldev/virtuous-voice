package com.example.virtuousvoice.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update





@Dao
interface userInterfaceDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser (userTable: userTable)

    @Update
    fun updateuser(userTable: userTable)

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun readAllData () : LiveData<List<userTable>>
}