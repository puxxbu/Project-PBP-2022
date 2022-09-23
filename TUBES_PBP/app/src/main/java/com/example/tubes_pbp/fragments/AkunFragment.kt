package com.example.tubes_pbp.fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tubes_pbp.MainActivity
import com.example.tubes_pbp.PrefManager
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.databinding.FragmentEditAkunBinding
import com.example.tubes_pbp.entity.room.UsersDB
import kotlinx.android.synthetic.main.fragment_akun.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AkunFragment : Fragment(R.layout.fragment_akun) {
    private var _binding : FragmentAkunBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentAkunBinding.inflate(inflater, container, false)

        var dialog = LogoutAlert()
        val rootView: View = inflater.inflate(R.layout.fragment_akun, container, false)

        prefManager = PrefManager(requireContext())
        usersDb = UsersDB.getDatabase(requireContext())

        val nama = prefManager.getUser()?.nama
        val tglLahir = prefManager.getUser()?.tglLahir
        val noHP = prefManager.getUser()?.noHP
        val email = prefManager.getUser()?.email

        binding.textNamaUser.setText(nama)
        binding.tietNamaLengkap.setText(nama)
        binding.tietTglLahir.setText(tglLahir)
        binding.tietNoHP.setText(noHP)
        binding.tietEmail.setText(email)

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
            CoroutineScope(Dispatchers.IO).launch {
                val id = prefManager.getUser()?.id
                val nama = binding.tilNamaLengkap.getEditText()?.getText().toString()
                val tglLahir = binding.tilTglLahir.getEditText()?.getText().toString()
                val noHP = binding.tilNoHP.getEditText()?.getText().toString()
                val email = binding.tilEmail.getEditText()?.getText().toString()
                usersDb.usersDao().updateUser(id,nama,tglLahir,noHP,email)
                val user = usersDb.usersDao().getUserbyID(id)
                prefManager.setUser(user)



                withContext(Dispatchers.Main){
                    Toast.makeText(getActivity(), "Data berhasil di Edit", Toast.LENGTH_SHORT).show()
                    binding.textNamaUser.setText(nama)
                    binding.tietNamaLengkap.setText(nama)
                    binding.tietTglLahir.setText(tglLahir)
                    binding.tietNoHP.setText(noHP)
                    binding.tietEmail.setText(email)
                }


            }
        }



        binding.btnLogout.setOnClickListener {
            dialog.show(parentFragmentManager, "alertLogout" )
        }

        return binding.root
    }

    private fun updateLable(myCalendar: Calendar, binding: FragmentAkunBinding) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tietTglLahir.setText(sdf.format(myCalendar.time))
    }

}