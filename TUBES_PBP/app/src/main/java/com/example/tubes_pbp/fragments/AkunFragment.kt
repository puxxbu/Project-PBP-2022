package com.example.tubes_pbp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.tubes_pbp.MainActivity
import com.example.tubes_pbp.PrefManager
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.entity.room.UsersDB
import kotlinx.android.synthetic.main.fragment_akun.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        binding.tietNamaLengkap.setText(nama)
        binding.tietTglLahir.setText(tglLahir)
        binding.tietNoHP.setText(noHP)
        binding.tietEmail.setText(email)

        binding.btnEdit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val id = prefManager.getUser()?.id
                val nama = binding.tilNamaLengkap.getEditText()?.getText().toString()
                val tglLahir = binding.tilTglLahir.getEditText()?.getText().toString()
                val noHP = binding.tilTglLahir.getEditText()?.getText().toString()
                val email = binding.tilEmail.getEditText()?.getText().toString()
                usersDb.usersDao().updateUser(id,nama,tglLahir,noHP,email)

            }
        }



        binding.btnLogout.setOnClickListener {
            dialog.show(parentFragmentManager, "alertLogout" )
        }

        return binding.root
    }

}