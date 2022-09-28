package com.example.tubes_pbp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.entity.room.Constant
import com.example.tubes_pbp.entity.room.Pesanan
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.notifications.NotificationReceiver
import kotlinx.android.synthetic.main.activity_edit_pesanan.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPesanan : AppCompatActivity()  {
    private lateinit var usersDb: UsersDB
    private var pesananId : Int = 0
    private val CHANNEL_ID = "pesanan"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pesanan)
        usersDb = UsersDB.getDatabase(this)

        getSupportActionBar()?.setTitle("Pesanan")

        setupView()
        createNotificationChannel()
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
            sendNotification()
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

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pesanan"
            val descriptionText = "Pesanan berhasil dibuat"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        val intent = Intent(this, EditPesanan::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        Log.d("notif","NOTIF PESANAN ")
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.travelpic)
            .setContentTitle("Pesanan")
            .setContentText("Pesanan berhasil dibuat!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Pesanan yang anda tambahkan telah berhasil dibuat" +
                    "dan telah ditambahkan di daftar pesanan."))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

}

