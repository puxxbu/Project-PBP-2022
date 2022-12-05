package com.example.tubes_pbp.webapi.pesananapi

import com.google.gson.annotations.SerializedName

data class PesananData(
    @SerializedName("id") val id:Int,
    @SerializedName("nama") val nama:String,
    @SerializedName("wilayah") val wilayah:String,
    @SerializedName("alamat") val alamat:String,
)

