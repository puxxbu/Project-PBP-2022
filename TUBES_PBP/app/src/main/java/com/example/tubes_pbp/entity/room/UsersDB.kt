package com.example.tubes_pbp.entity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Users::class, Pesanan::class], version = 1)
abstract class UsersDB: RoomDatabase() {
    abstract fun usersDao() : UsersDao
    abstract fun pesananDao() : PesananDao

    companion object{
        @Volatile
        private var INSTANCE: UsersDB? = null

        fun getDatabase(context: Context): UsersDB{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return  tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersDB::class.java,
                    "users_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}