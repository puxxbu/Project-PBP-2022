package com.example.tubes_pbp.webapi.pesananapi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tubes_pbp.databinding.ActivityFormAddPesananBinding
import com.example.tubes_pbp.webapi.ResponseCreate
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

//import android.app.DatePickerDialog
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Toast
//import com.example.tubes_pbp.databinding.ActivityFormAddPesananBinding
//import com.example.tubes_pbp.webapi.ResponseCreate
//import com.google.gson.Gson
//import kotlinx.android.synthetic.main.list_data_pesanan.*
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import www.sanju.motiontoast.MotionToast
//import java.util.*

class FormAddPesananActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormAddPesananBinding
    private var b: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        b = intent.extras
        val dataByScan = b?.getString("rawValue")
        if (dataByScan != null){
            var gson = Gson()
            var testModel = gson.fromJson(dataByScan, PesananData::class.java)
            binding.txtNama.setText(testModel.nama)
            binding.txtWilayah.setText(testModel.wilayah)
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
//            if (txtNama!!.text.toString().isEmpty()) {
//                Toast.makeText(
//                    this@FormAddPesananActivity,
//                    "Nama Hotel tidak boleh kosong!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (txtAlamat!!.text.toString().isEmpty()) {
//                Toast.makeText(
//                    this@FormAddPesananActivity,
//                    "Alamat Hotel tidak boleh kosong!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (txtWilayah!!.text.toString().isEmpty()) {
//                Toast.makeText(
//                    this@FormAddPesananActivity,
//                    "Wilayah Hotel tidak boleh kosong!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (txtWilayah!!.text.toString().isEmpty() && txtNama!!.text.toString().isEmpty() && txtAlamat!!.text.toString().isEmpty()) {
//                Toast.makeText(
//                    this@FormAddPesananActivity,
//                    "Nama, Wilayah dan Alamat Hotel tidak boleh kosong!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            else {
            val nama = txtNama.text.toString()
            val wilayah = txtWilayah.text.toString()
            val alamat = txtAlamat.text.toString()

            RClient.instances.createData(nama, wilayah, alamat).enqueue(object :
                Callback<ResponseCreate> {
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: Response<ResponseCreate>
                ) {
                    if (response.isSuccessful) {
                        MotionToast.Companion.createToast( this@FormAddPesananActivity, "Create Data is Success",
                            "Data Hotel berhasil Disimpan",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )
                        finish()
                    } else {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        if (getError(jsonObj,"nama").isNotEmpty() && getError(jsonObj,"wilayah").isNotEmpty() && getError(jsonObj,"alamat").isNotEmpty()){
                            Toast.makeText(
                                applicationContext,
                                "Isikan kolom nama, wilayah dan alamat terlebih dahulu",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if (getError(jsonObj,"nama").isNotEmpty()){
                            Toast.makeText(
                                applicationContext,
                                "Isikan kolom nama terlebih dahulu",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(getError(jsonObj,"wilayah").isNotEmpty()){
                            Toast.makeText(
                                applicationContext,
                                "Isikan kolom wilayah terlebih dahulu",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(getError(jsonObj,"alamat").isNotEmpty()){
                            Toast.makeText(
                                applicationContext,
                                "Isikan kolom alamat terlebih dahulu",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }
                }

                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                }
            })
 //           }
        }
    }
    fun getError(jsonObject: JSONObject, target: String): String {
        if (jsonObject.has(target)){
            return jsonObject.get(target).toString()
                .replace("[","")
                .replace("]","")
                .replace("]","")
                .replace("\"","")
        }else{
            return ""
        }

    }
}