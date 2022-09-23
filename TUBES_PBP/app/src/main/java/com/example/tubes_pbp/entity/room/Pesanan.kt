package com.example.tubes_pbp.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pesanan")
data class Pesanan (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val namaHotel: String,
    val wilayahHotel: String,
    val alamatHotel: String
)