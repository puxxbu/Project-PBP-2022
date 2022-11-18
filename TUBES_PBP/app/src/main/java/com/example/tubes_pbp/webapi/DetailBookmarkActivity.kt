package com.example.tubes_pbp.webapi

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.ActivityDetailBookmarkBinding
import com.example.tubes_pbp.fragments.BookmarkFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookmarkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBookmarkBinding
    private var b:Bundle? = null
    private val listBookmark = ArrayList<BookmarkData>()
    val bookmark = ArrayList<BookmarkData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        b = intent.extras
        val id = b?.getInt("id")
        if (id != null) {
            Log.d(TAG, id.toString() + "TESSSS")
            getDataDetail(id)
        }

        id?.let { getDataDetail(it) }
        binding.btnHapus.setOnClickListener { id?.let { it1 -> deleteData(it1) }
        }
        binding.btnEdit.setOnClickListener { startActivity(
            Intent(this,
                FormEditBookmarkActivity::class.java).apply {
                putExtra("id",id)
            })
        }
    }
    fun getDataDetail(id:Int){ RClient.instances.getData(id.toString()).enqueue(object :
        Callback<ResponseDataBookmark> {
        override fun onResponse(
            call: Call<ResponseDataBookmark>,
            response: Response<ResponseDataBookmark>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    listBookmark.addAll(it.data)
                     bookmark.add(listBookmark.find { it.id == id }!!)
                }
                Log.d(TAG,  "getDataDetail")
                with(binding) {
                    tvNama.text = bookmark[0].nama
                    tvAlamat.text = bookmark[0].alamat
                }
            }
        }
        override fun onFailure(call: Call<ResponseDataBookmark>, t: Throwable) {
            t.message?.let { Log.d("failure", it) }
        }
    })
    }
    override fun onRestart() {
        super.onRestart()
        this.recreate()
    }
    fun deleteData(idData:Int){
        val builder = AlertDialog.Builder(this@DetailBookmarkActivity)
        builder.setMessage("Anda Yakin mau hapus?? Saya ngga yakin loh.")
            .setCancelable(false)
            .setPositiveButton("Ya, Hapus Aja!"){dialog, id->doDeleteData(idData)
            }
            .setNegativeButton("Tidak, Masih sayang dataku"){dialog,id -> dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
    private fun doDeleteData(id:Int) {
        RClient.instances.deleteData(id).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(call: Call<ResponseCreate>,
                                    response: Response<ResponseCreate>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Data berhasil dihapus", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
            }
        })
    }




}