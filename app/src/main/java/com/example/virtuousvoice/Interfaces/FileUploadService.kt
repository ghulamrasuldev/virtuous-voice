package com.example.virtuousvoice.Interfaces

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call

import okhttp3.ResponseBody
import retrofit2.http.Body

public interface FileUploadService {

    @POST ("https://virtuous-api.herokuapp.com/?transcription=jfhsjfjhsjhfhsfss")
    fun callApi (@Body
        transcription: String
    ) :Call<ResponseBody>

}