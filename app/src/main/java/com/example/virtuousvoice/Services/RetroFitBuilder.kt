package com.example.virtuousvoice.Services

import com.example.virtuousvoice.Interfaces.FileUploadService
import com.google.android.gms.common.api.Api
import retrofit2.Retrofit


class RetrofitClient private constructor() {
    private lateinit var retrofit: Retrofit
    val api: FileUploadService
        get() = retrofit.create(FileUploadService::class.java)

    companion object {
        private const val BASE_URL = "http://192.168.43.244/Android%20Tutorials/"
        private var myClient: RetrofitClient? = null

        @get:Synchronized
        val instance: RetrofitClient?
            get() {
                if (myClient == null) {
                    myClient = RetrofitClient()
                }
                return myClient
            }
    }


}


