package com.example.tubes_pbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tubes_pbp.databinding.ActivityDetailBookmarkBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookmarkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBookmarkBinding
    private var b:Bundle? = null
    private val listBookmark = ArrayList<BookmarkData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        b = intent.extras
        val nama = b?.getString("nama")
        nama?.let { getDataDetail(it) }
        binding.btnHapus.setOnClickListener { nama?.let { it1 -> deleteData(it1) }
        }
        binding.btnEdit.setOnClickListener { startActivity(
            Intent(this,
                FormEditBookmarkActivity::class.java).apply {
                putExtra("nama",nama)
            })
        }
    }
    fun getDataDetail(nama:String){ RClient.instances.getData(nama).enqueue(object :
        Callback<ResponseDataBookmark> {
        override fun onResponse(
            call: Call<ResponseDataBookmark>,
            response: Response<ResponseDataBookmark>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    listBookmark.addAll(it.data) }
                with(binding) {
                    tvNama.text = listBookmark[0].nama
                    tvAlamat.text = listBookmark[0].alamat
                }
            }
        }
        override fun onFailure(call: Call<ResponseDataBookmark>, t: Throwable) {
        }
    })
    }
    override fun onRestart() {
        super.onRestart()
        this.recreate()
    }
    fun deleteData(nama:String){
        val builder = AlertDialog.Builder(this@DetailBookmarkActivity)
        builder.setMessage("Anda Yakin mau hapus?? Saya ngga yakin loh.")
            .setCancelable(false)
            .setPositiveButton("Ya, Hapus Aja!"){dialog, id->doDeleteData(nama)
            }
            .setNegativeButton("Tidak, Masih sayang dataku"){dialog,id -> dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
    private fun doDeleteData(nama:String) {
        RClient.instances.deleteData(nama).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse( call: Call<ResponseCreate>,
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