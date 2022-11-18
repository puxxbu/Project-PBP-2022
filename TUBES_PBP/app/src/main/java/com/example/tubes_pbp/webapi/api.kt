package com.example.tubes_pbp.webapi

import retrofit2.Call
import retrofit2.http.*

interface api {
    @GET("bookmarks/{cari}")
    fun getData(@Path("cari") cari:String? = null): Call<ResponseDataBookmark>

    @GET("bookmarks")
    fun getAllData(): Call<ResponseDataBookmark>

    @FormUrlEncoded
    @POST("bookmarks")
    fun createData(
        @Field("nama") nama:String?,
        @Field("alamat") alamat:String?,
    ):Call<ResponseCreate>
    @DELETE("bookmarks/{id}")
    fun deleteData(@Path("id")id:
                   Int?):Call<ResponseCreate>
    @FormUrlEncoded
    @PUT("bookmarks/{nama}")
    fun updateData(
        @Path("nama") nama:String?,
        @Field("alamat") alamat:String?,
    ):Call<ResponseCreate>
}
