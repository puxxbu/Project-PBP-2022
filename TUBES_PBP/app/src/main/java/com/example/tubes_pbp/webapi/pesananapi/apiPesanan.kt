package com.example.tubes_pbp.webapi.pesananapi

import com.example.tubes_pbp.webapi.ResponseCreate
import retrofit2.Call
import retrofit2.http.*

interface apiPesanan {
    @GET("pesanans/{cari}")
    fun getData(@Path("cari") cari:String? = null): Call<ResponseDataPesanan>

    @GET("pesanans")
    fun getAllData(): Call<ResponseDataPesanan>

    @FormUrlEncoded
    @POST("pesanans")
    fun createData(
        @Field("nama") nama:String?,
        @Field("wilayah") wilayah:String?,
        @Field("alamat") alamat:String?,
    ): Call<ResponseCreate>
    @DELETE("pesanans/{id}")
    fun deleteData(@Path("id")id:
                   Int?): Call<ResponseCreate>
    @FormUrlEncoded
    @PUT("pesanans/{id}")
    fun updateData(
        @Path("id") id:Int?,
        @Field("nama") nama:String?,
        @Field("wilayah") wilayah:String?,
        @Field("alamat") alamat:String?,
    ): Call<ResponseCreate>
}