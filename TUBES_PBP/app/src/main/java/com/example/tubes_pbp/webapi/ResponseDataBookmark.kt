package com.example.tubes_pbp.webapi

import com.google.gson.annotations.SerializedName

data class ResponseDataBookmark(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<BookmarkData>)
