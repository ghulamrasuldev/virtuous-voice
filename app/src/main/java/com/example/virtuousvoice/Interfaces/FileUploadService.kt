package com.example.virtuousvoice.Interfaces

import com.example.virtuousvoice.utilties.Common.PREDICT
import com.example.virtuousvoice.utilties.ToxicApiInput
import com.example.virtuousvoice.utilties.ToxicApiOutput
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body

interface FileUploadService {
    @POST (PREDICT)
    suspend fun MakeApiCall(@Body body: ToxicApiInput) :Response<ToxicApiOutput>
}