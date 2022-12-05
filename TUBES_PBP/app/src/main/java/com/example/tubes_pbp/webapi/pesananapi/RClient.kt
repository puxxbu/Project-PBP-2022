package com.example.tubes_pbp.webapi.pesananapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RClient {
    private const val BASE_URL = "https://gentle-scrubland-87023.herokuapp.com/api/"
    val instances:apiPesanan by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(apiPesanan::class.java)
    }
}