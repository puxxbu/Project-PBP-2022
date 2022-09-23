package com.example.tubes_pbp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.entity.room.Constant
import com.example.tubes_pbp.entity.room.Pesanan
import com.example.tubes_pbp.entity.room.UsersDB
import kotlinx.android.synthetic.main.activity_edit_pesanan.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPesanan : AppCompatActivity()  {
    private lateinit var usersDb: UsersDB
    private var pesananId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pesanan)
        usersDb = UsersDB.getDatabase(this)

        setupView()
        setupListener()

    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getBookmark()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getBookmark()
            }
        }
    }

    private fun setupListener(){
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                usersDb.pesananDao().addBookmark(
                    Pesanan(pesananId, edit_nama_hotel.text.toString(),
                        edit_wilayah_hotel.text.toString(), edit_alamat_hotel.text.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                 usersDb.pesananDao().updateBookmark(
                    Pesanan(pesananId, edit_nama_hotel.text.toString(),
                        edit_wilayah_hotel.text.toString(), edit_alamat_hotel.text.toString())
                )
                finish()
            }
        }
    }

    fun getBookmark(){
        pesananId = intent.getIntExtra("intent_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val bookmarks =  usersDb.pesananDao().getBookmark(pesananId)[0]
            edit_nama_hotel.setText(bookmarks.namaHotel)
            edit_wilayah_hotel.setText(bookmarks.wilayahHotel)
            edit_alamat_hotel.setText(bookmarks.alamatHotel)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}

