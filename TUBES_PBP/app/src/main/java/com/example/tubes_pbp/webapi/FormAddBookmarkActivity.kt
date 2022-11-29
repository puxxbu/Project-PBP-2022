package com.example.tubes_pbp.webapi

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tubes_pbp.databinding.ActivityFormAddBookmarkBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.list_data_bookmark.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.*

class FormAddBookmarkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormAddBookmarkBinding
    private var b:Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        b = intent.extras
        val dataByScan = b?.getString("rawValue")
        if (dataByScan != null){
            var gson = Gson()
            var testModel = gson.fromJson(dataByScan, BookmarkData::class.java)
            binding.txtNama.setText(testModel.nama)
            binding.txtAlamat.setText(testModel.alamat)
        }



        binding.btnAdd.setOnClickListener { saveData()
//            MotionToast.Companion.createToast( this, "Create Data is Success",
//                "Data Hotel berhasil Disimpan",
//                MotionToast.TOAST_SUCCESS,
//                MotionToast.GRAVITY_BOTTOM,
//                MotionToast.LONG_DURATION,
//                null
//            )
        }
    }
    fun saveData(){

        with(binding) {
            if (txtNama!!.text.toString().isEmpty()) {
                Toast.makeText(
                    this@FormAddBookmarkActivity,
                    "Nama Hotel tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (txtAlamat!!.text.toString().isEmpty()) {
                Toast.makeText(
                    this@FormAddBookmarkActivity,
                    "Alamat Hotel tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (txtNama!!.text.toString().isEmpty() && txtAlamat!!.text.toString().isEmpty()) {
                Toast.makeText(
                    this@FormAddBookmarkActivity,
                    "Nama dan Alamat Hotel tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                val nama = txtNama.text.toString()
                val alamat = txtAlamat.text.toString()

                RClient.instances.createData(nama, alamat).enqueue(object :
                    Callback<ResponseCreate> {
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if (response.isSuccessful) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Data Berhasil ditambahkan !",
//                                Toast.LENGTH_LONG
//                            ).show()
                            MotionToast.Companion.createToast( this@FormAddBookmarkActivity, "Create Data is Success",
                                "Data Hotel berhasil Disimpan",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                null
                            )
                            finish()
                        } else {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            txtNama.setError(jsonObj.getString("message"))
                            Toast.makeText(
                                applicationContext,
                                "Maaf sudah ada datanya",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                    }
                })
            }
        }
    }
    private fun dateToString(year: Int, month: Int, dayofMonth:
    Int): String {
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