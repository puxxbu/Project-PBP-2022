package com.example.tubes_pbp.entity.room

class UserRepository(private val usersDao: UsersDao) {

    val readAllData: List<Users> = usersDao.readAllData()

    suspend fun addUser(users: Users){
        usersDao.addUsers(users)
    }



}