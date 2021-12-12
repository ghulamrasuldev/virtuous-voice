package com.example.virtuousvoice.Interfaces

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call

import okhttp3.ResponseBody

public interface FileUploadService {

    @Multipart
    @POST ("https://virtuous-api.herokuapp.com/")
    fun upload (
        @Part("uuid") uuid: String,
        @Part("alarmType") alarmType: String,
        @Part("timeDuration") timeDuration:String,
        @Part("AudioFile") AudioFile: MultipartBody.Part,
    ) :Call<ResponseBody>

}