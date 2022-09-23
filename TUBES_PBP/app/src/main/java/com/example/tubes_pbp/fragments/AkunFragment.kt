package com.example.tubes_pbp.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.tubes_pbp.R
import kotlinx.android.synthetic.main.fragment_akun.view.*

class AkunFragment : Fragment(R.layout.fragment_akun) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var dialog = LogoutAlert()
        val rootView: View = inflater.inflate(R.layout.fragment_akun, container, false)

        rootView.btnLogout.setOnClickListener {
            dialog.show(parentFragmentManager, "alertLogout" )
        }

        return rootView
    }

}