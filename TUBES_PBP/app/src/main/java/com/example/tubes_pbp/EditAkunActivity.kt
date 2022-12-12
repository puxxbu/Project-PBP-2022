package com.example.tubes_pbp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tubes_pbp.databinding.FragmentEditAkunBinding
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.webapi.RClient
import com.example.tubes_pbp.webapi.ResponseCreate
import com.example.tubes_pbp.webapi.userApi.ResponseDataUser
import com.example.tubes_pbp.webapi.userApi.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditAkunActivity : Fragment(R.layout.fragment_edit_akun) {
    private var _binding : FragmentEditAkunBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager

    private val listUser = ArrayList<UserData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_akun,container, false)

        val rootView: View = inflater.inflate(R.layout.fragment_edit_akun, container, false)

        prefManager = PrefManager(requireContext())
        usersDb = UsersDB.getDatabase(requireContext())

        binding.user = prefManager.getUser()
        setDataUser()

//        val nama = prefManager.getUser()?.nama
//        val tglLahir = prefManager.getUser()?.tglLahir
//        val noHP = prefManager.getUser()?.noHP
//        val email = prefManager.getUser()?.email
//
//        binding.textNamaUser.setText(nama)
//        binding.tietNamaLengkap.setText(nama)
//        binding.tietTglLahir.setText(tglLahir)
//        binding.tietNoHP.setText(noHP)
//        binding.tietEmail.setText(email)

        val myCalendar = Calendar.getInstance()

        binding.tietTglLahir.setFocusable(false)

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar, binding)
        }

        binding.tietTglLahir.setOnClickListener {
            Log.d("CALENDAR", "TES MASUK KALENDER HARUSE")
            DatePickerDialog(
                requireContext(),
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnEdit.setOnClickListener {
            updateDataUser()


//            CoroutineScope(Dispatchers.IO).launch {
//                val id = prefManager.getUser()?.id
//                val nama = binding.tilNamaLengkap.getEditText()?.getText().toString()
//                val tglLahir = binding.tilTglLahir.getEditText()?.getText().toString()
//                val noHP = binding.tilNoHP.getEditText()?.getText().toString()
//                val email = binding.tilEmail.getEditText()?.getText().toString()
//                usersDb.usersDao().updateUser(id,nama,tglLahir,noHP,email)
//
//
//                withContext(Dispatchers.Main){
//                    val user = usersDb.usersDao().getUserbyID(id)
//                    prefManager.setUser(user)
//
//
//                    sendNotification()
//                }
//
//
//            }
        }
        return binding.root
    }

    private fun updateLable(myCalendar: Calendar, binding: FragmentEditAkunBinding) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tietTglLahir.setText(sdf.format(myCalendar.time))
    }


    fun setDataUser(){
        RClient.instances.getDataUser(prefManager.getUserID()).enqueue(object :
            Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                if (response.isSuccessful){
                    response.body()?.let {
                        listUser.addAll(it.data)
                        with(binding) {
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

    fun updateDataUser(){
        val nama = binding.tilNamaLengkap.getEditText()?.getText().toString()
        val tglLahir = binding.tilTglLahir.getEditText()?.getText().toString()
        val noHP = binding.tilNoHP.getEditText()?.getText().toString()
        val email = binding.tilEmail.getEditText()?.getText().toString()


        RClient.instances.updateDataUser(prefManager.getUserID(),listUser[0].username,listUser[0].password,nama,email,noHP,tglLahir).enqueue(object :
            Callback<ResponseCreate> {
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {
                if(response.isSuccessful) {
                    Toast.makeText(getActivity(),"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
            }
        })
    }
}