package com.example.tubes_pbp.entity.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class UsersDB: RoomDatabase() {
    abstract fun usersDao() : UsersDao

    companion object{
        @Volatile private var instance : UsersDB? = null
        private val LOCK = Any()
        operator  fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
            context.applicationContext,
            UsersDB::class.java,
            "users.db"
        ).build()
    }

}