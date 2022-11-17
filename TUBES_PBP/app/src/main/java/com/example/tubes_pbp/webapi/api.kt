package com.example.tubes_pbp.webapi

import retrofit2.Call
import retrofit2.http.*

interface api {
    @GET("bookmarks/{cari}")
    fun getData(@Path("cari") cari:String? = null): Call<ResponseDataBookmark>
    @FormUrlEncoded
    @POST("bookmarks")
    fun createData(
        @Field("nama") nama:String?,
        @Field("alamat") alamat:String?,
    ):Call<ResponseCreate>
    @DELETE("bookmarks/{nama}")
    fun deleteData(@Path("nama")nama:
                   String?):Call<ResponseCreate>
    @FormUrlEncoded
    @PUT("bookmarks/{nama}")
    fun updateData(
        @Path("nama") nama:String?,
        @Field("alamat") alamat:String?,
    ):Call<ResponseCreate>
}
