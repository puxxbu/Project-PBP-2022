package com.example.tubes_pbp.fragments
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.tubes_pbp.MyAdapter
//import com.example.tubes_pbp.R
//import com.example.tubes_pbp.entity.Hotel
//
//class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
//
//    lateinit var adapter: MyAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//       return inflater.inflate(R.layout.fragment_bookmark, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val layoutManager = LinearLayoutManager(context)
//        val rvHotel : RecyclerView = view.findViewById(R.id.hotelView)
//        init()
//        rvHotel.layoutManager = layoutManager
//        rvHotel.adapter = adapter
//
//    }
//
//
//    private fun init() {
//
//
//        var data = ArrayList<Hotel>()
//        data.add(Hotel(R.drawable.hotel7, "Four Seasons Resort Bali", "Jimbaran Bay Jimbaran"))
//        data.add(Hotel(R.drawable.hotel8, "Jumana Bali Ungasan", "Jl. Melasti, Banjar Kelod Ungasan Uluwatu"))
//        data.add(Hotel(R.drawable.hotel9, "Padma Resort Legian", "Jln. Padma No.1 PO BOX 1107 TBB Legian"))
//        data.add(Hotel(R.drawable.hotel10, "The St. Regis Bali Resort", "Kawasan Pariwisata Nusa Dua Lot S6 PO BOX 44 Nusa Dua"))
//        data.add(Hotel(R.drawable.hotel11, "Fairmont Jakarta", "Jl. Asia Afrika No. 8, Gelora Bung Karno Jakarta"))
//        data.add(Hotel(R.drawable.hotel12, "Hotel Borobudur Jakarta", "Jalan Lapangan Benteng Selatan No. 1 Jakarta"))
//        data.add(Hotel(R.drawable.hotel3, "Grand Rohan Yogyakarta", "Jl. Suryotomo No. 31 Yogyakarta"))
//        data.add(Hotel(R.drawable.hotel4, "Sheraton Mustika Yogyakarta Resort and Spa", "Jl. Laksda Adisucipto Km.8,7 Yogyakarta"))
//        data.add(Hotel(R.drawable.hotel11, "Cokro Adimojo Hotel", "Jl. Amidagla No. 31 Yogyakarta"))
//        data.add(Hotel(R.drawable.hotel7, "Hotel Asal Nama", "Jl. Laksda Adisucipto Km.102 Yogyakarta"))
//
//        adapter = MyAdapter(data)
//    }
//
//}


// UNTUK MAIN ACTIVITY BOOKMARKNYA

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
//    private lateinit var binding : ActivityMainBinding

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(
            inflater,
            container, false
        )


        return binding.root
//        getDataMahasiswa()
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