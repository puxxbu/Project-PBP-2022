package com.example.tubes_pbp.webapi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tubes_pbp.databinding.ActivityFormEditBookmarkBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class FormEditBookmarkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormEditBookmarkBinding
    private var b:Bundle? = null
    private val listBookmark = ArrayList<BookmarkData>()
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Form Edit Bookmark"
        b = intent.extras
        val id = b?.getInt("id")
        id?.let { getDetailData(it) }
        binding.btnUpdate.setOnClickListener {
            with(binding) {
                val nama = txtEditNama.text.toString()
                val alamat = txtEditAlamat.text.toString()

                RClient.instances.updateData(nama,alamat).enqueue(object :
                    Callback<ResponseCreate> {
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                    }
                })
            }
        }
    }
    fun getDetailData(id:Int) {
        RClient.instances.getData(id.toString()).enqueue(object :
            Callback<ResponseDataBookmark> {
            override fun onResponse(
                call: Call<ResponseDataBookmark>,
                response: Response<ResponseDataBookmark>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        listBookmark.addAll(it.data) }
                    with(binding) {
                        txtEditNama.setText(listBookmark[0].nama)
                        txtEditAlamat.setText(listBookmark[0].alamat)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseDataBookmark>, t: Throwable) {
            }
        })
    }
    private fun dateToString(year: Int, month: Int, dayofMonth: Int): String {
        return year.toString()+"-"+(month+1)+"-"+dayofMonth.toString()
    }
    private fun dateDialog(context: Context, datePicker:
    DatePickerDialog.OnDateSetListener):DatePickerDialog {
        val calender = Calendar.getInstance()
        return DatePickerDialog(
            context, datePicker,
            calender[Calendar.YEAR],
            calender[Calendar.MONTH],
            calender[Calendar.DAY_OF_MONTH],
        )
    }
}