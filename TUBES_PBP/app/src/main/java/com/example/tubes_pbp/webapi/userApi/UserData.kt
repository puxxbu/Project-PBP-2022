package com.example.tubes_pbp.webapi.userApi

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("id") val id:Int,
    @SerializedName("username") val username:String,
    @SerializedName("password") val password:String,
    @SerializedName("nama") val nama:String,
    @SerializedName("email") val email:String,
    @SerializedName("noHP") val noHP:String,
    @SerializedName("tglLahir") val tglLahir:String,
)

