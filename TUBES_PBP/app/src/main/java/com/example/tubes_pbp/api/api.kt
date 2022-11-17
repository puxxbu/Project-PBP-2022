package com.example.tubes_pbp.api

import com.example.tubes_pbp.api.response.ResponseCreate
import com.example.tubes_pbp.api.response.ResponseDataUser
import retrofit2.Call
import retrofit2.http.*

interface api {
    @GET("users/{cari}")
    fun getDataByID(@Path("cari") cari:Int? = 0):
            Call<ResponseDataUser>


    @GET("users")
    fun getData():
            Call<ResponseDataUser>
    @FormUrlEncoded

    @POST("users")
    fun createData(

        @Field("nama") nama:String?,
        @Field("username") username:String?,
        @Field("password") password:String?,
        @Field("email") email:String?,
        @Field("noHP") noHP:String?,
        @Field("tglLahir") tglLahir:String?,
    ):Call<ResponseCreate>



}