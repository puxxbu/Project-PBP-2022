package com.example.tubes_pbp.api

import com.google.gson.annotations.SerializedName

data class UserData(

    @SerializedName("nama") val nama:String,
    @SerializedName("username") val username:String,
    @SerializedName("password") val password:String,
    @SerializedName("email") val email:String,
    @SerializedName("tglLahir") val tglLahir:String,
    @SerializedName("noHP") val noHP:String,
)
