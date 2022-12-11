package com.example.tubes_pbp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.databinding.FragmentBerandaBinding
import com.example.tubes_pbp.databinding.FragmentPesananBinding
import com.example.tubes_pbp.maps.MapActivity

class BerandaFragment : Fragment(R.layout.fragment_beranda) {
    private var _binding : FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val moveMap = Intent(getActivity(), MapActivity::class.java)

//        binding.btnMap.setOnClickListener{
//            startActivity(moveMap)
//        }

        binding.tmlMaps.setOnClickListener{
            startActivity(moveMap)
        }




        return rootView
    }

}