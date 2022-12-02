package com.example.tubes_pbp.webapi

import com.example.tubes_pbp.webapi.userApi.ResponseDataUser
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
    @PUT("bookmarks/{id}")
    fun updateData(
        @Path("id") id:Int?,
        @Field("nama") nama:String?,
        @Field("alamat") alamat:String?,
    ):Call<ResponseCreate>

    //-----------------------------API USER

    @GET("users/{cari}")
    fun getDataUser(@Path("cari") cari:Int? = null): Call<ResponseDataUser>

    @GET("users")
    fun getAllDataUser(): Call<ResponseDataUser>

    @FormUrlEncoded
    @POST("users")
    fun createDataUser(
        @Field("username") username:String?,
        @Field("password") password:String?,
        @Field("nama") nama:String?,
        @Field("email") email:String?,
        @Field("noHP") noHP:String?,
        @Field("tglLahir") tglLahir:String?,
    ): Call<ResponseCreate>
    @DELETE("users/{id}")
    fun deleteDataUser(@Path("id")id:
                       Int?): Call<ResponseCreate>
    @FormUrlEncoded
    @PUT("users/{id}")
    fun updateDataUser(
        @Path("id") id:Int?,
        @Field("username") username:String?,
        @Field("password") password:String?,
        @Field("nama") nama:String?,
        @Field("email") email:String?,
        @Field("noHP") noHP:String?,
        @Field("tglLahir") tglLahir:String?,
    ): Call<ResponseCreate>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("username") username:String?,
        @Field("password") password:String?,
    ): Call<ResponseCreate>
}
