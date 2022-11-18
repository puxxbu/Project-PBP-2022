package com.example.tubes_pbp.webapi

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes_pbp.databinding.ListDataBookmarkBinding

class BookmarkAdapter (
    private val listBookmark:ArrayList<BookmarkData>,
    private val context: Context

): RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
    inner class
    BookmarkViewHolder(item: ListDataBookmarkBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(bookmarkData: BookmarkData){
            with(binding) {
                val idData = bookmarkData.id

                txtNama.text = bookmarkData.nama
                txtAlamat.text = bookmarkData.alamat
                cvData.setOnClickListener {
                    var i = Intent(context,
                        DetailBookmarkActivity::class.java).apply {
                        putExtra("id",bookmarkData.id)
                    }
                    context.startActivity(i)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): BookmarkViewHolder {
        return BookmarkViewHolder(ListDataBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) { holder.bind(listBookmark[position])
    }
    override fun getItemCount(): Int = listBookmark.size
}