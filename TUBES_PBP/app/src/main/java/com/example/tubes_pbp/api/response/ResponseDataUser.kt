package com.example.tubes_pbp.api.response

import com.example.tubes_pbp.api.UserData
import com.google.gson.annotations.SerializedName

data class ResponseDataUser(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<UserData>
)
