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

    @Query("SELECT * FROM users WHERE id=:users_id")
    suspend fun getUsers(users_id: Int) : List<Users>
}