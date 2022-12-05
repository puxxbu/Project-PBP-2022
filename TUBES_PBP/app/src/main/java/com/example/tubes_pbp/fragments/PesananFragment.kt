package com.example.tubes_pbp.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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
import com.example.tubes_pbp.databinding.FragmentPesananBinding
import com.example.tubes_pbp.qrcode.QRCodePesanan
import com.example.tubes_pbp.webapi.pesananapi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananFragment : Fragment(R.layout.fragment_pesanan) {
//    private lateinit var binding : ActivityMainBinding

    private var _binding: FragmentPesananBinding? = null
    private val binding get() = _binding!!
    private val listPesanan = ArrayList<PesananData>()

    override fun onStart() {
        super.onStart()
        getDataPesanan()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPesananBinding.inflate(
            inflater,
            container, false
        )

        Log.d(TAG, "RELOAD")
        getDataPesanan()

        binding.txtCari.setOnKeyListener(View.OnKeyListener{ _, keyCode, event->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            {
                getDataPesanan()
                return@OnKeyListener true
            }
            false
        })

        binding.btnAdd.setOnClickListener {
            var i = Intent(context,
                FormAddPesananActivity::class.java)
            context?.startActivity(i)
        }

        binding.btnAdd2.setOnClickListener {
            var i = Intent(context,
                QRCodePesanan::class.java)
            context?.startActivity(i)
        }


        return binding.root


    }
    private fun getDataPesanan() {
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager= LinearLayoutManager(context)
        val bundle = arguments
        val cari =  binding.txtCari.text.toString()
        binding.progressBar.visibility
        RClient.instances.getData(cari).enqueue(object : Callback<ResponseDataPesanan> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDataPesanan>,
                response: Response<ResponseDataPesanan>
            ){
                if (response.isSuccessful){
                    listPesanan.clear()
                    response.body()?.let { listPesanan.addAll(it.data) }
                    Log.d(TAG,context.toString() +"CONTEXT")

                    if ( isAdded() ){
                        val adapter = PesananAdapter(listPesanan, requireContext())
                        binding.rvData.adapter = adapter
                        adapter.notifyDataSetChanged()
                        binding.progressBar.isVisible = false
                    }



                }
            }
            override fun onFailure(call: Call<ResponseDataPesanan>, t: Throwable) {
                Log.d(TAG, "GAGAL")

            }
        }
        )
    }
}
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import androidx.core.content.ContextCompat.getSystemService
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.tubes_pbp.EditPesanan
//import com.example.tubes_pbp.PesananAdapter
//import com.example.tubes_pbp.R
//import com.example.tubes_pbp.databinding.FragmentAkunBinding
//import com.example.tubes_pbp.databinding.FragmentPesananBinding
//import com.example.tubes_pbp.entity.room.Constant
//import com.example.tubes_pbp.entity.room.Pesanan
//import com.example.tubes_pbp.entity.room.UsersDB
//import kotlinx.android.synthetic.main.fragment_pesanan.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class PesananFragment : Fragment(R.layout.fragment_pesanan) {
//    private lateinit var usersDb: UsersDB
//    lateinit var pesananAdapter: PesananAdapter
//
//    private var _binding : FragmentPesananBinding? = null
//    private val binding get() = _binding!!
//    private val CHANNEL_ID = "pesanan"
//    private val notificationId = 101
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentPesananBinding.inflate(inflater, container, false)
//        val rootView = binding.root
//
//        usersDb = UsersDB.getDatabase(requireContext())
//
//        binding.buttonCreate.setOnClickListener{
//            intentEdit(0, Constant.TYPE_CREATE)
//        }
////        setupRecyclerView(rootView)
//
//        return rootView
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        pesananAdapter = PesananAdapter(arrayListOf(), object :
//            PesananAdapter.OnAdapterListener{
//            override fun onClick(pesanan: Pesanan) {
//                intentEdit(pesanan.id, Constant.TYPE_READ)
//            }
//
//            override fun onUpdate(pesanan: Pesanan) {
//                intentEdit(pesanan.id, Constant.TYPE_UPDATE)
//            }
//
//            override fun onDelete(pesanan: Pesanan) {
//                deleteDialog(pesanan,view)
//            }
//        })
//        list_hotel.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = pesananAdapter
//        }
//
//    }
//
//
////    private fun setupRecyclerView(view: View){
////        pesananAdapter = PesananAdapter(arrayListOf(), object :
////            PesananAdapter.OnAdapterListener{
////            override fun onClick(pesanan: Pesanan) {
////                intentEdit(pesanan.id, Constant.TYPE_READ)
////            }
////
////            override fun onUpdate(pesanan: Pesanan) {
////                intentEdit(pesanan.id, Constant.TYPE_UPDATE)
////            }
////
////            override fun onDelete(pesanan: Pesanan) {
////                deleteDialog(pesanan,view)
////            }
////        })
////        list_hotel.apply {
////            layoutManager = LinearLayoutManager(context)
////            adapter = pesananAdapter
////        }
////    }
//
//    private fun deleteDialog(pesanan: Pesanan, view: View){
//        val alertDialog = AlertDialog.Builder(view.getContext())
//        alertDialog.apply {
//            setTitle("Confirmation")
//            setMessage("Are You Sure to delete this data From ${pesanan.namaHotel}?")
//            setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
//                dialogInterface.dismiss()
//            })
//            setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->
//                dialogInterface.dismiss()
//                CoroutineScope(Dispatchers.IO).launch {
//                    usersDb.pesananDao().deleteBookmark(pesanan)
//                    loadData()
//                }
//            })
//        }
//        alertDialog.show()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        loadData()
//    }
//
//    fun loadData(){
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = usersDb.pesananDao().getBookmark()
//            Log.d("PesananFragment","dbResponse: $notes")
//            withContext(Dispatchers.Main){
//                pesananAdapter.setData( notes )
//            }
//        }
//    }
//
//
//
//    fun intentEdit(bookmarkId : Int, intentType: Int){
//        startActivity(
//            Intent(getActivity(), EditPesanan::class.java)
//                .putExtra("intent_id", bookmarkId)
//                .putExtra("intent_type", intentType)
//        )
//    }
//
//
//}