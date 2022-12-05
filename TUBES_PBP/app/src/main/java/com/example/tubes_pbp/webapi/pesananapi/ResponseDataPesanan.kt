package com.example.tubes_pbp.webapi.pesananapi

import com.google.gson.annotations.SerializedName

data class ResponseDataPesanan(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<PesananData>)
