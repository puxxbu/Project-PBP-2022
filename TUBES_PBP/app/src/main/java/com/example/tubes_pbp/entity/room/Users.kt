package com.example.tubes_pbp.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val password: String,
    val nama: String,
    val email: String,
    val noHP: String,
    val tglLahir: String
)