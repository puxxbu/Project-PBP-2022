package com.example.tubes_pbp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.tubes_pbp.databinding.ActivityEditAkunBinding
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.webapi.userApi.UserData
import com.example.tubes_pbp.webapi.RClient
import com.example.tubes_pbp.webapi.ResponseCreate
import com.example.tubes_pbp.webapi.userApi.ResponseDataUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditAkun : AppCompatActivity() {
    private lateinit var binding: ActivityEditAkunBinding
    private var b: Bundle? = null
    private val listUser = ArrayList<UserData>()
    private lateinit var prefManager: PrefManager

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAkunBinding.inflate(layoutInflater)
        prefManager = PrefManager(this@EditAkun)
        binding.user = prefManager.getUser()
        setDataUser()
        setContentView(binding.root)
//        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = "Form Edit Akun"
        supportActionBar?.hide()
        b = intent.extras
        val id = b?.getInt("id")
        id?.let { setDataUser() }

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar, binding)
        }

        binding.tietTglLahir.setFocusable(false)
        binding.tietTglLahir.setOnClickListener {
            DatePickerDialog(
                this@EditAkun,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            binding.btnUpdate.setOnClickListener {
                with(binding) {
                    val nama = binding.tilNamaLengkap.getEditText()?.getText().toString()
                    val tglLahir = binding.tilTglLahir.getEditText()?.getText().toString()
                    val noHP = binding.tilNoHP.getEditText()?.getText().toString()
                    val email = binding.tilEmail.getEditText()?.getText().toString()


                    com.example.tubes_pbp.webapi.RClient.instances.updateDataUser(
                        prefManager.getUserID(),
                        listUser[0].username,
                        listUser[0].password,
                        nama,
                        email,
                        noHP,
                        tglLahir
                    ).enqueue(object :
                        Callback<ResponseCreate> {
                        override fun onResponse(
                            call: Call<ResponseCreate>,
                            response: Response<ResponseCreate>
                        ) {
                            if (response.isSuccessful) {
                                binding.textNamaUser.setText(nama)
                                Toast.makeText(
                                    this@EditAkun,
                                    "${response.body()?.pesan}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                        }
                    })
                }

                MotionToast.Companion.createToast(
                    this, "Update Data is Success",
                    "User Data sudah terUpdate",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }





    }
    fun setDataUser() {
        RClient.instances.getDataUser(prefManager.getUserID()).enqueue(object :
            Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listUser.addAll(it.data)
                        with(binding) {
                            textNamaUser.setText(listUser[0].nama)
                            tietNamaLengkap.setText(listUser[0].nama)
                            tietTglLahir.setText(listUser[0].tglLahir)
                            tietNoHP.setText(listUser[0].noHP)
                            tietEmail.setText(listUser[0].email)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
            }
        })
    }

    private fun updateLable(myCalendar: Calendar, binding: ActivityEditAkunBinding) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tietTglLahir.setText(sdf.format(myCalendar.time))
    }
}