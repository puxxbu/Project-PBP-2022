package com.example.tubes_pbp.webapi

import com.example.tubes_pbp.webapi.userApi.UserData
import com.google.gson.annotations.SerializedName
import java.util.*

class ResponseCreate (
    @SerializedName("data") val data:UserData,
    @SerializedName("success") val e:Boolean,
    @SerializedName("message") val pesan:String,
)