package com.example.tubes_pbp.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.FragmentBookmarkBinding
import com.example.tubes_pbp.qrcode.QRCodeBookmark
import com.example.tubes_pbp.webapi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
//    private lateinit var binding : ActivityMainBinding

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val listBookmark = ArrayList<BookmarkData>()

    override fun onStart() {
        super.onStart()
        getDataBookmark()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(
            inflater,
            container, false
        )

        Log.d(TAG, "RELOAD")
        getDataBookmark()

        binding.txtCari.setOnKeyListener(View.OnKeyListener{ _, keyCode, event->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            {
                getDataBookmark()
                return@OnKeyListener true
            }
            false
        })

        binding.btnAdd.setOnClickListener {
            var i = Intent(context,
                FormAddBookmarkActivity::class.java)
            context?.startActivity(i)
        }

        binding.btnAdd2.setOnClickListener {
            var i = Intent(context,
                QRCodeBookmark::class.java)
            context?.startActivity(i)
        }


        return binding.root


    }
    private fun getDataBookmark() {
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager= LinearLayoutManager(context)
        val bundle = arguments
        val cari =  binding.txtCari.text.toString()
        binding.progressBar.visibility
        RClient.instances.getData(cari).enqueue(object :
            Callback<ResponseDataBookmark> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDataBookmark>,
                response: Response<ResponseDataBookmark>
            ){
                if (response.isSuccessful){
                    listBookmark.clear()
                    response.body()?.let { listBookmark.addAll(it.data) }
                    Log.d(TAG,context.toString() +"CONTEXT")

                    if ( isAdded() ){
                        val adapter = BookmarkAdapter(listBookmark, requireContext())
                        binding.rvData.adapter = adapter
                        adapter.notifyDataSetChanged()
                        binding.progressBar.isVisible = false
                    }



                }
            }
            override fun onFailure(call: Call<ResponseDataBookmark>, t: Throwable) {
                Log.d(TAG, "GAGAL")

            }
        }
        )
    }






}
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        showDataFragment()
//        binding.txtCari.setOnKeyListener(View.OnKeyListener{ _, keyCode, event->
//            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
//            {
//                showDataFragment()
//                return@OnKeyListener true
//            }
//            false
//        })
//        binding.btnAdd.setOnClickListener{
//            startActivity(Intent(this, FormAddBookmarkActivity::class.java))
//        }
//    }
//    fun showDataFragment() {
//        val mFragmentManager = supportFragmentManager
//        val mFragmentTransaction = mFragmentManager.beginTransaction()
//        val mFragment = DataBookmarkFragment()
//        val textCari = binding.txtCari.text
//        val mBundle = Bundle()
//        mBundle.putString("cari", textCari.toString())
//        mFragment.arguments = mBundle
//        mFragmentTransaction.replace(R.id.fl_data, mFragment).commit()
//    }
//}