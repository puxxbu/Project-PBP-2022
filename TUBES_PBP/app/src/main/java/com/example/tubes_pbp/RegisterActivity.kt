package com.example.tubes_pbp

import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isEmpty
import com.example.tubes_pbp.databinding.ActivityMainBinding
import com.example.tubes_pbp.databinding.ActivityRegisterBinding
import com.example.tubes_pbp.entity.room.Users
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.notifications.NotificationReceiver
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager
    private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID = "register_notification"
    private val notificationId = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tietTglLahir.setFocusable(false)
        usersDb = UsersDB.getDatabase(this)

        createNotificationChannel()

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar, binding)
        }

        binding.tietTglLahir.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.regImgBack.setOnClickListener {
            val moveHome = Intent(this, MainActivity::class.java)
            startActivity(moveHome)
        }

        btnRegisterListener(binding)


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    fun btnRegisterListener(binding: ActivityRegisterBinding) {


         binding.regBtnRegister.setOnClickListener(View.OnClickListener {
            var checkRegister = false
            resetAlert(binding)
             val username =  binding.tilUsername.getEditText()?.getText().toString()
             val password: String =   binding.tilPassword.getEditText()?.getText().toString()
             val nama: String = binding.tilNamaLengkap.getEditText()?.getText().toString()
             val noHp: String = binding.tilNoHP.getEditText()?.getText().toString()
             val email: String = binding.tilEmail.getEditText()?.getText().toString()
             val tglLahir: String = binding.tilTglLahir.getEditText()?.getText().toString()

             val mBundle = Bundle()

             mBundle.putString("username", username)
             mBundle.putString("password", password)

             if (username.isEmpty()){
                 binding.tilUsername.setError("Username must be filled ")
                 checkRegister = false
             }

             if (password.isEmpty()){
                 binding.tilPassword.setError("Password must be filled ")
                 checkRegister = false
             }

             if (nama.isEmpty()){
                 binding.tilNamaLengkap.setError("Nama must be filled ")
                 checkRegister = false
             }

             if (noHp.isEmpty()){
                 binding.tilNoHP.setError("Nomor HP must be filled ")
                 checkRegister = false
             }

             if (email.isEmpty()){
                 binding.tilEmail.setError("Email must be filled ")
                 checkRegister = false
             }

             if (tglLahir.isEmpty()){
                 binding.tilTglLahir.setError("Tanggal Lahir must be filled ")
                 checkRegister = false
             }

             if(!nama.isEmpty() && !tglLahir.isEmpty() && !noHp.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() ){
                 checkRegister = true
             }

             if(!checkRegister){
                 return@OnClickListener

             }else {
                val moveLogin = Intent(this, LoginActivity::class.java)

                moveLogin.putExtra("register",mBundle)
                startActivity(moveLogin)
                sendNotification(nama)

                prefManager = PrefManager(this)
                prefManager.setUsername(username)

                val user = Users(0,username,password,nama,email,noHp,tglLahir)
                 // ROOM DELETE
//                CoroutineScope(Dispatchers.IO).launch{
//                     usersDb.usersDao().addUsers(user)
//                     finish()
//                }
                 Toast.makeText(this, "Masukkan berhasil!", Toast.LENGTH_SHORT).show()

             }

             })
    }

    private fun sendNotification(nama :String) {
        val intent : Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0,intent,0)

        val registerBigPicBitmap = ContextCompat.getDrawable(this, R.drawable.checklist)?.toBitmap()

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        // input data nama user pada binding text pada register layout
//        broadcastIntent.putExtra("toastMessage", binding?.etMessage?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_beranda_24)
            .setContentTitle("Terima kasih telah mendaftar!")
            .setContentText("Selamat datang di YourTravel, ${nama}")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(registerBigPicBitmap))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.CYAN)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }

    }

    private fun resetAlert(binding: ActivityRegisterBinding) {
        binding.tilEmail.setError(null)
        binding.tilUsername.setError(null)
        binding.tilNoHP.setError(null)
        binding.tilTglLahir.setError(null)
        binding.tilNamaLengkap.setError(null)
        binding.tilPassword.setError(null)
    }


    private fun updateLable(myCalendar: Calendar, binding: ActivityRegisterBinding) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tietTglLahir.setText(sdf.format(myCalendar.time))
    }

}

