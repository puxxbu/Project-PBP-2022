package com.example.tubes_pbp.webapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes_pbp.databinding.FragmentDataBookmarkBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class DataBookmarkFragment : Fragment() {
    private var _binding: FragmentDataBookmarkBinding? = null

    private val binding get() = _binding!!
    private val listBookmark = ArrayList<BookmarkData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBookmarkBinding.inflate(inflater,
            container, false)
        return binding.root
        getDataBookmark()
    }
    override fun onStart() {
        super.onStart()
        getDataBookmark()
    }
    private fun getDataBookmark() {
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager= LinearLayoutManager(context)
        val bundle = arguments
        val cari = bundle?.getInt(/* key = */ "cari")
        binding.progressBar.visibility
        RClient.instances.getAllData().enqueue(object : Callback<ResponseDataBookmark> {
            override fun onResponse(
                call: Call<ResponseDataBookmark>,
                response: Response<ResponseDataBookmark>
            ){
                if (response.isSuccessful){
                    listBookmark.clear()
                    response.body()?.let {
                        listBookmark.addAll(it.data) }
                    val adapter =
                        BookmarkAdapter(listBookmark, requireContext())
                    binding.rvData.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                }
            }
            override fun onFailure(call: Call<ResponseDataBookmark>, t:
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