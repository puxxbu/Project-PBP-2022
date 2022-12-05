package com.example.tubes_pbp.webapi.pesananapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes_pbp.databinding.FragmentDataPesananBinding
import com.example.tubes_pbp.webapi.pesananapi.PesananAdapter
import com.example.tubes_pbp.webapi.pesananapi.PesananData
import com.example.tubes_pbp.webapi.pesananapi.RClient
import com.example.tubes_pbp.webapi.pesananapi.ResponseDataPesanan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class DataPesananFragment : Fragment() {
    private var _binding: FragmentDataPesananBinding? = null

    private val binding get() = _binding!!
    private val listPesanan = ArrayList<PesananData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataPesananBinding.inflate(inflater,
            container, false)
        return binding.root
        getDataPesanan()
    }
    override fun onStart() {
        super.onStart()
        getDataPesanan()
    }
    private fun getDataPesanan() {
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager= LinearLayoutManager(context)
        val bundle = arguments
        val cari = bundle?.getInt(/* key = */ "cari")
        binding.progressBar.visibility
        RClient.instances.getAllData().enqueue(object : Callback<ResponseDataPesanan> {
            override fun onResponse(
                call: Call<ResponseDataPesanan>,
                response: Response<ResponseDataPesanan>
            ){
                if (response.isSuccessful){
                    listPesanan.clear()
                    response.body()?.let {
                        listPesanan.addAll(it.data) }
                    val adapter =
                        PesananAdapter(listPesanan, requireContext())
                    binding.rvData.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                }else{
                    PesananAdapter(listPesanan, requireContext())
                }
            }
            override fun onFailure(call: Call<ResponseDataPesanan>, t:
            Throwable) {
            }
        }
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}