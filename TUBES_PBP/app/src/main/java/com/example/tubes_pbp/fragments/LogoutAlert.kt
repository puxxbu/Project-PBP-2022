package com.example.tubes_pbp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.tubes_pbp.MainActivity
import com.example.tubes_pbp.R
import kotlinx.android.synthetic.main.fragment_logout_alert.view.*

class LogoutAlert : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_logout_alert, container, false)
        rootView.btnCancel.setOnClickListener {
            dismiss()
        }

        rootView.btnConfirm.setOnClickListener {
            val intent = Intent ( getActivity(), MainActivity::class.java)
            getActivity()?.onBackPressed()
            startActivity(intent)
        }
        return rootView
    }

}