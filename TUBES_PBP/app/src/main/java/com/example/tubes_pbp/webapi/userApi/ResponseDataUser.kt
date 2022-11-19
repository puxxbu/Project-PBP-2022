package com.example.tubes_pbp.webapi.userApi

import com.google.gson.annotations.SerializedName

data class ResponseDataUser(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<UserData>)
