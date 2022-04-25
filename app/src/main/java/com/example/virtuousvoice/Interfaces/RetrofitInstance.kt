package com.example.virtuousvoice.Interfaces

import android.util.Log
import com.example.virtuousvoice.utilties.Common.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: FileUploadService by lazy<FileUploadService> {
        Log.e("Request", "Sent")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FileUploadService::class.java)
    }
}