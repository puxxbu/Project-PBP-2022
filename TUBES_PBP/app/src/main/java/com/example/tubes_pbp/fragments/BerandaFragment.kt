package com.example.tubes_pbp.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.databinding.FragmentBerandaBinding
import com.example.tubes_pbp.databinding.FragmentPesananBinding
import com.example.tubes_pbp.maps.MapActivity
import com.example.tubes_pbp.webapi.BookmarkAdapter
import com.example.tubes_pbp.webapi.BookmarkData
import com.example.tubes_pbp.webapi.RClient
import com.example.tubes_pbp.webapi.ResponseDataBookmark
import com.example.tubes_pbp.webapi.pesananapi.PesananAdapter
import com.example.tubes_pbp.webapi.pesananapi.PesananData
import com.example.tubes_pbp.webapi.pesananapi.ResponseDataPesanan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaFragment : Fragment(R.layout.fragment_beranda) {
    private var _binding : FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private val listBookmark = ArrayList<BookmarkData>()
    private val listPesanan = ArrayList<PesananData>()


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

        getDataBookmark()
        getDataPesanan()

        binding.tmlMaps.setOnClickListener{
            startActivity(moveMap)
        }




        return rootView
    }

    private fun getDataBookmark() {
        RClient.instances.getAllData().enqueue(object :
            Callback<ResponseDataBookmark> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDataBookmark>,
                response: Response<ResponseDataBookmark>
            ){
                if (response.isSuccessful){
                    listBookmark.clear()
                    response.body()?.let { listBookmark.addAll(it.data) }
                    Log.d(ContentValues.TAG,context.toString() +"CONTEXT")

                    if ( isAdded() ){
                        with(binding){
                            tvMarks.setText(listBookmark.size.toString())
                        }

                    }



                }
            }
            override fun onFailure(call: Call<ResponseDataBookmark>, t: Throwable) {
                Log.d(ContentValues.TAG, "GAGAL")

            }
        }
        )
    }

    private fun getDataPesanan() {
        com.example.tubes_pbp.webapi.pesananapi.RClient.instances.getAllData().enqueue(object : Callback<ResponseDataPesanan> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDataPesanan>,
                response: Response<ResponseDataPesanan>
            ){
                if (response.isSuccessful){
                    listPesanan.clear()
                    response.body()?.let { listPesanan.addAll(it.data) }

                    if ( isAdded() ){
                        with(binding){
                            tvPesanan.setText(listPesanan.size.toString())
                        }
                    }



                }
            }
            override fun onFailure(call: Call<ResponseDataPesanan>, t: Throwable) {
                Log.d(ContentValues.TAG, "GAGAL")

            }
        }
        )
    }


}