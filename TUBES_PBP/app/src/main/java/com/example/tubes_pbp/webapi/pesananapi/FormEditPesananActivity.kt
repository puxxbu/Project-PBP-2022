package com.example.tubes_pbp.webapi.pesananapi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tubes_pbp.HomeActivity
import com.example.tubes_pbp.databinding.ActivityFormEditPesananBinding
import com.example.tubes_pbp.webapi.pesananapi.PesananData
import com.example.tubes_pbp.webapi.pesananapi.RClient
import com.example.tubes_pbp.webapi.ResponseCreate
import com.example.tubes_pbp.webapi.pesananapi.ResponseDataPesanan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.*
import kotlin.collections.ArrayList

class  FormEditPesananActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormEditPesananBinding
    private var b:Bundle? = null
    private val listPesanan = ArrayList<PesananData>()
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = "Form Edit Bookmark"
        supportActionBar?.hide()
        b = intent.extras
        val id = b?.getInt("id")
        id?.let { getDetailData(it) }
        binding.btnUpdate.setOnClickListener {
            with(binding) {
                val nama = txtEditNama.text.toString()
                val wilayah = txtEditWilayah.text.toString()
                val alamat = txtEditAlamat.text.toString()

                RClient.instances.updateData(id,nama,wilayah,alamat).enqueue(object :
                    Callback<ResponseCreate> {
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            var i = Intent(this@FormEditPesananActivity,HomeActivity::class.java).apply {
                                putExtra("isUpdated",true)
                            }
                            startActivity(i)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                    }
                })
            }

            MotionToast.Companion.createToast( this, "Update Data is Success",
                "Data Hotel Pilihan sudah terUpdate",
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
        }
    }
    fun getDetailData(id:Int) {
        RClient.instances.getData(id.toString()).enqueue(object :
            Callback<ResponseDataPesanan> {
            override fun onResponse(
                call: Call<ResponseDataPesanan>,
                response: Response<ResponseDataPesanan>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        listPesanan.addAll(it.data) }
                    with(binding) {
                        txtEditNama.setText(listPesanan[0].nama)
                        txtEditWilayah.setText(listPesanan[0].wilayah)
                        txtEditAlamat.setText(listPesanan[0].alamat)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseDataPesanan>, t: Throwable) {
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