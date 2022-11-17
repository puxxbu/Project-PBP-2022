package com.example.tubes_pbp

import com.google.gson.annotations.SerializedName

data class BookmarkData(
    @SerializedName("nama") val nama:String,
    @SerializedName("alamat") val alamat:String,
)

