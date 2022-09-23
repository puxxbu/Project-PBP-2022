package com.example.tubes_pbp.entity.room

import androidx.room.*

@Dao
interface PesananDao {

    @Insert
    suspend fun addBookmark(pesanan: Pesanan)

    @Update
    suspend fun updateBookmark(pesanan: Pesanan)

    @Delete
    suspend fun deleteBookmark(pesanan: Pesanan)

    @Query("SELECT * FROM pesanan")
    suspend fun getBookmark() : List<Pesanan>

    @Query("SELECT * FROM pesanan WHERE id =:pesanan_id")
    suspend fun getBookmark(pesanan_id : Int) : List<Pesanan>
}