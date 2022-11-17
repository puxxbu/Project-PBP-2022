package com.example.tubes_pbp.webapi

import com.google.gson.annotations.SerializedName

data class BookmarkData(
    @SerializedName("nama") val nama:String,
    @SerializedName("alamat") val alamat:String,
)

