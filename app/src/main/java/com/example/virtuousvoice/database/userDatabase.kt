package com.example.virtuousvoice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [userTable::class], version = 2, exportSchema = false)
abstract class userDatabase: RoomDatabase(){
    abstract fun userDAO(): userInterfaceDAO
    companion object{
        @Volatile
        private var INSTANCE: userDatabase? = null

        fun getDatabase(context: Context) : userDatabase {
            val tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    userDatabase::class.java,
                    "users"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}