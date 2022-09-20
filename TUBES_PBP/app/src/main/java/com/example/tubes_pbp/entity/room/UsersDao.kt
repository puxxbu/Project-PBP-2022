package com.example.tubes_pbp.entity.room

import androidx.room.*

@Dao
interface UsersDao {

    @Insert
     fun addUsers(users: Users)

    @Update
     fun updateUsers(users: Users)

    @Delete
     fun deleteUsers(users: Users)

    @Query("SELECT * FROM users WHERE id=:users_id")
    fun getUsers(users_id: Int) : List<Users>
}