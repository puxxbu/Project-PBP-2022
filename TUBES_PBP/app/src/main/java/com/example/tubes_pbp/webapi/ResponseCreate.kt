package com.example.tubes_pbp.webapi

import com.google.gson.annotations.SerializedName
import java.util.*

class ResponseCreate (
    @SerializedName("data") val data:Objects,
    @SerializedName("success") val e:Boolean,
    @SerializedName("message") val pesan:String,
)