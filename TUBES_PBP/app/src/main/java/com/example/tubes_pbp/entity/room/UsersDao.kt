package com.example.tubes_pbp.entity.room

import androidx.room.*

@Dao
interface UsersDao {

    @Insert
    suspend fun addUsers(users: Users)

    @Update
    suspend fun updateUsers(users: Users)

    @Delete
    suspend fun deleteUsers(users: Users)

    @Query ("UPDATE users SET nama=:nama, tglLahir=:tglLahir, noHP=:noHP, email=:email   WHERE id=:id ")
    suspend fun updateUser(id: Int?,nama: String, tglLahir: String, noHP: String, email: String, )

//    @Query("SELECT * FROM users WHERE id=:users_id")
//    fun getUsers(users_id: Int) : List<Users>

    @Query("SELECT * FROM users WHERE username=:user AND password=:pass")
    suspend fun getUser(user: String , pass: String) :Users

    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun getUserbyID(id: Int?) :Users

    @Query ("SELECT * FROM users ")
    fun readAllData() : List<Users>
}